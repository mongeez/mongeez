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

import java.util.ArrayList;
import java.util.List;

public class ChangeSet {
    private String changeId;
    private String author;
    private String file;
    private String resourcePath;
    private String contextsStr;
    private ArrayList<String> contexts;

    private boolean failOnError = true;
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

    public boolean isFailOnError() {
        return failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void add(Script command) {
        commands.add(command);
    }

    public List<Script> getCommands() {
        return commands;
    }
    
    public String getContexts()
    {
        if (contextsStr == null)
        {
            contextsStr = "";
        }

        return contextsStr;
    }

    public void setContexts(String contextsStr) {
        this.contextsStr = contextsStr;
        contexts = null;
    }

    public boolean canBeAppliedInContext(String context) {
        if (contextsStr == null) {
            return true;
        }

        if (contexts == null) {
            contexts = new ArrayList<String>();
            for (String requiredContext : contextsStr.split(",")) {
                String cleanedContext = requiredContext.toLowerCase().trim();
                if (cleanedContext.length() > 0) {
                    contexts.add(cleanedContext);
                }
            }
        }
        return contexts.isEmpty() || (context != null && contexts.contains(context.toLowerCase().trim()));
    }
}
