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

package org.mongeez.dao;

import org.mongeez.commands.ChangeSet;

public enum ChangeSetAttribute {
    file() {
        public String getAttributeValue(ChangeSet changeSet) {
            return changeSet.getFile();
        }
    }, changeId() {
        public String getAttributeValue(ChangeSet changeSet) {
            return changeSet.getChangeId();
        }
    }, author() {
        public String getAttributeValue(ChangeSet changeSet) {
            return changeSet.getAuthor();
        }
    }, resourcePath() {
        public String getAttributeValue(ChangeSet changeSet) {
            return changeSet.getResourcePath();
        }
    };

    abstract public String getAttributeValue(ChangeSet changeSet);
}
