package org.example.models;

import java.util.Objects;

public class Measurements {

    private int measurementId;
    private String measurementName;

    public Measurements() {
    }

    public Measurements(int measurementId, String measurementName) {
        this.measurementId = measurementId;
        this.measurementName = measurementName;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurements that = (Measurements) o;
        return measurementId == that.measurementId && Objects.equals(measurementName, that.measurementName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementId, measurementName);
    }
}
