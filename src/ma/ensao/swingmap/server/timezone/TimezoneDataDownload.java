package ma.ensao.swingmap.server.timezone;

import java.util.Date;

import ma.ensao.swingmap.shared.data.TimezoneData;

public abstract class TimezoneDataDownload implements TimezoneData {

	private String id = null;

	public Date getDateTimeZoneSystem(double latitude, double longitude) {
		Date date = null;
		if (systemDateTimeIsDownloaded(Double.toString(latitude), Double.toString(longitude))) {
			date = parseTimeZoneFile();
		}
		return date;
	}

	public abstract Date parseTimeZoneFile();

	public abstract boolean systemDateTimeIsDownloaded(String latitude, String longitude);

	@Override
	public String getClientId() {
		return id;
	}

	@Override
	public void setClientId(String id) {
		this.id = id;
	}
	
}
