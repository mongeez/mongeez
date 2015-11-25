package org.mongeez.dao;

import com.mongodb.DB;

class JavaScriptExecutor implements ScriptExecutor {

	@Override
	public void runScript(DB db, String code) {
		db.eval(code);
	}

}
