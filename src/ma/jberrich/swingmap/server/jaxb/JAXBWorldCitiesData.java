package ma.jberrich.swingmap.server.jaxb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ma.jberrich.swingmap.server.jaxb.city.City;
import ma.jberrich.swingmap.server.jaxb.country.World;
import ma.jberrich.swingmap.shared.data.WorldCitiesData;

public class JAXBWorldCitiesData implements WorldCitiesData {

	public List<String> getCountryList() {
		List<String> countryList = new ArrayList<String>();
		try {
			JAXBContext context = JAXBContext.newInstance(World.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			World world = (World) unmarshaller.unmarshal(new File("data/world.xml"));
			for(ma.jberrich.swingmap.server.jaxb.country.Country country : world.getCountry()) {
				countryList.add(country.getName());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return countryList;
	}
	
	private String getCountryCode(String country) {
		String code = null;
		try {
			JAXBContext context = JAXBContext.newInstance(World.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			World world = (World) unmarshaller.unmarshal(new File("data/world.xml"));
			for(ma.jberrich.swingmap.server.jaxb.country.Country countryBean : world.getCountry()) {
				if(countryBean.getName().equals(country)) {
					code = countryBean.getCode();
					break;
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return code;
	}

	public List<String> getCityList(String country) {
		List<String> cityList = new ArrayList<String>();
		String code = getCountryCode(country);
		if(code != null) {
			try {
				JAXBContext context = JAXBContext.newInstance(ma.jberrich.swingmap.server.jaxb.city.Country.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				ma.jberrich.swingmap.server.jaxb.city.Country countryCity = (ma.jberrich.swingmap.server.jaxb.city.Country) unmarshaller.unmarshal(new File(String.format("data/%s.xml", code)));
				for(City city : countryCity.getCity()) {
					cityList.add(city.getName());
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}			
		}
		return cityList;
	}

	public double[] getPosition(String country, String city) {
		double[] position = null;
		String code = getCountryCode(country);
		if(code != null) {
			try {
				JAXBContext context = JAXBContext.newInstance(ma.jberrich.swingmap.server.jaxb.city.Country.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				ma.jberrich.swingmap.server.jaxb.city.Country countryCity = (ma.jberrich.swingmap.server.jaxb.city.Country) unmarshaller.unmarshal(new File(String.format("data/%s.xml", code)));
				for(City cityBean : countryCity.getCity()) {
					if(cityBean.getName().equals(city)) {
						double latitude = cityBean.getLatitude();
						double longitude = cityBean.getLongitude();
						position = new double[] {latitude, longitude};
						break;
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}	
		}
		return position;
	}

}
