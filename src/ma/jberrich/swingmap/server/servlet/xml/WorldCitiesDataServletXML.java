package ma.jberrich.swingmap.server.servlet.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ezware.dialog.task.TaskDialogs;

import ma.jberrich.swingmap.server.servlet.api.WorldCitiesDataAPI;

public class WorldCitiesDataServletXML implements WorldCitiesDataAPI {

	@Override
	public String getContentType() {
		return "text/xml;charset=UTF-8";
	}

	@Override
	public void printCountryList(HttpServletResponse response, List<String> countryList) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<world>");
			for(String country : countryList) {
				out.println(String.format("<country name=\"%s\"/>", country));
			}
			out.println("</world>");
		} catch (IOException e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void printCityList(HttpServletResponse response, List<String> cityList) {
		try {
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<country>");
			for(String city : cityList) {
				out.println(String.format("<city name=\"%s\"/>", city));
			}
			out.println("</country>");
		} catch (IOException e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void printPosition(HttpServletResponse response, double[] position) {
		/*
		 * 	<?xml version="1.0" encoding="UTF-8"?>
		 * 	<city>
		 * 		<position latitude="34.680525" longitude="-1.907639"/>
		 * 	</city>
		 */
		try {
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<city>");
			out.println(String.format("<position latitude=\"%s\" longitude=\"%s\"/>", Double.toString(position[0]), Double.toString(position[1])));
			out.println("</city>");
		} catch (IOException e) {
			TaskDialogs.showException(e);
		}
	}

}
