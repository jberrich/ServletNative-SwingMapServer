package ma.ensao.swingmap.server.servlet.object;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ma.ensao.swingmap.server.servlet.api.WorldCitiesDataAPI;
import ma.ensao.swingmap.shared.data.bean.CityListBean;
import ma.ensao.swingmap.shared.data.bean.CityPositionBean;
import ma.ensao.swingmap.shared.data.bean.CountryListBean;

import com.ezware.dialog.task.TaskDialogs;

public class WorldCitiesDataServletObject implements WorldCitiesDataAPI {

	@Override
	public String getContentType() {
		return "application/x-java-serialized-object";
//		return "application/octet-stream";
	}

	@Override
	public void printCountryList(HttpServletResponse response, List<String> countryList) {
		try {
			ObjectOutput out = new ObjectOutputStream(response.getOutputStream());
			CountryListBean bean = new CountryListBean();
			bean.setData(countryList);
			out.writeObject(bean);
			out.flush();
			out.close();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void printCityList(HttpServletResponse response, List<String> cityList) {
		try {
			ObjectOutput out = new ObjectOutputStream(response.getOutputStream());
			CityListBean bean = new CityListBean();
			bean.setData(cityList);
			out.writeObject(bean);
			out.flush();
			out.close();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void printPosition(HttpServletResponse response, double[] position) {
		try {
			ObjectOutput out = new ObjectOutputStream(response.getOutputStream());
			CityPositionBean bean = new CityPositionBean();
			bean.setLatitude(position[0]);
			bean.setLongitude(position[1]);
			out.writeObject(bean);
			out.flush();
			out.close();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

}
