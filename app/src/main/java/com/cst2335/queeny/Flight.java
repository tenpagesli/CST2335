package com.cst2335.queeny;

public class Flight {

    String flightNo;
    String location;
    String speed;
    String altitude;
    String status;
    String airportCode;

    public Flight(String flightNo, String location, String speed, String altitude, String status, String airportCode) {
        this.flightNo = flightNo;
        this.location = location;
        this.speed = speed;
        this.altitude = altitude;
        this.status = status;
        this.airportCode = airportCode;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String toString(){
        return "Flight number is: " + this.getFlightNo();
    }

}
