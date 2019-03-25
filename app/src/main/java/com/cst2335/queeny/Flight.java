/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

public class Flight {

    String flightNo; // flight number
    String location; // flight location
    String speed; // flight speed
    String altitude; // flight altitude
    String status; // flight status
    String airportCode; // flight airportCode

    /**
     *  Default constructor
     *
     * @param flightNo
     * @param location
     * @param speed
     * @param altitude
     * @param status
     * @param airportCode
     */
    public Flight(String flightNo, String location, String speed, String altitude, String status, String airportCode) {
        this.flightNo = flightNo;
        this.location = location;
        this.speed = speed;
        this.altitude = altitude;
        this.status = status;
        this.airportCode = airportCode;
    }

    /**
     * get Flight number
     * @return
     */
    public String getFlightNo() {
        return flightNo;
    }

    /**
     * set Flight number
     * @param flightNo
     */
    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    /**
     * get Location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * set Location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get speed
     * @return
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * set Speed
     * @param speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * get Altitude
     * @return
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * set Altitude
     * @param altitude
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * get Status
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * set status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * get airportCode
     * @return
     */
    public String getAirportCode() {
        return airportCode;
    }

    /**
     * set AirportCode
     * @param airportCode
     */
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    /**
     * overriding toString
     * @return
     */
    public String toString(){
        return "Flight number is: " + this.getFlightNo();
    }

}
