package org.mongeez.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.EvalError;
import bsh.Interpreter;

import com.mongodb.DB;

class BshScriptExecutor implements ScriptExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BshScriptExecutor.class);

	private Interpreter i = new Interpreter();

	@Override
	public void runScript(DB db, String code) {
		try {
			i.getNameSpace().importPackage("com.mongodb");
			i.getNameSpace().importPackage("com.mongodb.util");
			i.set("db", db);
			i.eval(code);
		} catch (EvalError e) {
			LOGGER.error("Beanshell script eval error", e);
		}
	}

}
