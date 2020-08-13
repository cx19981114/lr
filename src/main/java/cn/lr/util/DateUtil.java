package cn.lr.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static List<String> cutDate(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);
            return findDates(dBegin, dEnd);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public static List<String> findDates( Date dBegin, Date dEnd) throws Exception {
	List<String> listDate = new ArrayList<>();
	Calendar calBegin = Calendar.getInstance();
	calBegin.setTime(dBegin);
	Calendar calEnd = Calendar.getInstance();
	calEnd.setTime(dEnd);
	listDate.add(new SimpleDateFormat("HH:mm").format(calBegin.getTime()));
	while (calEnd.after(calBegin)) {
         calBegin.add(Calendar.MINUTE, 30);
         if (calEnd.after(calBegin))
        	 listDate.add(new SimpleDateFormat("HH:mm").format(calBegin.getTime()));
         else
        	 listDate.add(new SimpleDateFormat("HH:mm").format(calEnd.getTime()));
	}
	return listDate;
	}
}
