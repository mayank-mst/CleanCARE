package com.example.dell.cleancare;

public class product {
    private int num;
    private String name;
    private String task;

    public product(int num, String name, String task) {
        this.num = num;
        this.name = name;
        this.task = task;
    }

    public int getNum() {
        return this.num;
    }

    public String getName() {
        return this.name;
    }

    public String getTask() {
        return this.task;
    }
}