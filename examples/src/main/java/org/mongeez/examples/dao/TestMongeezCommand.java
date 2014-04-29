package org.mongeez.examples.dao;

import org.mongeez.CustomMongeezCommand;
import org.mongeez.dao.MongeezDao;

import java.util.Properties;

/**
 * TestMongeezCommand.
 * TODO
 *
 * @author gpanthe
 * @since 28/04/2014
 */
public class TestMongeezCommand implements CustomMongeezCommand {
    @Override
    public void run(MongeezDao dao, Properties props) {

        dao.runScript("db.testM.insert({test:true});");
    }
}
