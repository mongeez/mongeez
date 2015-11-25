package org.mongeez.reader;

import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.mongeez.commands.Script;
import org.xml.sax.Attributes;

class ScriptCreationFactory extends AbstractObjectCreationFactory<Script> {

	@Override
	public Script createObject(Attributes attributes) throws Exception {
		Script script = new Script();
		script.setLanguage(attributes.getValue("language"));
		return script;
	}

}
