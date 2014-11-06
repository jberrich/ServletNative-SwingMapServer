package ma.jberrich.swingmap.server.servlet.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.ezware.dialog.task.TaskDialogs;

import ma.jberrich.swingmap.server.servlet.api.TimezoneDataAPI;

public class TimezoneDataServletXML implements TimezoneDataAPI {
	
	private String dateTimeFormat = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String getContentType() {
		return "text/xml;charset=UTF-8";
	}

	@Override
	public void printTimezone(HttpServletResponse response, Date datetime) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<timezone>");
			out.println(String.format("<date time=\"%s\"/>", new SimpleDateFormat(dateTimeFormat).format(datetime)));
			out.println("</timezone>");
		} catch (IOException e) {
			TaskDialogs.showException(e);
		}
	}

}
