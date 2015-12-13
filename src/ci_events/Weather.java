/*
AUTHOR  : Eric Valenzuela
CREATED : 11/02/2015
MODIFIED: 11/03/2015
see https://developer.forecast.io/docs/v2
    https://github.com/dvdme/forecastio-lib-java 
    https://developers.google.com/maps/documentation/geocoding/intro for usage information
*/
package ci_events;

import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.ForecastIO;
import com.github.dvdme.ForecastIOLib.FIOAlerts;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import com.google.maps.*;
import com.google.maps.model.GeocodingResult;

public final class Weather
{
    private static final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String FIO_KEY = "d9c4003e29d02d3649675ef8475bc2cf";
    private static final String GEO_KEY = "AIzaSyBkfyhnqFzGUIdkfpuw_3zVEAFo1eenIoU";
    private static final int WEEK = 7;
    private static final int CURRENT = 0;
    private static final int TOO_COLD = 40;
    private static final int TOO_HOT = 80;


    private String[] date;
    private String[] summary; 
    private String[] unixTime;
    private String[] warnings;
    private String[] week;
    private double[] minTemp;
    private double[] maxTemp;
    
    private double lat;
    private double lng;

    private FIODaily daily;
    private FIOAlerts alerts;

    GeocodingResult[] results;

    public Weather(String address)
    {
        init();
        setDate();
        setCoordinates(address);
        setForecast();
        setWeather();
    }

    public void init()
    {
        this.summary = new String[WEEK];
        this.warnings = new String[WEEK];
        this.date = new String[WEEK];
        this.week = new String[WEEK];
        this.minTemp = new double[WEEK];
        this.maxTemp = new double[WEEK];
    }

    public void setDate()
    {
        Calendar current = new GregorianCalendar();
        for (int i = 0; i < WEEK; i++)
        {
            this.week[i] = DAYS[((current.get(Calendar.DAY_OF_WEEK)-1)+i)%WEEK]; 
        } 

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy"); 
        Calendar currentDate = Calendar.getInstance();
        for (int i = 0; i < WEEK; i++)
        {
            date[i] = sdf.format(currentDate.getTime());
            currentDate.add(Calendar.DATE, 1);
        }
    }

    public void setCoordinates(String address){
        GeoApiContext context = new GeoApiContext().setApiKey(GEO_KEY);
        results = GeocodingApi.newRequest(context).address(address).awaitIgnoreError();
        lat = results[CURRENT].geometry.location.lat;
        lng = results[CURRENT].geometry.location.lng;
    }

    public void setForecast()
    {
        ForecastIO fio = new ForecastIO(FIO_KEY);
        fio.setUnits(ForecastIO.UNITS_US);
        fio.setLang(ForecastIO.LANG_ENGLISH);
        fio.getForecast("" + lat, "" + lng);
        daily = new FIODaily(fio);
        alerts = new FIOAlerts(fio);
    }

    public void setWeather()
    {
        if (daily.days() <= 0)
        {
            System.out.println("ERROR: No daily data available!");
        } else
        {
            for (int i = 0; i < WEEK; i++)
            {
                maxTemp[i] = daily.getDay(i).temperatureMax();
                minTemp[i] = daily.getDay(i).temperatureMin();
                summary[i] = daily.getDay(i).summary();
            }
        }
        
        if(alerts.NumberOfAlerts() > 0)
        {
            for(int i = 0; i < alerts.NumberOfAlerts(); i++)
            {
                warnings[i] = alerts.getAlert(i);
            }
        }
    }

    public Calendar getDate()
    {
        return Calendar.getInstance();
    }

    public double [] getWeather(){
        return this.maxTemp;
    }
    
    public String [] getWeek(){
        return this.week;
    }

    public String getSummary(int index) 
    {
        return summary[index] + "\n";
    }

    public String getWarnings()
    { 
        String result = "";
        for(int i = 0; i < alerts.NumberOfAlerts(); i++)
        {  
            result += this.warnings[i] + "\n"; 
        } 
        return result;
    }

    public int withinSevenDays(Date eventDate)
    {
        Date newDate = getDate().getTime();
        int result = (int) ((eventDate.getTime() - newDate.getTime()) / (1000 * 60 * 60 * 24));
        return result; // <= WEEK  ? true : false;
    }

    public void display()
    {
        System.out.printf("7 Day Forecast\nRequested: %s\n", this.week[CURRENT]);
        System.out.println("Location: " + results[CURRENT].formattedAddress);

        System.out.println("\nMax Temp");
        for (int i = 0; i < WEEK; i++)
        {
            System.out.printf("%-10s %.2f\n", this.week[i], maxTemp[i]);
        }

        System.out.println("\nMin Temp");
        for (int i = 0; i < WEEK; i++)
        {
            System.out.printf("%-10s %.2f\n", this.week[i], minTemp[i]);
        }

        System.out.println("\nSummary");
        for (int i = 0; i < WEEK; i++)
        {
            System.out.printf("%-10s %s\n", this.date[i], summary[i]);
        } 
        
        System.out.println("\nAlerts");
        if(alerts.NumberOfAlerts() <= 0)
        {
            System.out.println("No alerts for this location!");
        } 
        else
        {
            for(int i = 0; i < WEEK; i++)
            {
                System.out.printf("%-10s %s\n", this.week[i], warnings[i]);
            }
        }
    }
}
