package ma.ensao.swingmap.server.timezone.url;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ma.ensao.swingmap.server.timezone.TimezoneDataDownload;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.ezware.dialog.task.TaskDialogs;

public class HttpUrlTimezoneDataDownload extends TimezoneDataDownload {

	@Override
	public Date parseTimeZoneFile() {
		Date datetime = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File(String.format("tmp/timezone-%s.xml", getClientId())));
			XPathFactory xpath = XPathFactory.instance();
			XPathExpression<Content> expression = xpath.compile("/geonames/timezone/time", Filters.content());
			Content content = expression.evaluateFirst(document);
			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			datetime = formatDateTime.parse(content.getValue());
		} catch (JDOMException | IOException | ParseException e) {
			TaskDialogs.showException(e);
		}
		return datetime;
	}
	
	@Override
	public boolean systemDateTimeIsDownloaded(String latitude, String longitude) {
		boolean isDownloaded = true;
		HttpURLConnection connection = null;
		InputStream fis = null;
		OutputStream fos = null;
		try {
			URL url = new URL(String.format("http://api.geonames.org/timezone?lat=%s&lng=%s&username=supmtioujda", latitude, longitude));
			String tmp = String.format("tmp/timezone-%s.xml", getClientId());
			connection = (HttpURLConnection) url.openConnection();
			connection.setAllowUserInteraction(true);
			fis = connection.getInputStream();
			fos = new FileOutputStream(tmp);
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				TaskDialogs.error(null, "Erreur réponse http", connection.getResponseMessage());
			} else {
				while (true) {
					fos.write(fis.read());
				}
			}
		} catch (EOFException e) {
			
		} catch (Exception e) {
			TaskDialogs.showException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			connection.disconnect();
		}
		return isDownloaded;
	}

}
