package org.smartcity;

import java.io.Serializable;

public class DataEntryDTO implements Serializable {
    private int _timestep;
    private float _vehicleCO;
    private float _vehicleCO2;
    private float _vehicleHC;
    private float _vehicleNOx;
    private float _vehiclePMx;
    private float _vehicleFuel;
    private String _vehicleID;
    private String _vehicleLane;
    private float _vehicleNoise;
    private String _vehicleType;
    private boolean _waiting;
    private float _vehicleX;
    private float _vehicleY;

    public DataEntryDTO(int time, float co, float co2, float hc, float nox, float pmx, float fuel,
                        String id, String lane, float noise, String type, boolean waiting, float x, float y){
        this._timestep = time;
        this._vehicleCO = co;
        this._vehicleCO2 = co2;
        this._vehicleHC = hc;
        this._vehicleNOx = nox;
        this._vehiclePMx = pmx;
        this._vehicleFuel = fuel;
        this._vehicleID = id;
        this._vehicleLane = lane;
        this._vehicleNoise = noise;
        this._vehicleType = type;
        this._waiting = waiting;
        this._vehicleX = x;
        this._vehicleY = y;
    }

    public String getKey(){
        return this._vehicleID;
    }
}
