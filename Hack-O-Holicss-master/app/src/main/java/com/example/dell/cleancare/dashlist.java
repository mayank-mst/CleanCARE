package com.example.dell.cleancare;

public class dashlist {
    private String last_cleaned_at;
    private String no_of_time;
    private String nissue;
    private String count;
    String wash;

    public dashlist(String wash,String last_cleaned_at, String no_of_time, String nissue, String count) {
        this.last_cleaned_at = last_cleaned_at;
        this.no_of_time = no_of_time;
        this.nissue = nissue;
        this.count = count;
        this.wash=wash;
    }

    public String getLast_cleaned_at() {
        return last_cleaned_at;
    }

    public String getNo_of_time() {
        return no_of_time;
    }

    public String getNissue() {
        return nissue;
    }

    public String getCount() {
        return count;
    }
    public  String getWash(){
        return wash;
    }
}
