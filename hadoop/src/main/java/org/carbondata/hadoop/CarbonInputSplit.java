/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.carbondata.hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * Carbon input split to allow distributed read of CarbonInputFormat.
 */
public class CarbonInputSplit extends FileSplit implements Writable {

  private int segmentId;

  public CarbonInputSplit(int segmentId, Path path, long start, long length, String[] locations) {
    super(path, start, length, locations);
    this.segmentId = segmentId;
  }

  public static CarbonInputSplit from(int segmentId, FileSplit split) throws IOException {
    return new CarbonInputSplit(segmentId, split.getPath(), split.getStart(), split.getLength(),
        split.getLocations());
  }

  public int getSegmentId() {
    return segmentId;
  }
}
