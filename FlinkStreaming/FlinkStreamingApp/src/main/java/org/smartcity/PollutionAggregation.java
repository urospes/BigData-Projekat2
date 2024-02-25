package org.smartcity;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.smartcity.dtos.FullPollutionOutputRecord;
import org.smartcity.dtos.PollutionAccumulator;
import org.smartcity.dtos.PollutionOutputRecord;
import org.smartcity.dtos.TransformedRecord;

import java.text.Format;
import java.text.SimpleDateFormat;

public class PollutionAggregation {
    public static class PollutionAggregator implements AggregateFunction<TransformedRecord, PollutionAccumulator, PollutionOutputRecord> {
        @Override
        public PollutionAccumulator createAccumulator() {
            return new PollutionAccumulator();
        }

        @Override
        public PollutionAccumulator add(TransformedRecord record, PollutionAccumulator acc) {
            return new PollutionAccumulator(
                    record.get_vehicleCO() + acc.sum_vehicleCO,
                    record.get_vehicleCO2() + acc.sum_vehicleCO2,
                    record.get_vehicleHC() + acc.sum_vehicleHC,
                    record.get_vehiclePMx() + acc.sum_vehiclePMx,
                    record.get_vehicleNOx() + acc.sum_vehicleNOx,
                    record.get_vehicleFuel() + acc.sum_vehicleFuel,
                    acc.numObservations + 1
            );
        }

        @Override
        public PollutionOutputRecord getResult(PollutionAccumulator acc) {
            PollutionOutputRecord output = new PollutionOutputRecord();

            output.set_sumCO(acc.sum_vehicleCO); // / acc.numObservations);
            output.set_sumCO2(acc.sum_vehicleCO2); // / acc.numObservations);
            output.set_sumHC(acc.sum_vehicleHC); // / acc.numObservations);
            output.set_avgPMx(acc.sum_vehiclePMx); // / acc.numObservations);
            output.set_avgNOx(acc.sum_vehicleNOx); // / acc.numObservations);
            output.set_avgFuel(acc.sum_vehicleFuel); // / acc.numObservations);

            return output;
        }

        @Override
        public PollutionAccumulator merge(PollutionAccumulator acc1, PollutionAccumulator acc2) {
            // ovo ne mora, ali neka ga...
            return new PollutionAccumulator(
                    acc1.sum_vehicleCO + acc2.sum_vehicleCO,
                    acc1.sum_vehicleCO2 + acc2.sum_vehicleCO2,
                    acc1.sum_vehicleHC + acc2.sum_vehicleHC,
                    acc1.sum_vehiclePMx + acc2.sum_vehiclePMx,
                    acc1.sum_vehicleNOx + acc2.sum_vehicleNOx,
                    acc1.sum_vehicleFuel + acc2.sum_vehicleFuel,
                    acc1.numObservations + acc2.numObservations
            );
        }
    }

    public static class ContextExtractorFunction extends ProcessWindowFunction<PollutionOutputRecord, FullPollutionOutputRecord, String, TimeWindow> {

        @Override
        public void process(String key, ProcessWindowFunction<PollutionOutputRecord, FullPollutionOutputRecord, String, TimeWindow>.Context context, Iterable<PollutionOutputRecord> averages, Collector<FullPollutionOutputRecord> out) throws Exception {
            PollutionOutputRecord pollutionRecord = averages.iterator().next();

            Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            String windowStart =  format.format(context.window().getStart());
            String windowEnd = format.format(context.window().getEnd());

            FullPollutionOutputRecord fullRecord = new FullPollutionOutputRecord();
            fullRecord.set_key(key);
            fullRecord.set_windowStart(windowStart);
            fullRecord.set_windowEnd(windowEnd);
            fullRecord.setPollutionRecord(pollutionRecord);

            out.collect(fullRecord);
        }
    }
}
