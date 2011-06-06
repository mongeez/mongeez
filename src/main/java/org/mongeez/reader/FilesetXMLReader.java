package org.mongeez.reader;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.mongeez.commands.ChangeFile;
import org.mongeez.commands.ChangeFileSet;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesetXMLReader {

    private static final Logger logger = Logger.getLogger(FilesetXMLReader.class);

    public List<Resource> getFiles(Resource file) {
        List<Resource> files = new ArrayList<Resource>();

        try {
            Digester digester = new Digester();

            digester.setValidating(false);

            digester.addObjectCreate("changeFiles", ChangeFileSet.class);
            digester.addObjectCreate("changeFiles/file", ChangeFile.class);
            digester.addSetProperties("changeFiles/file");
            digester.addSetNext("changeFiles/file", "add");

            ChangeFileSet changeFileSet = (ChangeFileSet) digester.parse(file.getInputStream());
            logger.info("Num of changefiles " + changeFileSet.getChangeFiles().size());

            for (ChangeFile changeFile : changeFileSet.getChangeFiles()) {
                files.add(file.createRelative(changeFile.getPath()));
            }
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (org.xml.sax.SAXException e) {
            logger.error("SAXException", e);
        }
        return files;
    }
}
