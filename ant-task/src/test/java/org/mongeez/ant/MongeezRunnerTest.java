package org.mongeez.ant;

import static org.testng.Assert.*;

import java.io.File;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.mongeez.commands.ChangeSet;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.io.directories.IDirectory;

@Test
public class MongeezRunnerTest {
    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static int mongoPort = 0;
    
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

    /**
     * First we run an initial simple collection of changelogs... see what happens.
     * @throws UnknownHostException 
     */
    @Test
    public void runFirstMongeezSetup() throws UnknownHostException {
        Mongo mongo = new Mongo("localhost", mongoPort);
        // make sure we start clean
        mongo.dropDatabase("unittest");
        mongo.close();
        
        // configure and run the ant task
        MongeezRunner antTask = new MongeezRunner();
        antTask.setHost("localhost");
        antTask.setPort(mongoPort);
        antTask.setDbName("unittest");
        antTask.setFilePath("target/test-classes/mongeez_test1.xml");
        antTask.execute();
        
        // check the results
        // first lets check the mongeez records
        mongo = new Mongo("localhost", mongoPort);
        DB db = mongo.getDB("unittest");
        Set<String> collections = db.getCollectionNames();
        assertTrue(collections.contains("mongeez"));
        
        Queue<ChangeSet> expectedChanges = new LinkedList<ChangeSet>();
        ChangeSet change = new ChangeSet();
        change.setFile("changelog_set1.js");
        change.setChangeId("InsertSomeStuff");
        change.setAuthor("nlloyd");
        expectedChanges.add(change);
        change = new ChangeSet();
        change.setFile("changelog_set1.js");
        change.setChangeId("CreateAnIndex");
        change.setAuthor("otherguy");
        expectedChanges.add(change);
        change = new ChangeSet();
        change.setFile("changelog_set2.js");
        change.setChangeId("InsertSomeMoreStuffNewCollection");
        change.setAuthor("nlloyd");
        expectedChanges.add(change);
        DBCollection mongeezCollection = db.getCollection("mongeez");
        assertEquals(expectedChanges.size(), mongeezCollection.count(new BasicDBObject("type", "changeSetExecution")));
        DBCursor cursor = mongeezCollection.find(new BasicDBObject("type", "changeSetExecution")).sort(new BasicDBObject("$natural", 1));
        while(cursor.hasNext()) {
            DBObject entry = cursor.next();
            assertTrue(entry.containsField("file"));
            assertTrue(entry.containsField("changeId"));
            assertTrue(entry.containsField("author"));
            ChangeSet expected = expectedChanges.remove();
            assertEquals(expected.getFile(), entry.get("file"));
            assertEquals(expected.getChangeId(), entry.get("changeId"));
            assertEquals(expected.getAuthor(), entry.get("author"));
        }
        
        // now lets check the simple test collections and index
        assertTrue(collections.contains("test"));
        assertTrue(collections.contains("dates"));
        
        DBCollection testCollection = db.getCollection("test");
        assertEquals(3, testCollection.count());
        List<DBObject> indexes = testCollection.getIndexInfo();
        boolean hasIndex = false;
        for(DBObject index : indexes) {
            System.out.println(index);
            if(index.get("name").equals("_id_")) { continue; }
            if(index.get("key") instanceof DBObject) {
                assertTrue(((DBObject)index.get("key")).containsField("num"));
                assertEquals(1.0, ((DBObject)index.get("key")).get("num"));
                hasIndex = true;
            }
        }
        assertTrue(hasIndex);
        
        DBCollection datesCollection = db.getCollection("dates");
        assertEquals(50, datesCollection.count());
        cursor = datesCollection.find();
        while(cursor.hasNext()) {
            assertTrue(cursor.next().containsField("date"));
        }
    }

    /**
     * Now we see if a second collection of changelogs, same as the first but with additional changesets,
     * does what it is supposed to do.
     * @throws UnknownHostException 
     */
    @Test
    public void runSecondMongeezSetup() throws UnknownHostException {
        // configure and run the ant task
        MongeezRunner antTask = new MongeezRunner();
        antTask.setHost("localhost");
        antTask.setPort(mongoPort);
        antTask.setDbName("unittest");
        antTask.setFilePath("target/test-classes/mongeez_test2.xml");
        antTask.execute();
        
        // check the results
        // first lets check the mongeez records
        Mongo mongo = new Mongo("localhost", mongoPort);
        DB db = mongo.getDB("unittest");
        Set<String> collections = db.getCollectionNames();
        assertTrue(collections.contains("mongeez"));
        
        Queue<ChangeSet> expectedChanges = new LinkedList<ChangeSet>();
        ChangeSet change = new ChangeSet();
        change.setFile("changelog_set1.js");
        change.setChangeId("InsertSomeStuff");
        change.setAuthor("nlloyd");
        expectedChanges.add(change);
        change = new ChangeSet();
        change.setFile("changelog_set1.js");
        change.setChangeId("CreateAnIndex");
        change.setAuthor("otherguy");
        expectedChanges.add(change);
        change = new ChangeSet();
        change.setFile("changelog_set2.js");
        change.setChangeId("InsertSomeMoreStuffNewCollection");
        change.setAuthor("nlloyd");
        expectedChanges.add(change);
        // the NEW changeset
        change = new ChangeSet();
        change.setFile("changelog_set3.js");
        change.setChangeId("DropDate");
        change.setAuthor("nlloyd");
        expectedChanges.add(change);
        DBCollection mongeezCollection = db.getCollection("mongeez");
        assertEquals(expectedChanges.size(), mongeezCollection.count(new BasicDBObject("type", "changeSetExecution")));
        DBCursor cursor = mongeezCollection.find(new BasicDBObject("type", "changeSetExecution")).sort(new BasicDBObject("$natural", 1));
        while(cursor.hasNext()) {
            DBObject entry = cursor.next();
            assertTrue(entry.containsField("file"));
            assertTrue(entry.containsField("changeId"));
            assertTrue(entry.containsField("author"));
            ChangeSet expected = expectedChanges.remove();
            assertEquals(expected.getFile(), entry.get("file"));
            assertEquals(expected.getChangeId(), entry.get("changeId"));
            assertEquals(expected.getAuthor(), entry.get("author"));
        }
        
        // now lets check the simple test collections and index
        assertTrue(collections.contains("test"));
        assertTrue(collections.contains("dates"));
        
        DBCollection testCollection = db.getCollection("test");
        assertEquals(3, testCollection.count());
        List<DBObject> indexes = testCollection.getIndexInfo();
        boolean hasIndex = false;
        for(DBObject index : indexes) {
            System.out.println(index);
            if(index.get("name").equals("_id_")) { continue; }
            if(index.get("key") instanceof DBObject) {
                assertTrue(((DBObject)index.get("key")).containsField("num"));
                assertEquals(1.0, ((DBObject)index.get("key")).get("num"));
                hasIndex = true;
            }
        }
        assertTrue(hasIndex);
        
        DBCollection datesCollection = db.getCollection("dates");
        assertEquals(50, datesCollection.count());
        cursor = datesCollection.find();
        while(cursor.hasNext()) {
            // field should be removed for this test
            assertFalse(cursor.next().containsField("date"));
        }
    }

    /**
     * See if we get an exception when a unique index is created on a non-unique field.
     * @throws UnknownHostException 
     */
    @Test
    public void runBadMongeezScriptUniqueIndexCreationFailure() throws UnknownHostException {
        // configure and run the ant task
        MongeezRunner antTask = new MongeezRunner();
        antTask.setHost("localhost");
        antTask.setPort(mongoPort);
        antTask.setDbName("unittest");
        antTask.setFilePath("target/test-classes/mongeez_badtest3.xml");
        try {
            antTask.execute();
        } catch (Exception e) {
            assertTrue(e instanceof MongoException.DuplicateKey);
        }
        
        // check the results
        Mongo mongo = new Mongo("localhost", mongoPort);
        DB db = mongo.getDB("unittest");
        Set<String> collections = db.getCollectionNames();
        assertTrue(collections.contains("mongeez"));
        DBCollection mongeez = db.getCollection("mongeez");
        BasicDBObject query = new BasicDBObject();
        query.put("type", "changeSetExecution");
        query.put("file", "changelog_badset4.js");
        query.put("changeId", "breakingIndex");
        long foundEntryCount = mongeez.count(query);
        assertEquals(0, foundEntryCount);
    }

    /**
     * See if we get an exception when trying to run a background index.
     * TODO: find a way to make this possible through mongeez!
     * @throws UnknownHostException 
     */
    @Test
    public void runBadMongeezScriptBackgroundIndex() throws UnknownHostException {
        // configure and run the ant task
        MongeezRunner antTask = new MongeezRunner();
        antTask.setHost("localhost");
        antTask.setPort(mongoPort);
        antTask.setDbName("unittest");
        antTask.setFilePath("target/test-classes/mongeez_badtest4.xml");
        try {
            antTask.execute();
        } catch (Exception e) {
            assertTrue(e instanceof MongoException);
            assertEquals("can't start bg index b/c in recursive lock (db.eval?)", e.getMessage());
        }
        
        // check the results
        Mongo mongo = new Mongo("localhost", mongoPort);
        DB db = mongo.getDB("unittest");
        Set<String> collections = db.getCollectionNames();
        assertTrue(collections.contains("mongeez"));
        DBCollection mongeez = db.getCollection("mongeez");
        BasicDBObject query = new BasicDBObject();
        query.put("type", "changeSetExecution");
        query.put("file", "changelog_badset5.js");
        query.put("changeId", "breakingIndex2");
        long foundEntryCount = mongeez.count(query);
        assertEquals(0, foundEntryCount);
    }
}
