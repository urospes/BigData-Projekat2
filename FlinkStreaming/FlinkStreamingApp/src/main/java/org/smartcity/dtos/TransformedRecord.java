package org.smartcity.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class TransformedRecord {
    int _timestep;
    String _zone;
    float _vehicleCO;
    float _vehicleCO2;
    float _vehicleHC;
    float _vehicleNOx;
    float _vehiclePMx;
    float _vehicleFuel;
    String _vehicleID;
    String _vehicleLane;
    float _vehicleNoise;
    String _vehicleType;
    boolean _waiting;
    float _vehicleX;
    float _vehicleY;


    @JsonGetter("timestep")
    public int get_timestep() {
        return _timestep;
    }
    @JsonSetter("timestep")
    public void set_timestep(int _timestep) {
        this._timestep = _timestep;
    }



    @JsonGetter("zone")
    public String get_zone() { return _zone;}
    @JsonSetter("zone")
    public void set_zone(String _zone) { this._zone = _zone; }


    @JsonGetter("vehicle_CO")
    public float get_vehicleCO() {
        return _vehicleCO;
    }
    @JsonSetter("vehicle_CO")
    public void set_vehicleCO(float _vehicleCO) {
        this._vehicleCO = _vehicleCO;
    }


    @JsonGetter("vehicle_CO2")
    public float get_vehicleCO2() {
        return _vehicleCO2;
    }
    @JsonSetter("vehicle_CO2")
    public void set_vehicleCO2(float _vehicleCO2) {
        this._vehicleCO2 = _vehicleCO2;
    }


    @JsonGetter("vehicle_HC")
    public float get_vehicleHC() {
        return _vehicleHC;
    }
    @JsonSetter("vehicle_HC")
    public void set_vehicleHC(float _vehicleHC) {
        this._vehicleHC = _vehicleHC;
    }


    @JsonGetter("vehicle_NOx")
    public float get_vehicleNOx() {
        return _vehicleNOx;
    }
    @JsonSetter("vehicle_NOx")
    public void set_vehicleNOx(float _vehicleNOx) {
        this._vehicleNOx = _vehicleNOx;
    }


    @JsonGetter("vehicle_PMx")
    public float get_vehiclePMx() {
        return _vehiclePMx;
    }
    @JsonSetter("vehicle_PMx")
    public void set_vehiclePMx(float _vehiclePMx) {
        this._vehiclePMx = _vehiclePMx;
    }


    @JsonGetter("vehicle_fuel")
    public float get_vehicleFuel() {
        return _vehicleFuel;
    }
    @JsonSetter("vehicle_fuel")
    public void set_vehicleFuel(float _vehicleFuel) {
        this._vehicleFuel = _vehicleFuel;
    }


    @JsonGetter("vehicle_id")
    public String get_vehicleID() {
        return _vehicleID;
    }
    @JsonSetter("vehicle_id")
    public void set_vehicleID(String _vehicleID) {
        this._vehicleID = _vehicleID;
    }


    @JsonGetter("vehicle_lane")
    public String get_vehicleLane() {
        return _vehicleLane;
    }
    @JsonSetter("vehicle_lane")
    public void set_vehicleLane(String _vehicleLane) {
        this._vehicleLane = _vehicleLane;
    }


    @JsonGetter("vehicle_noise")
    public float get_vehicleNoise() {
        return _vehicleNoise;
    }
    @JsonSetter("vehicle_noise")
    public void set_vehicleNoise(float _vehicleNoise) {
        this._vehicleNoise = _vehicleNoise;
    }


    @JsonGetter("vehicle_type")
    public String get_vehicleType() {
        return _vehicleType;
    }
    @JsonSetter("vehicle_type")
    public void set_vehicleType(String _vehicleType) {
        this._vehicleType = _vehicleType;
    }


    @JsonGetter("vehicle_waiting")
    public boolean is_waiting() {
        return _waiting;
    }
    @JsonSetter("vehicle_waiting")
    public void set_waiting(boolean _waiting) {
        this._waiting = _waiting;
    }


    @JsonGetter("vehicle_x")
    public float get_vehicleX() {
        return _vehicleX;
    }
    @JsonSetter("vehicle_x")
    public void set_vehicleX(float _vehicleX) {
        this._vehicleX = _vehicleX;
    }


    @JsonGetter("vehicle_y")
    public float get_vehicleY() {
        return _vehicleY;
    }
    @JsonSetter("vehicle_y")
    public void set_vehicleY(float _vehicleY) {
        this._vehicleY = _vehicleY;
    }



    public TransformedRecord() {}
    public TransformedRecord(MessageDTO record){
        this._timestep = record._timestep;
        this._vehicleCO = record._vehicleCO;
        this._vehicleCO2 = record._vehicleCO2;
        this._vehicleHC = record._vehicleHC;
        this._vehicleNOx = record._vehicleNOx;
        this._vehiclePMx = record._vehiclePMx;
        this._vehicleFuel = record._vehicleFuel;
        this._vehicleID = record._vehicleID;
        this._vehicleLane = record._vehicleLane;
        this._vehicleNoise = record._vehicleNoise;
        this._vehicleType = record._vehicleType;
        this._waiting = record._waiting;
        this._vehicleX = record._vehicleX;
        this._vehicleY = record._vehicleY;
    }
}
