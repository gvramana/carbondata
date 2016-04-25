package org.carbondata.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * @author Venkata Ramana G on 1/4/16.
 */
public class CarbonRawDataInputSplit extends InputSplit implements Writable {

    long length;
    String[] locations;

    public CarbonRawDataInputSplit(long length, String[] locations) {
        this.length = length;
        this.locations = locations;
    }

    public static CarbonRawDataInputSplit from(FileSplit split) throws IOException {
        return new CarbonRawDataInputSplit(split.getLength(), split.getLocations());
    }

    @Override public long getLength() throws IOException, InterruptedException {
        return length;
    }

    @Override public String[] getLocations() throws IOException, InterruptedException {
        return locations;
    }

    @Override public void write(DataOutput out) throws IOException {
        out.writeLong(length);
        out.writeLong(locations.length);
    }

    @Override public void readFields(DataInput in) throws IOException {

    }
}
