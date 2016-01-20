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

import org.mongeez.commands.ChangeFile;
import org.mongeez.commands.ChangeFileSet;
import org.mongeez.validation.ValidationException;
import org.apache.commons.digester3.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import static java.lang.String.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesetXMLReader {

    private static final Logger logger = LoggerFactory.getLogger(FilesetXMLReader.class);

    public List<Resource> getFiles(Resource file) {
        List<Resource> files = new ArrayList<Resource>();

        try {
            Digester digester = new Digester();

            digester.setValidating(false);

            digester.addObjectCreate("changeFiles", ChangeFileSet.class);
            digester.addObjectCreate("changeFiles/file", ChangeFile.class);
            digester.addSetProperties("changeFiles/file");
            digester.addSetNext("changeFiles/file", "add");

            logger.info("Parsing XML Fileset file {}", file.getFilename());
            ChangeFileSet changeFileSet = (ChangeFileSet) digester.parse(file.getInputStream());
            if (changeFileSet != null) {
                logger.info("Num of changefiles found " + changeFileSet.getChangeFiles().size());
                for (ChangeFile changeFile : changeFileSet.getChangeFiles()) {
                    files.add(file.createRelative(changeFile.getPath()));
                }
            }
            else {
                String message = format("The file {} doesn't seem to contain a changeFiles declaration. Are you "
                        + "using the correct file to initialize Mongeez?", file.getFilename());
                throw new ValidationException(message);
            }
        } catch (IOException e) {
        	throw new ValidationException(e);
        } catch (org.xml.sax.SAXException e) {
        	throw new ValidationException(e);
        }
        return files;
    }
}
