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

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.testng.annotations.Test;

@Test
public class MongeezMongoClientTest extends AbstractMongeezTest {

    private MongoDatabase mongoDatabase;

    @Override
    protected Mongo prepareDatabase(String databaseName) {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase(databaseName);
        mongoDatabase.drop();
        return mongoClient;
    }

    @Override
    protected long collectionCount(String collection) {
        return mongoDatabase.getCollection(collection).count();
    }

}
