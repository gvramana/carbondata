package org.carbondata.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.security.TokenCache;
import org.carbondata.core.util.CarbonPathUtil;
import org.carbondata.core.util.TableIdentifier;

/**
 * @author Venkata Ramana G on 1/4/16.
 */
public class CarbonInputFormat<T> extends FileInputFormat<Void, T> {

    private static final Log LOG = LogFactory.getLog(CarbonInputFormat.class);

    private static final String DATABASE_NAME = "mapreduce.input.carboninputformat.databasename";
    private static final String TABLE_NAME = "mapreduce.input.carboninputformat.tablename";

    //private static final Log LOG = Log.getLog(CarbonInputFormat.class);

    public static void setTableToAccess(Job job, TableIdentifier tableIdentifier) {
        job.getConfiguration().set(CarbonInputFormat.DATABASE_NAME, tableIdentifier.dataBaseName);
        job.getConfiguration().set(CarbonInputFormat.TABLE_NAME, tableIdentifier.tableName);
    }

    @Override protected long getFormatMinSplitSize() {
        return super.getFormatMinSplitSize();
    }

    /**
     * {@inheritDoc}
     * Configurations FileInputFormat.INPUT_DIR
     * are used to get table path to read.
     *
     * @param job
     * @return
     * @throws IOException
     */
    @Override public List<InputSplit> getSplits(JobContext job) throws IOException {
        return super.getSplits(job);
    }

    @Override public RecordReader<Void, T> createRecordReader(InputSplit inputSplit,
            TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        //TODO: implement
        return null;
    }

    @Override protected long computeSplitSize(long blockSize, long minSize, long maxSize) {
        return super.computeSplitSize(blockSize, minSize, maxSize);
    }

    @Override protected int getBlockIndex(BlockLocation[] blkLocations, long offset) {
        return super.getBlockIndex(blkLocations, offset);
    }

    @Override protected List<FileStatus> listStatus(JobContext job) throws IOException {
        List<FileStatus> result = new ArrayList<FileStatus>();
        String[] segmentsToConsider = getValidSegments(job);
        if (segmentsToConsider.length == 0) {
            throw new IOException("No segments found");
        }

        PathFilter inputFilter = getFactFileFilter();

        // get tokens for all the required FileSystem for table path
        TokenCache.obtainTokensForNamenodes(job.getCredentials(), new Path[] { getTablePath(job) },
                job.getConfiguration());

        //identify fact file status in each segment
        for (int i = 0; i < segmentsToConsider.length; ++i) {
            String segment = segmentsToConsider[i];
            Path segmentPath = CarbonPathUtil.getSegmentPath(getTablePath(job), segment);
            FileSystem fs = segmentPath.getFileSystem(job.getConfiguration());

            RemoteIterator<LocatedFileStatus> iter = fs.listLocatedStatus(segmentPath);
            while (iter.hasNext()) {
                LocatedFileStatus stat = iter.next();
                if (inputFilter.accept(stat.getPath())) {
                    if (stat.isDirectory()) {
                        addInputPathRecursively(result, fs, stat.getPath(), inputFilter);
                    } else {
                        result.add(stat);
                    }
                }
            }
        }
        return result;
    }

    public Path getTablePath(JobContext job) throws IOException {

        Path[] inputPaths = getInputPaths(job);
        if (inputPaths.length == 0) throw new IOException("No input paths specified in job");

        return inputPaths[0];
    }

    /**
     * @return the PathFilter for Fact Files.
     */
    public PathFilter getFactFileFilter() {
        return new CarbonPathFilter(getUpdateExtension());
    }

    /**
     * required to be moved to core
     * @return updateExtension
     */
    private String getUpdateExtension() {
        // TODO: required to modify when supporting update, mostly will be update timestamp
        return "update";
    }

    /**
     * required to be moved to core
     * @return updateExtension
     */
    private String[] getValidSegments(JobContext job) {
        //TODO: required to get valid segments from loadmetadata
        return new String[0];
    }

}
