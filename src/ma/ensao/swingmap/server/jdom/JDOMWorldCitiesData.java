package ma.ensao.swingmap.server.jdom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ma.ensao.swingmap.shared.data.WorldCitiesData;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class JDOMWorldCitiesData implements WorldCitiesData {

	public List<String> getCountryList() {
		List<String> countryList = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File("data/world.xml"));
			XPathFactory xpath = XPathFactory.instance();
			XPathExpression<Element> expression = xpath.compile("//country", Filters.element());
			List<Element> list = expression.evaluate(document);
			for(Element element : list) {
				countryList.add(element.getAttributeValue("name"));
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return countryList;
	}
	
	private String getCountryCode(String country) {
		String code = null;
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File("data/world.xml"));
			XPathFactory xpath = XPathFactory.instance();
			XPathExpression<Element> expression = xpath.compile(String.format("//country[@name='%s']", country), Filters.element());
			Element element = expression.evaluateFirst(document);
			code = element.getAttributeValue("code");
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	public List<String> getCityList(String country) {
		List<String> cityList = new ArrayList<String>();
		String code = getCountryCode(country);
		if(code != null) {
			SAXBuilder builder = new SAXBuilder();
			try {
				Document document = builder.build(new File(String.format("data/%s.xml", code)));
				XPathFactory xpath = XPathFactory.instance();
				XPathExpression<Element> expression = xpath.compile("//city", Filters.element());
				List<Element> list = expression.evaluate(document);
				for(Element element : list) {
					cityList.add(element.getAttributeValue("name"));
				}
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		return cityList;
	}

	public double[] getPosition(String country, String city) {
		double[] position = null;
		String code = getCountryCode(country);
		if(code != null) {
			SAXBuilder builder = new SAXBuilder();
			try {
				Document document = builder.build(new File(String.format("data/%s.xml", code)));
				XPathFactory xpath = XPathFactory.instance();
				XPathExpression<Element> expression = xpath.compile(String.format("//city[@name='%s']", city), Filters.element());
				Element element = expression.evaluateFirst(document);
				double latitude = Double.parseDouble(element.getChildText("latitude"));
				double longitude = Double.parseDouble(element.getChildText("longitude"));
				position = new double[] {latitude, longitude};
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
		return position;
	}

}
