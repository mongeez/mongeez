/*
 * Copyright 2011 SecondMarket Labs, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.mongeez;

import static org.testng.Assert.assertEquals;

import org.mongeez.validation.ValidationException;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.mongodb.Mongo;

@Test
public abstract class AbstractMongeezTest {

    private static final String DB_NAME = "test_mongeez";

    protected abstract Mongo prepareDatabase(String databaseName);
    protected abstract long collectionCount(String collection);

    private Mongo mongo;

    @BeforeMethod
    protected void setUp() throws Exception {
        mongo = prepareDatabase(DB_NAME);
    }

    private Mongeez create(String path) {
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource(path));
        mongeez.setMongo(mongo);
        mongeez.setDbName(DB_NAME);
        return mongeez;
    }

    @Test(groups = "dao")
    public void testMongeez() throws Exception {
        Mongeez mongeez = create("mongeez.xml");

        mongeez.process();

        assertEquals(collectionCount("mongeez"), 5);

        assertEquals(collectionCount("organization"), 2);
        assertEquals(collectionCount("user"), 2);
    }

    @Test(groups = "dao")
    public void testRunTwice() throws Exception {
        testMongeez();
        testMongeez();
    }

    @Test(groups = "dao")
    public void testFailOnError_False() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_fail.xml");
        mongeez.process();

        assertEquals(collectionCount("mongeez"), 2);
    }

    @Test(groups = "dao", expectedExceptions = com.mongodb.MongoCommandException.class)
    public void testFailOnError_True() throws Exception {
        Mongeez mongeez = create("mongeez_fail_fail.xml");
        mongeez.process();
    }

    @Test(groups = "dao")
    public void testNoFiles() throws Exception {
        Mongeez mongeez = create("mongeez_empty.xml");
        mongeez.process();

        assertEquals(collectionCount("mongeez"), 1);
    }

    @Test(groups = "dao")
    public void testNoFailureOnEmptyChangeLog() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_empty_changelog.xml");
        mongeez.process();

        assertEquals(collectionCount("mongeez"), 1);
    }

    @Test(groups = "dao")
    public void testNoFailureOnNoChangeFilesBlock() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_no_changefiles_declared.xml");
        mongeez.process();
        assertEquals(collectionCount("mongeez"), 1);
    }

    @Test(groups = "dao")
    public void testChangesWContextContextNotSet() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_contexts.xml");
        mongeez.process();
        assertEquals(collectionCount("mongeez"), 2);
        assertEquals(collectionCount("car"), 2);
        assertEquals(collectionCount("user"), 0);
        assertEquals(collectionCount("organization"), 0);
        assertEquals(collectionCount("house"), 0);
    }

    @Test(groups = "dao")
    public void testChangesWContextContextSetToUsers() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_contexts.xml");
        mongeez.setContext("users");
        mongeez.process();
        assertEquals(collectionCount("mongeez"), 4);
        assertEquals(collectionCount("car"), 2);
        assertEquals(collectionCount("user"), 2);
        assertEquals(collectionCount("organization"), 0);
        assertEquals(collectionCount("house"), 2);
    }

    @Test(groups = "dao")
    public void testChangesWContextContextSetToOrganizations() throws Exception {
        assertEquals(collectionCount("mongeez"), 0);

        Mongeez mongeez = create("mongeez_contexts.xml");
        mongeez.setContext("organizations");
        mongeez.process();
        assertEquals(collectionCount("mongeez"), 4);
        assertEquals(collectionCount("car"), 2);
        assertEquals(collectionCount("user"), 0);
        assertEquals(collectionCount("organization"), 2);
        assertEquals(collectionCount("house"), 2);
    }

    @Test(groups = "dao", expectedExceptions = ValidationException.class)
    public void testFailDuplicateIds() throws Exception {
        Mongeez mongeez = create("mongeez_fail_on_duplicate_changeset_ids.xml");
        mongeez.process();
    }
}
