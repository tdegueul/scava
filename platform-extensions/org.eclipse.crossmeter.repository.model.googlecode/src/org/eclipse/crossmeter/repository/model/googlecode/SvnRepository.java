/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Davide Di Ruscio - Implementation,
 *    Juri Di Rocco - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.repository.model.googlecode;


public class SvnRepository extends org.eclipse.crossmeter.repository.model.VcsRepository {
	
	
	
	public SvnRepository() { 
		super();
	}
	
	public String getBrowse() {
		return parseString(dbObject.get("browse")+"", "");
	}
	
	public SvnRepository setBrowse(String browse) {
		dbObject.put("browse", browse + "");
		notifyChanged();
		return this;
	}
	
	
	
	
}