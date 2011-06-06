package org.mongeez.reader;

import org.apache.commons.digester.Digester;
import org.mongeez.commands.ChangeSet;
import org.mongeez.commands.ChangeSetList;
import org.mongeez.commands.Script;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeSetReader {
    public List<ChangeSet> getChangeSets(List<Resource> files) {
        List<ChangeSet> changeSets = new ArrayList<ChangeSet>();

        try {
            Digester digester = new Digester();

            digester.setValidating(false);

            digester.addObjectCreate("mongoChangeLog", ChangeSetList.class);
            digester.addObjectCreate("mongoChangeLog/changeSet", ChangeSet.class);
            digester.addSetProperties("mongoChangeLog/changeSet");
            digester.addSetNext("mongoChangeLog/changeSet", "add");

            digester.addObjectCreate("mongoChangeLog/changeSet/script", Script.class);
            digester.addBeanPropertySetter("mongoChangeLog/changeSet/script", "body");
            digester.addSetNext("mongoChangeLog/changeSet/script", "add");

            for (Resource file : files) {
                ChangeSetList changeFileSet = (ChangeSetList) digester.parse(file.getInputStream());
                for (ChangeSet changeSet : changeFileSet.getList()) {
                    changeSet.setFile(file.getFilename());
                }
                changeSets.addAll(changeFileSet.getList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }

        return changeSets;
    }
}
