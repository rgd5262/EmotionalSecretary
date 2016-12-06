package com.example.nago.es_2;

public class WeatherData {
    private String day;
    private String temp;  // 온도
    private String wfKor; // 상태
    private String pop; // 강수확률

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {this.pop = pop;}

    public void setTemp(String temp) {this.temp = temp;}

    public void setWfKor(String wfKor) {this.wfKor = wfKor;}

    public String getTemp() {
        return temp;
    }

    public String getWfKor() {
        return wfKor;
    }

}
