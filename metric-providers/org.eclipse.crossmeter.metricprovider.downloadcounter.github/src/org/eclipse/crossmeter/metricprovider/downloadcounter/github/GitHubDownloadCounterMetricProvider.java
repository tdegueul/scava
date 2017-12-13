/*******************************************************************************
 * Copyright (c) 2014 CROSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Davide Di Ruscio - Implementation.
 *******************************************************************************/
package org.eclipse.crossmeter.metricprovider.downloadcounter.github;

import java.net.UnknownHostException;

import org.eclipse.crossmeter.metricprovider.downloadcounter.model.DownloadCounter;
import org.eclipse.crossmeter.platform.AbstractTransientMetricProvider;
import org.eclipse.crossmeter.platform.Date;
import org.eclipse.crossmeter.platform.Platform;
import org.eclipse.crossmeter.platform.delta.ProjectDelta;
import org.eclipse.crossmeter.repository.model.Project;
import org.eclipse.crossmeter.repository.model.github.*;
import org.eclipse.crossmeter.repository.model.vcs.git.GitRepository;
import org.eclipse.egit.github.core.Download;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.DownloadService;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.googlecode.pongo.runtime.PongoFactory;
import com.googlecode.pongo.runtime.osgi.OsgiPongoFactoryContributor;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class GitHubDownloadCounterMetricProvider extends AbstractTransientMetricProvider<DownloadCounter>{

	@Override
	public boolean appliesTo(Project project) {
		return project instanceof GitHubRepository;
	}

	@Override
	public DownloadCounter adapt(DB db) {
		return new DownloadCounter(db);
	}

	@Override
	public void measure(Project project, ProjectDelta delta, DownloadCounter db) {
		
		if (!new Date().toString().equals(delta.getDate().toString())) return;
		
		GitHubRepository gitHubProject = (GitHubRepository) project;
		
		GitRepository gitHubRepository = ((GitRepository)gitHubProject.getVcsRepositories().get(0));
		String owner = gitHubRepository.getUsername();
		
		try {
			RepositoryService repositoryService = new RepositoryService();
			Repository repository = null;
			for (Repository r : repositoryService.getRepositories(owner)) {
				if (gitHubRepository.getName().equals(r.getName())) repository = r;
			}
			DownloadService downloadService = new DownloadService();
			
			int downloads = 0;
			
			for (Download download : downloadService.getDownloads(repository)) {
				downloads += download.getDownloadCount();
			}
			
			org.eclipse.crossmeter.metricprovider.downloadcounter.model.Download download = null;
			if (db.getDownloads().size() == 0) {
				download = new org.eclipse.crossmeter.metricprovider.downloadcounter.model.Download();
				db.getDownloads().add(download);
			}
			download.setCounter(downloads);
			db.sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Date d = new Date();
		
		GitHubDownloadCounterMetricProvider provider = new GitHubDownloadCounterMetricProvider();
		Project p1 = (Project)provider.createGitHubProject("mojombo", "grit");
		
		try 
		{
			Mongo mongo;
			mongo = new Mongo();
			PongoFactory.getInstance().getContributors().add(new OsgiPongoFactoryContributor());
			Platform platform = new Platform(mongo);
			provider.measure(p1, new ProjectDelta(p1, d, platform),new DownloadCounter());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public GitHubRepository createGitHubProject(String login, String repository) {
		GitHubRepository project = new GitHubRepository();
		
		GitRepository gitHubRepository = new GitRepository();
		gitHubRepository.setName(repository);
		
		GitHubUser owner = new GitHubUser();
		owner.setLogin(login);
		gitHubRepository.setUsername(owner.getLogin());
		
		project.getVcsRepositories().add(gitHubRepository);
		
		return project;
	}

	@Override
	public String getShortIdentifier() {
		return "ghdc";
	}

	@Override
	public String getFriendlyName() {
		return "Download counter";
	}

	@Override
	public String getSummaryInformation() {
		return "Lorum ipsum";
	}
}