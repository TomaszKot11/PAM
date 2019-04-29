package com.example.university.astroweathertwo.utilities.database.entities;

public class City {
    private int id;
    private String name;
    private String locationString;
    private String countryCode;
    private String woeid;

    public City() { }

    public City(String name, String countryCode, String woeid, String locationString) {
        this.name = name;
        this.countryCode = countryCode;
        this.woeid = woeid;
        this.locationString = locationString;
    }

    public int getId() {
        return id;
    }

    public String getLocationString() {
        return locationString;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getWoeid() {
        return woeid;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", locationString='" + locationString + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", woeid='" + woeid + '\'' +
                '}';
    }
}
