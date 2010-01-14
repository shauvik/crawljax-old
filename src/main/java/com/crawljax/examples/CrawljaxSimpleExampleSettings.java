package com.crawljax.examples;

import org.apache.commons.configuration.ConfigurationException;

import com.crawljax.browser.WebDriverFirefox;
import com.crawljax.core.CrawljaxController;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawlSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;

/**
 * Simple Example.
 * 
 * @author dannyroest@gmail.com (Danny Roest)
 * @version $id$
 */
public final class CrawljaxSimpleExampleSettings {

	private static final String URL = "http://www.google.com";
	private static final int MAX_DEPTH = 2;
	private static final int MAX_NUMBER_STATES = 8;

	private CrawljaxSimpleExampleSettings() {

	}

	private static CrawljaxConfiguration getCrawljaxConfiguration() {
		CrawljaxConfiguration config = new CrawljaxConfiguration();
		config.setCrawlSpecification(getCrawlSpecification());
		config.setBrowser(new WebDriverFirefox());
		return config;
	}

	private static CrawlSpecification getCrawlSpecification() {
		CrawlSpecification crawler = new CrawlSpecification(URL);
		crawler.setNumberOfThreads(1);

		// click these elements
		crawler.click("a");
		crawler.click("input").withAttribute("type", "submit");

		// except these
		crawler.dontClick("a").underXPath("//DIV[@id='guser']");
		crawler.dontClick("a").withText("Language Tools");

		crawler.setInputSpecification(getInputSpecification());

		// limit the crawling scope
		crawler.setMaximumStates(MAX_NUMBER_STATES);
		crawler.setDepth(MAX_DEPTH);

		return crawler;
	}

	private static InputSpecification getInputSpecification() {
		InputSpecification input = new InputSpecification();
		input.field("q").setValue("Crawljax");
		return input;
	}

	/**
	 * @param args
	 *            the command line args
	 */
	public static void main(String[] args) {
		try {
			CrawljaxController crawljax = new CrawljaxController(getCrawljaxConfiguration());
			crawljax.run();
		} catch (CrawljaxException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
