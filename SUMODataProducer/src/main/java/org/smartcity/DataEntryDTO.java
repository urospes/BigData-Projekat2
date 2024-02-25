package org.smartcity;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;

public class DataEntryDTO implements Serializable {

    private int _timestep;
    @JsonGetter("timestep")
    public int getTimestep(){
        return this._timestep;
    }

    private float _vehicleCO;
    @JsonGetter("vehicle_CO")
    public float getVehicleCO(){
        return this._vehicleCO;
    }

    private float _vehicleCO2;
    @JsonGetter("vehicle_CO2")
    public float getVehicleCO2(){
        return this._vehicleCO2;
    }

    private float _vehicleHC;
    @JsonGetter("vehicle_HC")
    public float getVehicleHC(){
        return this._vehicleHC;
    }

    private float _vehicleNOx;
    @JsonGetter("vehicle_NOx")
    public float getVehicleNOx(){
        return this._vehicleNOx;
    }

    private float _vehiclePMx;
    @JsonGetter("vehicle_PMx")
    public float getVehiclePMx(){
        return this._vehiclePMx;
    }

    private float _vehicleFuel;
    @JsonGetter("vehicle_fuel")
    public float getVehicleFuel(){
        return this._vehicleFuel;
    }

    private String _vehicleID;
    @JsonGetter("vehicle_id")
    public String getVehicleID(){
        return this._vehicleID;
    }

    private String _vehicleLane;
    @JsonGetter("vehicle_lane")
    public String getVehicleLane(){
        return this._vehicleLane;
    }

    private float _vehicleNoise;
    @JsonGetter("vehicle_noise")
    public float getVehicleNoise(){
        return this._vehicleNoise;
    }

    private String _vehicleType;
    @JsonGetter("vehicle_type")
    public String getVehicleType(){
        return this._vehicleType;
    }

    private boolean _waiting;
    @JsonGetter("vehicle_waiting")
    public boolean getVehicleWaiting(){
        return this._waiting;
    }

    private float _vehicleX;
    @JsonGetter("vehicle_x")
    public float getVehicleX(){
        return this._vehicleX;
    }

    private float _vehicleY;
    @JsonGetter("vehicle_y")
    public float getVehicleY(){
        return this._vehicleY;
    }


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
