package ma.ensao.swingmap.server.servlet.api;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public interface TimezoneDataAPI {
	
	public String getContentType();
	public void printTimezone(HttpServletResponse response, Date datetime);
}
