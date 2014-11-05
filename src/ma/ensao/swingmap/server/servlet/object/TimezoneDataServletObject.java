package ma.ensao.swingmap.server.servlet.object;

import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import ma.ensao.swingmap.server.servlet.api.TimezoneDataAPI;
import ma.ensao.swingmap.shared.data.bean.CityTimezoneBean;

import com.ezware.dialog.task.TaskDialogs;

public class TimezoneDataServletObject implements TimezoneDataAPI {

	@Override
	public String getContentType() {
		return "application/x-java-serialized-object";
//		return "application/octet-stream";
	}

	@Override
	public void printTimezone(HttpServletResponse response, Date datetime) {
		try {
			ObjectOutput out = new ObjectOutputStream(response.getOutputStream());
			CityTimezoneBean bean = new CityTimezoneBean();
			bean.setDatetime(datetime);
			out.writeObject(bean);
			out.flush();
			out.close();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

}
