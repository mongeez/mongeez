package org.mongeez.commands;

import java.util.ArrayList;
import java.util.List;

public class ChangeSetList {
    List<ChangeSet> list = null;

    public List<ChangeSet> getList() {
        return list;
    }

    public void setList(List<ChangeSet> list) {
        this.list = list;
    }

    public void add(ChangeSet changeSet) {
        if (list == null) {
            list = new ArrayList<ChangeSet>();
        }
        this.list.add(changeSet);
    }
}
