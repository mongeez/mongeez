package org.mongeez;

import static org.testng.Assert.assertEquals;

import com.mongodb.DB;
import com.mongodb.Mongo;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class MongeezTest {
    private String dbName = "test_mongeez";
    private Mongo mongo;
    private DB db;

    @BeforeMethod
    protected void setUp() throws Exception {
        mongo = new Mongo();
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

        assertEquals(db.getCollection("mongeez").count(), 4);

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

        assertEquals(db.getCollection("mongeez").count(), 0);
    }
}
