package org.mongeez.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class SchemaLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchemaLoader.class);
	
	private SchemaLoader(){
	}

	public static SchemaLoader create(){
		return new SchemaLoader();
	}
	
	public Schema load(){
		Schema schema = null;
		try {
				
				InputStream inputStream = getClass().getResourceAsStream("mongeez-0.9.xsd");
				
				File createTempFile = File.createTempFile("stream2file", ".tmp");
				createTempFile.deleteOnExit();
				@SuppressWarnings("resource")
				OutputStream outputStream = new FileOutputStream(createTempFile);
				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				
				schema = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI ).newSchema( createTempFile );
				
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException",e);
		} catch (IOException e) {
			LOGGER.error("IOException",e);
		} catch (SAXException e) {
			LOGGER.error("SAXException",e);
		}
		return schema;
	}
}
