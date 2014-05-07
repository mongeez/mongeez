package org.mongeez.commands;

import org.mongeez.dao.MongeezDao;

import java.io.IOException;
import java.util.Map;

/**
 * Command.
 * TODO
 *
 * @author gpanthe
 * @since 28/04/2014
 */
public interface Command {
    void run(MongeezDao dao, Map<String, CustomMongeezCommand> beanFactory) throws IOException;
}
