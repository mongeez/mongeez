package org.mongeez.commands;

import java.util.ArrayList;
import java.util.List;

public class ChangeFileSet {
    private List<ChangeFile> changeFiles = new ArrayList<ChangeFile>();

    public void add(ChangeFile changeFile) {
        this.changeFiles.add(changeFile);
    }

    public List<ChangeFile> getChangeFiles() {
        return changeFiles;
    }

    public void setChangeFiles(List<ChangeFile> changeFile) {
        this.changeFiles = changeFile;
    }
}
