package org.mongeez.commands;

import org.mongeez.dao.MongeezDao;

import java.io.IOException;
import java.util.Properties;

/**
 * CustomMongeezCommand.
 *
 * @author gpanthe
 * @since 28/04/2014
 */
public interface CustomMongeezCommand {
    void run(MongeezDao dao, Properties props) throws IOException;
}
