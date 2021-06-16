package com.example.farm_connect;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData
{

    private String mTemperature;
    private String mcity ;
    private String mWeatherType;


    public static weatherData fromJson(JSONObject jsonObject)
    {
        try
        {
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getmTemperature()
    {
        return mTemperature+"°C";
    }

    public String getMcity()
    {
        return mcity;
    }

    public String getmWeatherType()
    {
        return mWeatherType;
    }
}
