package ma.jberrich.swingmap.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.jberrich.swingmap.server.ConsoleMessage;
import ma.jberrich.swingmap.server.servlet.api.TimezoneDataAPI;
import ma.jberrich.swingmap.server.servlet.json.TimezoneDataServletJSON;
import ma.jberrich.swingmap.server.servlet.object.TimezoneDataServletObject;
import ma.jberrich.swingmap.server.timezone.http.HttpClientTimezoneDataDownload;
import ma.jberrich.swingmap.shared.data.TimezoneData;

@SuppressWarnings("serial")
public class TimezoneDataServlet extends HttpServlet {

//	private TimezoneDataAPI timezoneDataAPI = new TimezoneDataServletXML();
//	private TimezoneDataAPI timezoneDataAPI = new TimezoneDataServletJSON();
	private TimezoneDataAPI timezoneDataAPI = new TimezoneDataServletObject();
	
	private ConsoleMessage console = null;
	
	private TimezoneData timezoneData = new HttpClientTimezoneDataDownload();
//	private TimezoneData timezoneData = new HttpUrlTimezoneDataDownload();
	
	public TimezoneDataServlet(ConsoleMessage console) {
		this.console = console;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		double latitude = Double.parseDouble(req.getParameter("latitude"));
		double longitude = Double.parseDouble(req.getParameter("longitude"));
		console.displayRequest(req.getRemoteAddr(), latitude, longitude);
		resp.setContentType(timezoneDataAPI.getContentType());
		timezoneData.setClientId(req.getSession().getId());
		timezoneDataAPI.printTimezone(resp, timezoneData.getDateTimeZoneSystem(latitude, longitude));
		console.displayResponse(req.getRemoteAddr());
	}

}
