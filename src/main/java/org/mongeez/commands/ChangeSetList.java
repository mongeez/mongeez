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

public class ChangeSetList {
    List<ChangeSet> list = null;

    public List<ChangeSet> getList() {
        if (list == null) {
            list = new ArrayList<ChangeSet>();
        }
        return list;
    }

    public void setList(List<ChangeSet> list) {
        this.list = list;
    }

    public void add(ChangeSet changeSet) {
        getList().add(changeSet);
    }
}
