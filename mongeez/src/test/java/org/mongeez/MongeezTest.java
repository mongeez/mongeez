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

import java.io.File;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.io.directories.IDirectory;

import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class MongeezTest {
    private String dbName = "test_mongeez";
    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static int mongoPort = 0;
    private Mongo mongo;
    private DB db;
    
    @BeforeClass
    public static void setUpMongod() throws Exception {
        IDirectory mvnBuildPath = new IDirectory() {
            @Override
            public File asFile() {
                String currentDir = System.getProperty("user.dir");
                return new File(currentDir, "target");
            }
        };
        ITempNaming mvnMongoNamer = new ITempNaming() {
            @Override
            public String nameFor(String prefix, String postfix) {
                return prefix + postfix;
            }
        };
        RuntimeConfig runtimeConfig = new RuntimeConfig();
        runtimeConfig.setTempDirFactory(mvnBuildPath);
        runtimeConfig.setExecutableNaming(mvnMongoNamer);
        MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
        mongodExe = runtime.prepare(new MongodConfig(Version.V2_0_5));
        mongod = mongodExe.start();
        mongoPort = mongod.getConfig().getPort();
    }

    @SuppressWarnings("deprecation")
    @AfterClass
    public static void cleanUpMongod() throws Exception {
        mongod.stop();
        mongodExe.cleanup();
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        mongo = new Mongo("localhost", mongoPort);
        db = mongo.getDB(dbName);
        db.dropDatabase();
    }

    private Mongeez create(String path) {
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource(path));
        mongeez.setMongo(mongo);
        mongeez.setDbName(dbName);
        mongeez.setVerbose(true);
        return mongeez;
    }

    @Test(groups = "dao")
    public void testMongeez() throws Exception {
        Mongeez mongeez = create("mongeez.xml");

        mongeez.process();

        assertEquals(db.getCollection("mongeez").count(), 5);

        assertEquals(db.getCollection("organization").count(), 2);
        assertEquals(db.getCollection("user").count(), 2);
    }

    @Test(groups = "dao")
    public void testRunTwice() throws Exception {
        testMongeez();
        testMongeez();
    }

    @Test(groups = "dao")
    public void testNoFiles() throws Exception {
        Mongeez mongeez = create("mongeez_empty.xml");
        mongeez.process();

        assertEquals(db.getCollection("mongeez").count(), 1);
    }
}
