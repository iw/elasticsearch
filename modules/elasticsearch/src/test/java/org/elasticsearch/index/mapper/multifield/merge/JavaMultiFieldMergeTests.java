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

package org.elasticsearch.index.mapper.multifield.merge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.DocumentMapperParser;
import org.elasticsearch.index.mapper.MapperTests;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.elasticsearch.common.io.Streams.*;
import static org.elasticsearch.index.mapper.DocumentMapper.MergeFlags.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
@Test
public class JavaMultiFieldMergeTests {

    @Test public void testMergeMultiField() throws Exception {
        String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-mapping1.json");
        DocumentMapperParser parser = MapperTests.newParser();

        DocumentMapper docMapper = parser.parse(mapping);

        assertThat(docMapper.mappers().fullName("name").mapper().indexed(), equalTo(true));
        assertThat(docMapper.mappers().fullName("name.indexed"), nullValue());

        byte[] json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-data.json");
        Document doc = docMapper.parse(json).rootDoc();
        Fieldable f = doc.getFieldable("name");
        assertThat(f, notNullValue());
        f = doc.getFieldable("name.indexed");
        assertThat(f, nullValue());


        mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-mapping2.json");
        DocumentMapper docMapper2 = parser.parse(mapping);

        DocumentMapper.MergeResult mergeResult = docMapper.merge(docMapper2, mergeFlags().simulate(true));
        assertThat(Arrays.toString(mergeResult.conflicts()), mergeResult.hasConflicts(), equalTo(false));

        docMapper.merge(docMapper2, mergeFlags().simulate(false));

        assertThat(docMapper.mappers().name("name").mapper().indexed(), equalTo(true));

        assertThat(docMapper.mappers().fullName("name").mapper().indexed(), equalTo(true));
        assertThat(docMapper.mappers().fullName("name.indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed2"), nullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed3"), nullValue());

        json = copyToBytesFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-data.json");
        doc = docMapper.parse(json).rootDoc();
        f = doc.getFieldable("name");
        assertThat(f, notNullValue());
        f = doc.getFieldable("name.indexed");
        assertThat(f, notNullValue());

        mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-mapping3.json");
        DocumentMapper docMapper3 = parser.parse(mapping);

        mergeResult = docMapper.merge(docMapper3, mergeFlags().simulate(true));
        assertThat(Arrays.toString(mergeResult.conflicts()), mergeResult.hasConflicts(), equalTo(false));

        docMapper.merge(docMapper3, mergeFlags().simulate(false));

        assertThat(docMapper.mappers().name("name").mapper().indexed(), equalTo(true));

        assertThat(docMapper.mappers().fullName("name").mapper().indexed(), equalTo(true));
        assertThat(docMapper.mappers().fullName("name.indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed2").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed3"), nullValue());


        mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/multifield/merge/test-mapping4.json");
        DocumentMapper docMapper4 = parser.parse(mapping);

        mergeResult = docMapper.merge(docMapper4, mergeFlags().simulate(true));
        assertThat(Arrays.toString(mergeResult.conflicts()), mergeResult.hasConflicts(), equalTo(false));

        docMapper.merge(docMapper4, mergeFlags().simulate(false));

        assertThat(docMapper.mappers().name("name").mapper().indexed(), equalTo(true));

        assertThat(docMapper.mappers().fullName("name").mapper().indexed(), equalTo(true));
        assertThat(docMapper.mappers().fullName("name.indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed2").mapper(), notNullValue());
        assertThat(docMapper.mappers().fullName("name.not_indexed3").mapper(), notNullValue());
    }
}
