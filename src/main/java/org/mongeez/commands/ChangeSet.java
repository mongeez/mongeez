package org.mongeez.commands;

import java.util.ArrayList;
import java.util.List;

public class ChangeSet {
    private String changeId;
    private String author;
    private String file;

    private boolean runAlways;

    private List<Script> commands = new ArrayList<Script>();

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isRunAlways() {
        return runAlways;
    }

    public void setRunAlways(boolean runAlways) {
        this.runAlways = runAlways;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void add(Script command) {
        commands.add(command);
    }

    public List<Script> getCommands() {
        return commands;
    }
}
