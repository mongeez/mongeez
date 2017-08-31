package org.mongeez.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

enum ScriptType {

	/**
	 * JavaScript based on MongoDB's DB.eval.
	 */
	JAVASCRIPT("javascript", new JavaScriptExecutor()),

	/**
	 * BeanShell.
	 */
	BSH("bsh", new BshScriptExecutor());

	private static final Map<String, ScriptType> STRING_TO_ENUM;
	static {
		Map<String, ScriptType> stringToEnum = new HashMap<String, ScriptType>();
		for (ScriptType op : values()) {
			stringToEnum.put(op.toString(), op);
		}
		STRING_TO_ENUM = Collections.unmodifiableMap(stringToEnum);
	}

	// Returns Operation for string, or null if string is invalid
	static ScriptType fromString(String language) {
		ScriptType result = STRING_TO_ENUM.get(language);
		return result;
	}

	private final String language;
	private final ScriptExecutor scriptExecutor;

	private ScriptType(String language, ScriptExecutor scriptExecutor) {
		this.language = language;
		this.scriptExecutor = scriptExecutor;
	}

	ScriptExecutor scriptExecutor() {
		return scriptExecutor;
	}

	@Override
	public String toString() {
		return language;
	}

}
