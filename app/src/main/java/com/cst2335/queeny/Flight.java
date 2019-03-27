/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import java.util.HashMap;
import java.util.Objects;

public class Flight {

    /** flight number */
    HashMap<String, String> flightNo;
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

    public Flight(HashMap<String, String> flightNo, HashMap<String, String> departure, HashMap<String, String> arrival, HashMap<String, String> speed, String altitude, String status) {
        this.flightNo = flightNo;
        this.departure = departure;
        this.arrival = arrival;
        this.speed = speed;
        this.altitude = altitude;
        this.status = status;
    }

    public HashMap<String, String> getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(HashMap<String, String> flightNo) {
        this.flightNo = flightNo;
    }

    public HashMap<String, String> getDeparture() {
        return departure;
    }

    public void setDeparture(HashMap<String, String> departure) {
        this.departure = departure;
    }

    public HashMap<String, String> getArrival() {
        return arrival;
    }

    public void setArrival(HashMap<String, String> arrival) {
        this.arrival = arrival;
    }

    public HashMap<String, String> getSpeed() {
        return speed;
    }

    public void setSpeed(HashMap<String, String> speed) {
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

    @Override
    public String toString() {
        return "Flight{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightNo, flight.flightNo) &&
                Objects.equals(departure, flight.departure) &&
                Objects.equals(arrival, flight.arrival) &&
                Objects.equals(speed, flight.speed) &&
                Objects.equals(altitude, flight.altitude) &&
                Objects.equals(status, flight.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNo, departure, arrival, speed, altitude, status);
    }
}
