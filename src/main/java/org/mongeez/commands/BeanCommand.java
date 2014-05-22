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
package org.mongeez.commands;

import org.mongeez.dao.MongeezDao;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

/**
 * @author greyfairer
 * @since 28/04/2014
 */
public class BeanCommand implements Command {
    private String ref;
    private String props;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    @Override
    public void run(MongeezDao dao, Map<String, CustomMongeezCommand> customCommands) throws IOException {
        if (customCommands == null || !customCommands.containsKey(ref)) {
            throw new IllegalArgumentException("Could not find CustomMongeezCommand with ref: " + ref);
        }
        final Properties props = new Properties();
        props.load(new StringReader(this.props));
        customCommands.get(ref).run(dao, props);
    }

    @Override
    public String toString() {
        return String.format("bean ref=%s", ref);
    }
}
