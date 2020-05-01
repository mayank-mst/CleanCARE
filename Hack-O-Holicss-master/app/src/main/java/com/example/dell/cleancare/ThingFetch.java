package com.example.dell.cleancare;

public class ThingFetch {
    private String id;
    private String gsensor;
    private String counter;

    public ThingFetch(String id, String gsensor,String counter) {
        this.id=id;
       this.gsensor = gsensor;
        this.counter = counter;
    }
    public String getId(){ return  this.id;}

    public String getSensor() {
        return this.gsensor;
    }

    public String getCounter() {
        return this.counter;
    }
}
