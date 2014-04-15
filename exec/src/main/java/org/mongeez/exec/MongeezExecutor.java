
package org.mongeez.exec;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.mongeez.Mongeez;
import org.mongeez.MongoAuth;
import org.mongeez.reader.ChangeSetFileProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import com.mongodb.MongoClient;


/**
 * Mongeez command line executor.
 */
public final class MongeezExecutor {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    
    private MongeezExecutor() {
    }
    
    private void updateDb(URI uri, final String filePath, String user, 
            String pass) {
        
        log.info("Start updating " + uri);
        try {
            Mongeez mongeez = new Mongeez();
            
            String fileExt = getFileExtension(filePath);
            if ("js".equals(fileExt) || !isChangeSetFilesXml(filePath)) {
                mongeez.setChangeSetFileProvider(new ChangeSetFileProvider() {
                    @Override
                    public List<Resource> getChangeSetFiles() {
                        return Arrays.<Resource>asList(
                                new FileSystemResource(filePath));
                    }
                });
            } else {
                mongeez.setFile(new FileSystemResource(filePath));
            }
            
            mongeez.setMongo(new MongoClient(uri.getHost(), 
                    uri.getPort() != -1 ? uri.getPort() : 27017));
            mongeez.setDbName(uri.getPath().substring(1)); // trim slash
            
            if (user != null && pass != null) {
                mongeez.setAuth(new MongoAuth(user, pass));
            }
            
            mongeez.process();
            
            log.info("Update is successfull");
        
        } catch (Exception x) {
            log.error("Update failed", x);
        }
    }
    
    private Options buildOptions() {
        Options options = new Options();
        options.addOption(new Option("?", "help", false, 
                "print this message"));
        
        options.addOption(new Option("url", "url", true, 
                "target host, port and database"));
        
        options.addOption(new Option("user", "username", true, 
                "database user name"));
        
        options.addOption(new Option("pass", "password", true, 
                "database user password"));
        
        options.addOption(new Option("file", "changeLogFile", true, 
                "name of changelog file"));
        
        return options;
    }
    
    private void run(String[] args) {
        try {
            // parse the command line arguments
            Options options = buildOptions();
            CommandLine line = new GnuParser().parse(options, args);
            
            for (String arg : line.getArgs()) {
                if (!"update".equals(arg)) {
                    log.error("Unrecognized command: " + arg);
                    break;
                }
                
                String url = line.getOptionValue("url");
                if (url == null || url.isEmpty()) {
                    log.error("url is not specified");
                    return;
                }
                
                String file = line.getOptionValue("file");
                if (file == null || file.isEmpty()) {
                    log.error("changeLogFile file is not specified");
                    return;
                }
                
                updateDb(URI.create(url), file, 
                        line.getOptionValue("user"), 
                        line.getOptionValue("pass"));
                return;
            }
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("mongeez-exec [options] update", options);
            
        } catch (Exception x) {
            log.error("main", x);
        }
    }
    
    public static void main(String[] args) {
        new MongeezExecutor().run(args);
    }
    
    private static boolean isChangeSetFilesXml(String xmlFilePath) 
            throws IOException {
        
        InputStream in = null;
        try {
            in = new FileInputStream(xmlFilePath);
            Scanner s = new Scanner(in);
            return (s.findWithinHorizon("<changeFiles>", 500) != null);
            
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    
    private static String getFileExtension(String filePath) {
        int index = filePath.lastIndexOf('.');
        return filePath.substring(index + 1).toLowerCase();
    }
    
}
