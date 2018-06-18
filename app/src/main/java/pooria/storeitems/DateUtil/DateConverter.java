package pooria.storeitems.DateUtil;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class DateConverter {
  private static final String LOG_TAG=DateConverter.class.getSimpleName();
private DateConverter (){

}




//this method gets current miladi date in Timestamp and change to Jalali date
public static String getShamsiDateFromLocalTimestamp(Long TimeStamp){
  PersianDate persianDate=new PersianDate(TimeStamp);

  PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d H:i:s");
  String PersianDateString= pdformater1.format(persianDate);//1396/05/20

  Log.d(LOG_TAG,"Persiandate: "+PersianDateString);

return PersianDateString;
}


//make miladi Date from Timestamp;
public static String makeMiladiFRomTimestamp(Long timestamp){
  Date date = new Date(timestamp);
  SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");

  String formattedTimeandDate = format.format(date);
  Log.d(LOG_TAG,"MiladiDate: "+formattedTimeandDate);


  return formattedTimeandDate;

}



}
