package ma.ensao.swingmap.server.servlet.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import ma.ensao.swingmap.server.servlet.api.TimezoneDataAPI;

import com.ezware.dialog.task.TaskDialogs;
import com.google.gson.stream.JsonWriter;

public class TimezoneDataServletJSON implements TimezoneDataAPI {
	
	private String dateTimeFormat = "dd/MM/yyyy HH:mm:ss";

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public void printTimezone(HttpServletResponse response, Date datetime) {
		try {
			JsonWriter writer = new JsonWriter(response.getWriter());
			writer.beginObject();
			writer.name("time").value(new SimpleDateFormat(dateTimeFormat).format(datetime));
			writer.endObject(); 
			writer.close();
		} catch (IOException e) {
			TaskDialogs.showException(e);
		}
	}

}
