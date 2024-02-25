package org.smartcity.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class FullLaneOutputRecord {
    String _windowStart;
    String _windowEnd;
    String _lane;
    int _vehicleCount;

    String _aggType = "lane_agg";

    @JsonGetter("agg_type")
    public String get_aggType() {
        return _aggType;
    }

    @JsonSetter("agg_type")
    public void set_aggType(String _aggType) {
        this._aggType = _aggType;
    }

    @JsonGetter("window_start")
    public String get_windowStart() {
        return _windowStart;
    }
    @JsonSetter("window_start")
    public void set_windowStart(String _windowStart) {
        this._windowStart = _windowStart;
    }

    @JsonGetter("window_end")
    public String get_windowEnd() {
        return _windowEnd;
    }
    @JsonSetter("window_end")
    public void set_windowEnd(String _windowEnd) {
        this._windowEnd = _windowEnd;
    }

    @JsonGetter("vehicle_lane")
    public String get_lane() {
        return _lane;
    }
    @JsonSetter("vehicle_lane")
    public void set_lane(String _lane) {
        this._lane = _lane;
    }

    @JsonGetter("vehicle_count")
    public int get_vehicleCount() {
        return _vehicleCount;
    }

    @JsonSetter("vehicle_count")
    public void set_vehicleCount(int _vehicleCount) {
        this._vehicleCount = _vehicleCount;
    }

    public FullLaneOutputRecord() {}
}
