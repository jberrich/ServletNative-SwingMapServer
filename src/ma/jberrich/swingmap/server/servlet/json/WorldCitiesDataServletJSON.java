package ma.jberrich.swingmap.server.servlet.json;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ma.jberrich.swingmap.server.servlet.api.WorldCitiesDataAPI;

import com.ezware.dialog.task.TaskDialogs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

public class WorldCitiesDataServletJSON implements WorldCitiesDataAPI {

	@Override
	public String getContentType() {
		return "application/json";
	}

	@Override
	public void printCountryList(HttpServletResponse response, List<String> countryList) {
	    try {
	    	response.setCharacterEncoding("UTF-8");
//			out.println(new Gson().toJson(countryList));
			Gson gson = new GsonBuilder().create();
			gson.toJson(countryList, response.getWriter());
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void printCityList(HttpServletResponse response, List<String> cityList) {
	    try {
	    	response.setCharacterEncoding("UTF-8");
//			out.println(new Gson().toJson(cityList));
			Gson gson = new GsonBuilder().create();
			gson.toJson(cityList, response.getWriter());
		} catch (Exception e) {
			TaskDialogs.showException(e);
		} 
	}

	@Override
	public void printPosition(HttpServletResponse response, double[] position) {
		/*
		 * 	{
		 * 		"latitude":34.680525,
		 * 		"longitude":-1.907639
		 * 	}
		 */
		try {
			JsonWriter writer = new JsonWriter(response.getWriter());
			writer.beginObject();
			writer.name("latitude").value(position[0]);
			writer.name("longitude").value(position[1]);
			writer.endObject(); 
			writer.close();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

}
