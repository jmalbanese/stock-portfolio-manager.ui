package com.proserus.stocks.ui.dbutils;

import org.apache.log4j.Logger;

import com.proserus.stocks.bp.dao.PersistenceManager;


public class ExecuteScriptStrategy extends AbstractScriptDatabaseStrategy {
	private static Logger log = Logger.getLogger(ExecuteScriptStrategy.class.getName());
	private String script;

	public ExecuteScriptStrategy(String script){
		super(script);
		this.script = script;
	}
	
	@Override
    public void applyUpgrade(PersistenceManager pm) {
		log.debug("will rollback?: " + pm.getTransaction().getRollbackOnly());
		log.debug("Executing " + script);
		
		DbUtils.executeScript(pm,  getScript());
		log.debug("will rollback?: " + pm.getTransaction().getRollbackOnly());
    }
}

