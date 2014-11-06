package ma.jberrich.swingmap.server.timezone.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ma.jberrich.swingmap.server.jaxb.timezone.Geonames;
import ma.jberrich.swingmap.server.timezone.TimezoneDataDownload;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ezware.dialog.task.TaskDialogs;

public class HttpClientTimezoneDataDownload extends TimezoneDataDownload {

	@Override
	public Date parseTimeZoneFile() {
		Date datetime = null;
		try {
			JAXBContext context = JAXBContext.newInstance(Geonames.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Geonames geonames = (Geonames) unmarshaller.unmarshal(new File(String.format("tmp/timezone-%s.xml", getClientId())));
			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			datetime = formatDateTime.parse(geonames.getTimezone().getTime());
		} catch (JAXBException | ParseException e) {
			TaskDialogs.showException(e);
		}		
		return datetime;
	}
	
	@Override
	public boolean systemDateTimeIsDownloaded(String latitude, String longitude) {
		boolean isDownloaded = true;
		HttpClient httpclient = null;
		HttpGet httpget = null;
		FileOutputStream fos = null;
		try {
			String url = String.format("http://api.geonames.org/timezone?lat=%s&lng=%s&username=supmtioujda", latitude, longitude);
			String tmp = String.format("tmp/timezone-%s.xml", getClientId());
			httpclient = new DefaultHttpClient();
			httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			switch (response.getStatusLine().getStatusCode()) {
			case HttpStatus.SC_OK:
				isDownloaded = true;
				break;
			case HttpStatus.SC_CREATED:
				isDownloaded = true;
				break;
			default:
				isDownloaded = false;
				break;
			}
			if (isDownloaded) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					fos = new FileOutputStream(tmp);
					entity.writeTo(fos);
					fos.close();
				}
			} else {
				System.err.println("Error in downloding file");
			}
		} catch (ClientProtocolException e) {
			isDownloaded = false;
			if (httpget != null) {
				httpget.abort();
			}
			TaskDialogs.showException(e);
		} catch (IOException e) {
			isDownloaded = false;
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			TaskDialogs.showException(e);
		} finally {
			if (httpclient != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return isDownloaded;
	}

}
