/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import java.util.HashMap;
import java.util.Objects;

public class Flight {
    /** id in database */
    long id;
    /** flight number */
    HashMap<String, String> flightNo;
    /** location */
    HashMap<String, String> location;
    /**  flight departure */
    HashMap<String, String>  departure;
    /**  flight arrival */
    HashMap<String, String>  arrival;
    /**  flight speed */
    HashMap<String, String>  speed;
    /** flight altitude */
    String  altitude;
    /**  flight status */
    String status;

    public Flight() {
    }

    public Flight(long id, HashMap<String, String> flightNo, HashMap<String, String> location,
                  HashMap<String, String> departure, HashMap<String, String> arrival,
                  HashMap<String, String> speed, String altitude, String status) {
        this.setId(id);
        this.setFlightNo(flightNo);
        this.setLocation(location);
        this.setDeparture(departure);
        this.setArrival(arrival);
        this.setSpeed(speed);
        this.setAltitude(altitude);
        this.setStatus(status);
    }

    /**
     * get id
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * set id
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * get location
     *
     * @return
     */
    public HashMap<String, String> getLocation() {
        return location;
    }

    /**
     * set location
     *
     * @param location
     */
    public void setLocation(HashMap<String, String> location) {
        this.location = location;
    }

    /**
     * get flight number
     * @return
     */
    public HashMap<String, String> getFlightNo() {
        return flightNo;
    }

    /**
     * set flight number
     *
     * @param flightNo
     */
    public void setFlightNo(HashMap<String, String> flightNo) {
        this.flightNo = flightNo;
    }

    /**
     * get departure
     *
     * @return
     */
    public HashMap<String, String> getDeparture() {
        return departure;
    }

    /**
     * set departure
     *
     * @param departure
     */
    public void setDeparture(HashMap<String, String> departure) {
        this.departure = departure;
    }

    /**
     * get arrival
     *
     * @return
     */
    public HashMap<String, String> getArrival() {
        return arrival;
    }

    /**
     * set arrival
     *
     * @param arrival
     */
    public void setArrival(HashMap<String, String> arrival) {
        this.arrival = arrival;
    }

    /**
     * get speed
     *
     * @return
     */
    public HashMap<String, String> getSpeed() {
        return speed;
    }

    /**
     * set speed
     *
     * @param speed
     */
    public void setSpeed(HashMap<String, String> speed) {
        this.speed = speed;
    }

    /** get altitude
     *
     * @return
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * set altitude
     *
     * @param altitude
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * get status
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * set status
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * to string to get flight number
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getFlightNo().get("number")+"";
    }

    /**
     * override equals()
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return id == flight.id &&
                Objects.equals(flightNo, flight.flightNo) &&
                Objects.equals(location, flight.location) &&
                Objects.equals(departure, flight.departure) &&
                Objects.equals(arrival, flight.arrival) &&
                Objects.equals(speed, flight.speed) &&
                Objects.equals(altitude, flight.altitude) &&
                Objects.equals(status, flight.status);
    }

    /**
     * Override hashCode()
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, flightNo, location, departure, arrival, speed, altitude, status);
    }
}
