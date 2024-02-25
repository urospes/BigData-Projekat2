package org.smartcity;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.smartcity.dtos.FullLaneOutputRecord;
import org.smartcity.dtos.FullPollutionOutputRecord;
import org.smartcity.dtos.MessageDTO;

import java.text.Format;
import java.text.SimpleDateFormat;

public class LaneAggregation {
    public static class CountPerLane implements AggregateFunction<MessageDTO, Integer, Integer> {

        @Override
        public Integer createAccumulator() {
            return 0;
        }

        @Override
        public Integer add(MessageDTO rec, Integer acc) {
            return acc + 1;
        }

        @Override
        public Integer getResult(Integer acc) {
            return acc;
        }

        @Override
        public Integer merge(Integer acc1, Integer acc2) {
            return acc1 + acc2;
        }
    }

    public static class ContextExtractorFunction extends ProcessWindowFunction<Integer, FullLaneOutputRecord, String, TimeWindow> {
        @Override
        public void process(String key, ProcessWindowFunction<Integer, FullLaneOutputRecord, String, TimeWindow>.Context context, Iterable<Integer> counts, Collector<FullLaneOutputRecord> out) throws Exception {
            Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            String windowStart =  format.format(context.window().getStart());
            String windowEnd = format.format(context.window().getEnd());
            int vehicleCount = counts.iterator().next();

            FullLaneOutputRecord record = new FullLaneOutputRecord();
            record.set_windowStart(windowStart);
            record.set_windowEnd(windowEnd);
            record.set_lane(key);
            record.set_vehicleCount(vehicleCount);

            out.collect(record);
        }
    }
}
