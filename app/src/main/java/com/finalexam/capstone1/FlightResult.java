package com.finalexam.capstone1;

public class FlightResult {
    private String[] dep_code, arr_code, dep_time, arr_time, carrierCode;
    private String totalTime;
    private int price;

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setDep_code(String[] dep_code) {
        this.dep_code = dep_code;
    }

    public void setDep_code(int i) {
        this.dep_code = new String[i];
    }

    public void setDep_code(String dep_code, int i){
        this.dep_code[i] = dep_code;
    }

    public void setDep_time(String[] dep_time) {
        this.dep_time = dep_time;
    }

    public void setDep_time(int i) {
        this.dep_time = new String[i];
    }

    public void setDep_time(String dep_time, int i){
        this.dep_time[i] = dep_time;
    }

    public void setArr_code(String[] arr_code) {
        this.arr_code = arr_code;
    }

    public void setArr_code(int i) {
        this.arr_code = new String[i];
    }

    public void setArr_code(String arr_code, int i){
        this.arr_code[i] = arr_code;
    }

    public void setArr_time(String[] arr_time) {
        this.arr_time = arr_time;
    }

    public void setArr_time(int i) {
        this.arr_time = new String[i];
    }

    public void setArr_time(String arr_time, int i){
        this.arr_time[i] = arr_time;
    }

    public void setCarrierCode(String[] carrierCode) {
        this.carrierCode = carrierCode;
    }

    public void setCarrierCode(int i) {
        this.carrierCode = new String[i];
    }

    public void setCarrierCode(String carrierCode, int i){
        this.carrierCode[i] = carrierCode;
    }

    public int getPrice() {
        return price;
    }

    public String[] getDep_code() {
        return dep_code;
    }

    public String getDep_code(int i){
        return dep_code[i];
    }

    public String[] getDep_time() {
        return dep_time;
    }

    public String getDep_time(int i){
        return dep_time[i];
    }

    public String[] getArr_code() {
        return arr_code;
    }

    public String getArr_code(int i){
        return arr_code[i];
    }

    public String[] getArr_time() {
        return arr_time;
    }

    public String getArr_time(int i){
        return arr_time[i];
    }

    public String[] getCarrierCode() {
        return carrierCode;
    }

    public String getCarrierCode(int i){
        return carrierCode[i];
    }

    public int getDepCodeSize(){
        return dep_code.length;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public FlightResult(int i){
        this.dep_code = new String[i];
        this.dep_time = new String[i];
        this.arr_code = new String[i];
        this.arr_time = new String[i];
        this.carrierCode = new String[i];
    }

    public FlightResult(String[] dep_code, String[] dep_time, String[] arr_code, String[] arr_time, String[] carrierCode, String totalTime, int price){
        this.dep_code = dep_code;
        this.dep_time = dep_time;
        this.arr_code = arr_code;
        this.arr_time = arr_time;
        this.carrierCode = carrierCode;
        this.totalTime = totalTime;
        this.price = price;
    }

}
