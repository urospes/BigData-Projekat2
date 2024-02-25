package org.smartcity.dtos;

public class PollutionAccumulator {
    public float sum_vehicleCO;

    public float sum_vehicleCO2;

    public float sum_vehicleHC;

    public float sum_vehiclePMx;

    public float sum_vehicleNOx;

    public float sum_vehicleFuel;

    public int numObservations;

    public PollutionAccumulator()
    {
        this.sum_vehicleCO = 0.0f;
        this.sum_vehicleCO2 = 0.0f;
        this.sum_vehicleHC = 0.0f;
        this.sum_vehiclePMx = 0.0f;
        this.sum_vehicleNOx = 0.0f;
        this.sum_vehicleFuel = 0.0f;
        this.numObservations = 0;
    }

    public PollutionAccumulator(float co, float co2, float hc, float pmx, float nox, float fuel, int num)
    {
        this.sum_vehicleCO = co;
        this.sum_vehicleCO2 = co2;
        this.sum_vehicleHC = hc;
        this.sum_vehiclePMx = pmx;
        this.sum_vehicleNOx = nox;
        this.sum_vehicleFuel = fuel;
        this.numObservations = num;
    }
}
