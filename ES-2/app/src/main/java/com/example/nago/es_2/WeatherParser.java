package com.example.nago.es_2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nago.es_2.databases.DatabaseHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nago on 2016-12-01.
 */
public class WeatherParser {
    private Context mContext;
    private ArrayList<WeatherData> shortWeathers;
    private int weatherCode;

    public WeatherParser(Context mContext)
    {
        this.mContext = mContext;
        shortWeathers = new ArrayList<WeatherData>();

        if(isNetWorkConnected() == true)
        {
            ReceiveShortWeather receiveShortWeather = new ReceiveShortWeather();
            receiveShortWeather.execute();
            try {
                receiveShortWeather.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetWorkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public ArrayList<WeatherData> getShortWeathers(){return shortWeathers; }

    public int getWeatherCode(DatabaseHelper mDBHelper)
    {
        if(isNetWorkConnected() == true) {
            String pop = shortWeathers.get(0).getPop();
            String temperature = shortWeathers.get(0).getTemp();
            String Wfkor = shortWeathers.get(0).getWfKor();

            mDBHelper = new DatabaseHelper(mContext);
            if(mDBHelper.copyDatabase(mContext)){}
            else{
                Log.e("TabFragment1","DBCopy Failed");
            }

            weatherCode = mDBHelper.getWeatherCode(pop, temperature, Wfkor);
        }
        Log.e("WeatherCode : ",String.valueOf(weatherCode));
        return weatherCode;
    }


    private class ReceiveShortWeather extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {

            String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4719025300";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


//        protected void onPostExecute(Long result) {
//            String data = "";
//
//            for(int i=0; i<shortWeathers.size(); i++) {
//                if(shortWeathers.get(i).getDay().equals("0") || shortWeathers.get(i).getDay().equals("1"))
//                {
//                    data += shortWeathers.get(i).getDay() + ":" +
//                            shortWeathers.get(i).getTemp() + ":" +
//                            shortWeathers.get(i).getWfKor() + ":" +
//                            shortWeathers.get(i).getPop() + ":";
//                }
//
//            }
//        }

        private void parseXML(String xml) {
            try {
                String tagName = "";
                boolean onDay = false;
                boolean onTem = false;
                boolean onWfKor = false;
                boolean onPop = false;
                boolean onEnd = false;
                boolean isItemTag1 = false;
                int i = 0;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("data")) {
                            shortWeathers.add(new WeatherData());
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                        if (tagName.equals("day") && !onDay) {
                            shortWeathers.get(i).setDay(parser.getText());
                            onDay = true;
                        }
                        if (tagName.equals("temp") && !onTem) {
                            shortWeathers.get(i).setTemp(parser.getText());
                            onTem = true;
                        }
                        if (tagName.equals("wfKor") && !onWfKor) {
                            shortWeathers.get(i).setWfKor(parser.getText());
                            onWfKor = true;
                        }
                        if (tagName.equals("pop") && !onPop) {
                            shortWeathers.get(i).setPop(parser.getText());
                            onPop = true;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("s06") && onEnd == false) {
                            i++;
                            onDay = false;
                            onTem = false;
                            onWfKor = false;
                            onPop = false;
                            isItemTag1 = false;
                            onEnd = true;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
