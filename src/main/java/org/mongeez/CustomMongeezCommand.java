package org.mongeez;

import org.mongeez.dao.MongeezDao;

import java.util.Properties;

/**
 * CustomMongeezCommand.
 *
 * @author gpanthe
 * @since 28/04/2014
 */
public interface CustomMongeezCommand {
    void run(MongeezDao dao, Properties props);
}
