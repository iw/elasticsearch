/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
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

package org.elasticsearch.search.query;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.search.SearchShardTarget;

import java.io.IOException;

import static org.elasticsearch.search.SearchShardTarget.*;
import static org.elasticsearch.search.query.QuerySearchResult.*;

/**
 * @author kimchy (shay.banon)
 */
public class ScrollQuerySearchResult implements Streamable {

    private QuerySearchResult queryResult;

    private SearchShardTarget shardTarget;

    public ScrollQuerySearchResult() {
    }

    public ScrollQuerySearchResult(QuerySearchResult queryResult, SearchShardTarget shardTarget) {
        this.queryResult = queryResult;
        this.shardTarget = shardTarget;
    }

    public QuerySearchResult queryResult() {
        return queryResult;
    }

    public SearchShardTarget shardTarget() {
        return shardTarget;
    }

    @Override public void readFrom(StreamInput in) throws IOException {
        shardTarget = readSearchShardTarget(in);
        queryResult = readQuerySearchResult(in);
        queryResult.shardTarget(shardTarget);
    }

    @Override public void writeTo(StreamOutput out) throws IOException {
        shardTarget.writeTo(out);
        queryResult.writeTo(out);
    }
}