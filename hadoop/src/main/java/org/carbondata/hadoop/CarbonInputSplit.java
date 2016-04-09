package org.carbondata.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * @author Venkata Ramana G on 1/4/16.
 */
public class CarbonInputSplit extends FileSplit implements Writable {

   public static InputSplit from(FileSplit split) {
        return split;
    }
}
