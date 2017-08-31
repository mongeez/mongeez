package org.mongeez.dao;

import com.mongodb.DB;

interface ScriptExecutor {
	void runScript(DB db, String code);
}
