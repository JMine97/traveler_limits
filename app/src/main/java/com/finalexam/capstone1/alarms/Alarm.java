package com.finalexam.capstone1.alarms;

import java.util.Date;

public class Alarm {

    String dept, arrv, airl;   // 출발지, 도착지, 항공사
    int adlt, chld; // 성인, 아동 인원
//    private Date date;      // 출발날짜
    String date, price;
    Boolean stop;   // 경유여부 (경유True, 직항False)

    public Alarm(String dept, String arrv, String airl, int adlt, int chld, String date, String price) {
        this.dept = dept;
        this.arrv = arrv;
        this.airl = airl;
        this.adlt = adlt;
        this.chld = chld;
        this.date = date;
        this.price = price;
    }
}
