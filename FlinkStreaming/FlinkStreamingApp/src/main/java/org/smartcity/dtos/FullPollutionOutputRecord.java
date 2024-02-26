package org.smartcity.dtos;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class FullPollutionOutputRecord {
    String _key;
    String _windowStart;
    String _windowEnd;

    @JsonUnwrapped
    PollutionOutputRecord pollutionRecord;

    String _aggType = "pollution_agg";

    @JsonGetter("agg_type")
    public String get_aggType() {
        return _aggType;
    }

    @JsonSetter("agg_type")
    public void set_aggType(String _aggType) {
        this._aggType = _aggType;
    }

    @JsonGetter("zone")
    public String get_key() {
        return _key;
    }

    @JsonSetter("zone")
    public void set_key(String _key) {
        this._key = _key;
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

    @JsonGetter("pollution")
    public PollutionOutputRecord getPollutionRecord() {
        return pollutionRecord;
    }

    @JsonSetter("pollution")
    public void setPollutionRecord(PollutionOutputRecord pollutionRecord) {
        this.pollutionRecord = pollutionRecord;
    }

    public FullPollutionOutputRecord() {}
}
