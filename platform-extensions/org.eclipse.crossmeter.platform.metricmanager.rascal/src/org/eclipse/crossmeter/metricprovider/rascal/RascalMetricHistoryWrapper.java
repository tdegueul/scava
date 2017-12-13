/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jurgen Vinju - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.metricprovider.rascal;

import java.util.Arrays;
import java.util.List;

import org.eclipse.crossmeter.metricprovider.rascal.trans.model.ListMeasurement;
import org.eclipse.crossmeter.metricprovider.rascal.trans.model.Measurement;
import org.eclipse.crossmeter.metricprovider.rascal.trans.model.RascalMetrics;
import org.eclipse.crossmeter.platform.AbstractHistoricalMetricProvider;
import org.eclipse.crossmeter.platform.IMetricProvider;
import org.eclipse.crossmeter.platform.MetricProviderContext;
import org.eclipse.crossmeter.repository.model.Project;
import org.eclipse.imp.pdb.facts.type.Type;

import com.googlecode.pongo.runtime.Pongo;
import com.mongodb.DB;

/**
 * Wraps a transient metric provider to be an historic one
 */
public class RascalMetricHistoryWrapper extends AbstractHistoricalMetricProvider {
	private final RascalMetricProvider transientId;
	private MetricProviderContext context;

	public RascalMetricHistoryWrapper(RascalMetricProvider transientProvider) {
		this.transientId = transientProvider;
	}
	
	@Override
	public String getIdentifier() {
		return transientId.getIdentifier() + ".historic";
	}

	@Override
	public String getShortIdentifier() {
		return transientId.getShortIdentifier() + ".historic";
	}

	@Override
	public String getFriendlyName() {
		return "Historic " + transientId.getShortIdentifier();
	}

	@Override
	public String getSummaryInformation() {
		return "Historic version of:\n" + transientId.getSummaryInformation();
	}

	@Override
	public boolean appliesTo(Project project) {
		return transientId.appliesTo(project);
	}

	@Override
	public void setUses(List<IMetricProvider> uses) {
		uses.add(transientId);

	}

	@Override
	public List<String> getIdentifiersOfUses() {
		return Arrays.asList(transientId.getIdentifier());
	}

	@Override
	public void setMetricProviderContext(MetricProviderContext context) {
		this.context = context;
	}

	public Type getValueType() {
		return transientId.getReturnType();
	}
	
	@Override
	public Pongo measure(Project project) {
		DB db = context.getProjectDB(project);
		RascalMetrics result = transientId.adapt(db);
		
		ListMeasurement list = new ListMeasurement();
		List<Measurement> collection = list.getValue();
		
		for (Measurement m : result.getMeasurements()) {
			collection.add(m);
		}
  
		return list;
	}
	
}