package com.brewmapp.utils.events.markerCluster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.brewmapp.R;
import com.brewmapp.app.environment.Starter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nixus on 02.12.2017.
 */

public class MapUtils {

    public static String getCityName(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context, new Locale("RU","ru"));
        //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses != null ? addresses.get(0).getLocality() : null;
    }

    public static String calculateDistance(Location restoLocation, Location myLocation) {
        float[] distances = new float[1];
        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                restoLocation.getLatitude(), restoLocation.getLongitude(),
                distances);
        return String.valueOf(distances[0]);
    }


    public static String strJoin(Object[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }

    public static File ImageFormat(File file) {

        int MAX_RESOLUTION =1300;
        int imageHeight ;
        int imageWidth ;

        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        boolean needResize=false;
        if(imageHeight> MAX_RESOLUTION){
            float ratio=(float) MAX_RESOLUTION / (float)imageHeight;
            imageWidth= (int) (imageWidth*ratio);
            needResize=true;
        }
        if(imageWidth> MAX_RESOLUTION){
            float ratio=(float) MAX_RESOLUTION / (float)imageWidth;
            imageHeight= (int) (imageHeight*ratio);
            needResize=true;
        }
        if(needResize)
            bmp=Bitmap.createScaledBitmap(bmp, imageWidth, imageHeight, false);

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(File.createTempFile("tmp","png"));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static Locale getLocaleRu() {
        Locale[] locales=Locale.getAvailableLocales();
        for(Locale locale:locales)
            if(locale.getLanguage().equals("ru"))
                return locale;

        Starter.InfoAboutCrashSendToServer("Locale ru - not found!!!", "MapUtils");
        return null;
    }

    public static Locale getLocaleEn() {
        Locale[] locales=Locale.getAvailableLocales();
        for(Locale locale:locales)
            if(locale.getLanguage().equals("en"))
                return locale;

        Starter.InfoAboutCrashSendToServer("Locale en - not found!!!", "MapUtils");

        return null;
    }


    public static String FormatDate(String timestamp) {

        Date date=parseDate(timestamp);
        if (date!=null) {
            return FormatDate(date);
        }else {
            return "Дата отсутствует";
        }
    }

    public static String FormatDate(Date date) {

        if (date!=null) {
            return android.text.format.DateFormat.format(
                    "dd MMMM yyyy в HH:mm",
                    date
            ).toString();
        }else {
            return "Дата отсутствует";
        }
    }

    public static String FormatTime(String timestamp) {

        Date date=parseDate(timestamp);

        if (date!=null) {
            return android.text.format.DateFormat.format(
                    "HH:mm",
                    date
            ).toString();
        }else {
            return "Дата отсутствует";
        }
    }

    private static Date parseDate(String timestamp) {
        Locale locale=getLocaleEn();
        if(!TextUtils.isEmpty(timestamp)) {

            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(timestamp + " GMT+03:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                if (locale != null)
                    return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", locale).parse(timestamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                if (locale != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", locale);
                    Date date = simpleDateFormat.parse(timestamp);
                    return date;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
