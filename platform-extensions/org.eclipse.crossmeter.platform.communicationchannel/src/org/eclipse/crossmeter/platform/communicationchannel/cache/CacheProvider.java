/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Yannis Korkontzelos - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.platform.communicationchannel.cache;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.crossmeter.repository.model.CommunicationChannel;

public abstract class CacheProvider<T, K> {

	public abstract boolean changedOnDate(T item, Date date,
			CommunicationChannel communicationChannel);

	public abstract boolean changedSinceDate(T item, Date date,
			CommunicationChannel communicationChannel);

	public abstract K getKey(T item);

	public abstract void process(T item, CommunicationChannel communicationChannel);

	/**
	 * returns true if one of dates matches date.
	 * 
	 * @param date
	 * @param dates
	 * @return
	 */
	public static boolean findMatchOnDate(Date date, Date... dates) {
		for (Date d : dates) {
			if (null != d && DateUtils.isSameDay(date, d)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * returns true if one of dates occurred after date.
	 * 
	 * @param date
	 * @param dates
	 * @return
	 */
	public static boolean findMatchSinceDate(Date date, Date... dates) {
		for (Date d : dates) {
			if (null != d && d.after(date)) {
				return true;
			}
		}

		return false;
	}
}