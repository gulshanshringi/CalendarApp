package com.jsrdev.calendarapp;

public class Event {

    private String title , location , description , startDate , endDate;
    private int id;

    public Event(int id ,String title , String location ,String description ,String startDate ,String endDate){
        this.id = id;
        this.title = title;
        this.location = location ;
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle(){return title;}

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
