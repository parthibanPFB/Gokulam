package real.estate.gokulam.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AND I5 on 09-03-2018.
 */

public class DatetimeUtils {


    /**
     * Get Current date from System
     *
     * @param userFormat
     * @return
     */
    public static String getDate(String userFormat) {
        Date d = new Date();
        return DateFormat.format(userFormat, d.getTime()).toString();
    }


    /**
     * Convert date format from user given date
     *
     * @param dateStr
     * @param currentFormat
     * @param convertFormat
     * @return
     * @throws ParseException
     */
    public static String convertDates(String dateStr, String currentFormat, String convertFormat) throws ParseException {

        SimpleDateFormat curFormater = new SimpleDateFormat(currentFormat);
        Date dateObj = curFormater.parse(dateStr);
        SimpleDateFormat postFormater = new SimpleDateFormat(convertFormat);

        return postFormater.format(dateObj);
    }

    public static Long convertMillesFromDate(String inputString, String format) {
        //String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        long mille = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date mDate = sdf.parse(inputString);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            mille = timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mille;
    }
}
