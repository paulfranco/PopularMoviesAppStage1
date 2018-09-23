package co.paulfran.paulfranco.popularmoviesappstage1;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateTimeShortForm {

    private static Date getFormattedDate(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.parse(date);
    }


    // Return date in short form based on the device settings.

    public static String getLocalizedDate(Context context, String date, String format)
            throws ParseException {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);

        return dateFormat.format(getFormattedDate(date, format));
    }

}
