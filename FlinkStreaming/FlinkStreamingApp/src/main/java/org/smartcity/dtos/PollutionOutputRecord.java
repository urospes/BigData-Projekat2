package org.smartcity.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PollutionOutputRecord {
    float _sumCO;
    float _sumCO2;
    float _sumHC;
    float _sumPMx;
    float _sumNOx;
    float _sumFuel;

    @JsonGetter("sum(vehicle_CO)")
    public float get_sumCO() {
        return _sumCO;
    }

    @JsonSetter("sum(vehicle_CO)")
    public void set_sumCO(float _sumCO) {
        this._sumCO = _sumCO;
    }

    @JsonGetter("sum(vehicle_CO2)")
    public float get_sumCO2() {
        return _sumCO2;
    }

    @JsonSetter("sum(vehicle_CO2)")
    public void set_sumCO2(float _sumCO2) {
        this._sumCO2 = _sumCO2;
    }

    @JsonGetter("sum(vehicle_HC)")
    public float get_sumHC() {
        return _sumHC;
    }

    @JsonSetter("sum(vehicle_HC)")
    public void set_sumHC(float _sumHC) {
        this._sumHC = _sumHC;
    }

    @JsonGetter("sum(vehicle_PMx)")
    public float get_avgPMx() {
        return _sumPMx;
    }

    @JsonSetter("sum(vehicle_PMx)")
    public void set_avgPMx(float _avgPMx) {
        this._sumPMx = _avgPMx;
    }

    @JsonGetter("sum(vehicle_NOx)")
    public float get_avgNOx() {
        return _sumNOx;
    }

    @JsonSetter("sum(vehicle_NOx)")
    public void set_avgNOx(float _avgNOx) {
        this._sumNOx = _avgNOx;
    }

    @JsonGetter("sum(vehicle_fuel)")
    public float get_avgFuel() {
        return _sumFuel;
    }

    @JsonSetter("sum(vehicle_fuel)")
    public void set_avgFuel(float _avgFuel) {
        this._sumFuel = _avgFuel;
    }

    public PollutionOutputRecord() {}

}
