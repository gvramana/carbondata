package org.carbondata.core.util;

import java.io.File;

import org.apache.hadoop.fs.Path;

/** All carbon path and directory structure utils
 * @author Venkata Ramana G on 1/4/16.
 */

public class CarbonPathUtil {

    public static Path getSegmentPath(Path tablePath, String segment) {
        return new Path(tablePath+ File.separator+segment);
    }

    public static String getFactMatchExpression(String updateExtension) {
        return "(.*)(.fact)(.*)(."+updateExtension+")";
    }
}
