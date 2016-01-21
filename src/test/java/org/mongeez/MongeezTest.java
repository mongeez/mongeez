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

import com.mongodb.DB;
import com.mongodb.Mongo;

import org.testng.annotations.Test;

@Test
public class MongeezTest extends AbstractMongeezTest {

    private DB db;

    @Override
    protected Mongo prepareDatabase(String databaseName) {
        Mongo mongo = new Mongo();
        db = mongo.getDB(databaseName);
        db.dropDatabase();
        return mongo;
    }

    @Override
    protected long collectionCount(String collection) {
        return db.getCollection(collection).count();
    }

}
