/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    James Williams - Implementation,
 *    Juri Di Rocco - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.platform.importers.tests;

import static org.junit.Assert.*;

import org.eclipse.crossmeter.platform.Platform;
import org.eclipse.crossmeter.platform.delta.vcs.AbstractVcsManager;
import org.eclipse.crossmeter.platform.vcs.git.GitManager;
import org.eclipse.crossmeter.platform.vcs.svn.SvnManager;
import org.eclipse.crossmeter.repository.model.*;
import org.eclipse.crossmeter.repository.model.eclipse.EclipseProject;
import org.eclipse.crossmeter.repository.model.eclipse.importer.EclipseProjectImporter;
import org.eclipse.crossmeter.repository.model.vcs.git.GitRepository;
import org.eclipse.crossmeter.repository.model.vcs.svn.SvnRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.Mongo;

public class EclipseManagerTest {
	static Mongo mongo;
	static Platform platform;
	static EclipseProjectImporter im;
	AbstractVcsManager manager;
	
	@BeforeClass
	public static void setup() throws Exception {
		mongo = new Mongo();
		platform = new Platform(mongo);
		im = new EclipseProjectImporter();
		//repository.setUrl("https://code.google.com/p/super-awesome-fighter");
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		mongo.close();
	}
	
	@Test
	public void testEclipseBTS()
	{
		fail("Not yet implement");
	}
	
	
	@Test
	public void testEclipseVCSRepository() throws Exception {
		ProjectCollection pc = platform.getProjectRepositoryManager().getProjectRepository().getProjects();
		boolean test = true;
		int countSVN = 0;
		int countGit = 0;
		for (Project project : pc) 
		{
			
			if (project instanceof EclipseProject)
			{
				for (VcsRepository vcs : project.getVcsRepositories()) {
					
					if (vcs instanceof GitRepository)
					{
						try
						{
							countGit ++;
							manager = new GitManager();
							GitRepository bb = (GitRepository)vcs;
							manager.getFirstRevision(bb);
						} catch (Exception e) {
							System.out.println("Manager exception when call GIT getFirstRevision for Project: " + project.getShortName());
							test = false;
						}
					}
					if (vcs instanceof SvnRepository)
					{
						try
						{
							countSVN ++;
							manager = new SvnManager();
							SvnRepository bb = (SvnRepository)vcs;
							manager.getFirstRevision(bb);
						} catch (Exception e) {
							System.out.println("Manager exception when call SVN getFirstRevision for Project: " + project.getShortName());
							test = false;
						}
					}
				}
			}
		}
		System.out.println("Total Git VCS = " + countGit );
		System.out.println("Total SVN VCS = " + countSVN );
		assertTrue(test);
	}
}