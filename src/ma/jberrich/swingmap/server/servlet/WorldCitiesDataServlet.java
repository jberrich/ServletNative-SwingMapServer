package ma.jberrich.swingmap.server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.jberrich.swingmap.server.jaxb.JAXBWorldCitiesData;
import ma.jberrich.swingmap.server.servlet.api.WorldCitiesDataAPI;
import ma.jberrich.swingmap.server.servlet.json.WorldCitiesDataServletJSON;
import ma.jberrich.swingmap.server.servlet.object.WorldCitiesDataServletObject;
import ma.jberrich.swingmap.shared.data.WorldCitiesData;

@SuppressWarnings("serial")
public class WorldCitiesDataServlet extends HttpServlet {
	
//	private WorldCitiesDataAPI worldCitiesDataAPI = new WorldCitiesDataServletXML();
//	private WorldCitiesDataAPI worldCitiesDataAPI = new WorldCitiesDataServletJSON();
	private WorldCitiesDataAPI worldCitiesDataAPI = new WorldCitiesDataServletObject();
	
//	private WorldCitiesData worldCitiesData = new JDOMWorldCitiesData();
	private WorldCitiesData worldCitiesData = new JAXBWorldCitiesData();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
		resp.setContentType(worldCitiesDataAPI.getContentType());
		if(req.getParameterMap().containsKey("getcountry")) {
			List<String> countryList = worldCitiesData.getCountryList();
			worldCitiesDataAPI.printCountryList(resp, countryList);
		} else if(req.getParameterMap().containsKey("getcity")) {
			String country = req.getParameter("country");
			List<String> cityList = worldCitiesData.getCityList(country);
			if(cityList.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);
				worldCitiesDataAPI.printCityList(resp, cityList);
			}
		} else if(req.getParameterMap().containsKey("getposition")) {
			String country = req.getParameter("country");
			String city = req.getParameter("city");
			double[] position = worldCitiesData.getPosition(country, city);
			if(position == null) {
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);
				worldCitiesDataAPI.printPosition(resp, position);
			}
		}
	}

}
