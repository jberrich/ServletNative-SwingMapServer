package ma.ensao.swingmap.server.servlet.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface WorldCitiesDataAPI {
	
	public String getContentType();
	public void printCountryList(HttpServletResponse response, List<String> countryList);
	public void printCityList(HttpServletResponse response, List<String> cityList);
	public void printPosition(HttpServletResponse response, double[] position);

}
