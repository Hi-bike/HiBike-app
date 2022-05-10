package com.roundG0929.hibike.api.weather;

import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.weather.airconditionDto.AirconditionItem;
import com.roundG0929.hibike.api.weather.wheatherDto.Item;

import java.util.ArrayList;

public class WeatherAnalysis {

    ArrayList<Item> weatherData;
    ArrayList<AirconditionItem> airconditionData;

    int temperature;
    int moisture;
    int rain;
    int cloud;
    int now_RainSnowType;
    boolean isRainSnow = false;

    int pm10Average;
    int pm25Average;
    String pm10Condition;
    String pm25Condition;

    public WeatherAnalysis() {

    }

    public void setWeatherData(ArrayList<Item> weatherDataParameter) {
        this.weatherData = weatherDataParameter;

        for (int i = 0; weatherData.size() > i; i++) {
            if (weatherData.get(i).category.equals("T1H")) {
                temperature = Integer.parseInt(weatherData.get(i).fcstValue);

                break;
            }
        }
        for (int i = 0; weatherData.size() > i; i++) {
            if (weatherData.get(i).category.equals("SKY")) {
                int cloud_amount = Integer.parseInt(weatherData.get(i).fcstValue);

                break;
            }

        }
        for (int i = 0; weatherData.size() > i; i++) {
            if (weatherData.get(i).category.equals("REH")) {
                moisture = Integer.parseInt(weatherData.get(i).fcstValue);

                break;
            }
        }
        for (int i = 0; weatherData.size() > i; i++) {
            if (weatherData.get(i).category.equals("PTY")) {
                now_RainSnowType = Integer.parseInt(weatherData.get(i).fcstValue);

                break;
            }
        }
        for (int i = 0; weatherData.size() > i; i++) {
            if (weatherData.get(i).category.equals("PTY")) {
                if(Integer.parseInt(weatherData.get(i).fcstValue)>0){
                    isRainSnow = true;
                    break;
                }
            }
        }
    }
    public void setAirconditionData(ArrayList<AirconditionItem> airconditionDataParameter){
        this.airconditionData = airconditionDataParameter;
        int sum10=0;
        int sum25=0;

        int count10=0;
        int count25=0;
        for(int i = 0;i<airconditionData.size();i++){
            if(!airconditionData.get(i).pm10Value.equals("-")){
                sum10 += Integer.parseInt(airconditionData.get(i).pm10Value);
                count10++;
            }
            if(!airconditionData.get(i).pm25Value.equals("-")){
                sum25 += Integer.parseInt(airconditionData.get(i).pm25Value);
                count25++;
            }

        }
        pm10Average = (int) sum10/(count10);
        pm25Average = (int) sum25/(count25);

        if(pm10Average<=30){pm10Condition="좋음";}
        else if(pm10Average<=80){pm10Condition="보통";}
        else if(pm10Average<=150){pm10Condition="나쁨";}
        else {pm10Condition="최악";}

        if(pm25Average<=15){pm25Condition="좋음";}
        else if(pm25Average<=35){pm25Condition="보통";}
        else if(pm25Average<=75){pm25Condition="나쁨";}
        else {pm25Condition="최악";}
    }

    public String makeComment(){
        String totalcomment = "자전거 타기 딱 좋은 날씨 🚲";

        if(now_RainSnowType != 0){ totalcomment="비가 오는중이에요☂️"; }
        else if(isRainSnow == true){ totalcomment="비 예보가 있어요🌧";}
        else if(pm10Average > 80 || pm25Average > 75){ totalcomment="공기질이 안 좋아요😨";}
        else if(temperature>=30){ totalcomment="기온이 너무 높아요🔥";}

        return totalcomment;
    }




    public ArrayList<Item> getWeatherData() {
        return weatherData;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public int getPm10Average() {
        return pm10Average;
    }

    public void setPm10Average(int pm10Average) {
        this.pm10Average = pm10Average;
    }

    public int getPm25Average() {
        return pm25Average;
    }

    public void setPm25Average(int pm25Average) {
        this.pm25Average = pm25Average;
    }

    public int getNow_RainSnowType() {
        return now_RainSnowType;
    }

    public void setNow_RainSnowType(int now_RainSnowType) {
        this.now_RainSnowType = now_RainSnowType;
    }

    public boolean isRainSnow() {
        return isRainSnow;
    }

    public void setRainSnow(boolean rainSnow) {
        isRainSnow = rainSnow;
    }

    public String getPm10Condition() {
        return pm10Condition;
    }

    public void setPm10Condition(String pm10Condition) {
        this.pm10Condition = pm10Condition;
    }

    public String getPm25Condition() {
        return pm25Condition;
    }

    public void setPm25Condition(String pm25Condition) {
        this.pm25Condition = pm25Condition;
    }
}
