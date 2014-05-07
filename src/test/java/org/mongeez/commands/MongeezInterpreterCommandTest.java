package org.mongeez.commands;

import com.mongodb.DBObject;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mongeez.dao.MongeezDao;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MongeezInterpreterCommandTest {

    @Mock
    private MongeezDao mongeezDao;

    @Test
    public void testRun() throws Exception {
        MongeezInterpreterCommand cmd = new MongeezInterpreterCommand();

        final Properties props = new Properties();
        props.load(getClass().getResourceAsStream("interpreter-test.properties"));
        cmd.run(mongeezDao, props);

        final ArgumentCaptor<DBObject> inserted = ArgumentCaptor.forClass(DBObject.class);
        verify(mongeezDao, times(2)).insertCollection(eq("organization"), inserted.capture());
        assertEquals(2, inserted.getAllValues().size());

        final ArgumentCaptor<DBObject> query = ArgumentCaptor.forClass(DBObject.class);
        final ArgumentCaptor<DBObject> update = ArgumentCaptor.forClass(DBObject.class);
        verify(mongeezDao).updateCollection(eq("organization"), query.capture(), update.capture(), eq(true), eq(false));

        assertEquals("Hi10", query.getValue().get("Organization"));
    }
}