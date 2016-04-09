package org.carbondata.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.carbondata.core.util.CarbonPathUtil;

/** Filters to accept Fact Files only
 * @author Venkata Ramana G on 1/4/16.
 */
public class CarbonPathFilter implements PathFilter{

    // update extension which should be picked
    private final String updateExtension;

    /**
     * @param updateExtension update extension which should be picked
     */
    public CarbonPathFilter(String updateExtension) {
        this.updateExtension = updateExtension;
    }

    @Override public boolean accept(Path path) {
        if (path.getName().matches(CarbonPathUtil.getFactMatchExpression(updateExtension)))
            return true;
        else
            return false;
    }
}
