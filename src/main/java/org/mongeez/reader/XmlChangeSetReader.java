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

package org.mongeez.reader;

import org.apache.commons.digester3.Digester;
import org.mongeez.MongeezException;
import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.ChangeSetList;
import org.mongeez.commands.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

public class XmlChangeSetReader implements ChangeSetReader {
    private static final Logger logger = LoggerFactory.getLogger(XmlChangeSetReader.class);

    private Digester digester;

    XmlChangeSetReader() {
        digester = new Digester();

        digester.setValidating(false);

        digester.addObjectCreate("mongoChangeLog", ChangeSetList.class);
        digester.addObjectCreate("mongoChangeLog/changeSet", ChangeSet.class);
        digester.addSetProperties("mongoChangeLog/changeSet");
        digester.addSetNext("mongoChangeLog/changeSet", "add");

        digester.addObjectCreate("mongoChangeLog/changeSet/script", Script.class);
        digester.addBeanPropertySetter("mongoChangeLog/changeSet/script", "body");
        digester.addSetNext("mongoChangeLog/changeSet/script", "add");
    }

    @Override
    public boolean supports(Resource file) {
        return true;
    }

    @Override
    public List<ChangeSet> getChangeSets(Resource file) {
        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        try {
            logger.info("Parsing XML Change Set File {}", file.getFilename());
            ChangeSetList changeFileSet = (ChangeSetList) digester.parse(file.getInputStream());
            if (changeFileSet == null) {
                throw new MongeezException("Change file \"" + file.getFilename() + "\" could not be parsed. changeFileSet was null. Please check your formatting.");
            }
            else {
                for (ChangeSet changeSet : changeFileSet.getList()) {
                    ChangeSetReaderUtil.populateChangeSetResourceInfo(changeSet, file);
                }
                changeSets.addAll(changeFileSet.getList());
            }
        } catch (Exception e) {
            throw new MongeezException(e);
        }
        return changeSets;
    }
}
