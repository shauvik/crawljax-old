/**
 * Created Jun 13, 2008
 */
package com.crawljax.core;

import java.util.ArrayList;
import java.util.List;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.configuration.CrawljaxConfigurationReader;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.StateFlowGraph;
import com.crawljax.core.state.StateVertix;

/**
 * The data about the crawlsession.
 * 
 * @author mesbah
 * @version $Id$
 */
public class CrawlSession {

	private final EmbeddedBrowser browser;
	private final StateFlowGraph stateFlowGraph;
	private final List<List<Eventable>> crawlPaths = new ArrayList<List<Eventable>>();
	private StateVertix currentState;
	private final StateVertix initialState;
	private final CrawljaxConfigurationReader crawljaxConfiguration;
	private final long startTime;
	// TODO Stefan; optimise / change this behaviour this is not the most speedy solution
	private final ThreadLocal<List<Eventable>> exactEventPath =
	        new ThreadLocal<List<Eventable>>();

	/**
	 * @param browser
	 *            the Embedded browser.
	 */
	public CrawlSession(EmbeddedBrowser browser) {
		this(browser, null, null, 0);
	}

	/**
	 * @param browser
	 *            the embedded browser instance.
	 * @param stateFlowGraph
	 *            the state flow graph.
	 * @param state
	 *            the current state.
	 * @param startTime
	 *            the time this session started in milliseconds.
	 */
	public CrawlSession(EmbeddedBrowser browser, StateFlowGraph stateFlowGraph,
	        StateVertix state, long startTime) {
		this(browser, stateFlowGraph, state, startTime, null);
	}

	/**
	 * @param browser
	 *            the embedded browser instance.
	 * @param stateFlowGraph
	 *            the state flow graph
	 * @param state
	 *            the current state.
	 * @param startTime
	 *            the time this session started in milliseconds.
	 * @param crawljaxConfiguration
	 *            the configuration.
	 */
	public CrawlSession(EmbeddedBrowser browser, StateFlowGraph stateFlowGraph,
	        StateVertix state, long startTime, CrawljaxConfigurationReader crawljaxConfiguration) {
		this.crawljaxConfiguration = crawljaxConfiguration;
		this.browser = browser;
		this.stateFlowGraph = stateFlowGraph;
		this.currentState = state;
		this.initialState = state;
		this.startTime = startTime;
	}

	/**
	 * @return the browser
	 */
	public EmbeddedBrowser getBrowser() {
		return browser;
	}

	/**
	 * @return the stateFlowGraph
	 */
	public StateFlowGraph getStateFlowGraph() {
		return stateFlowGraph;
	}

	/**
	 * @return the currentState
	 */
	public StateVertix getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(StateVertix currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the crawlPaths
	 */
	public List<List<Eventable>> getCrawlPaths() {
		return crawlPaths;
	}

	/**
	 * @param crawlPath
	 *            the eventable list
	 */
	public void addCrawlPath(List<Eventable> crawlPath) {
		this.crawlPaths.add(crawlPath);
	}

	/**
	 * @return the crawljaxConfiguration
	 */
	public CrawljaxConfigurationReader getCrawljaxConfiguration() {
		return crawljaxConfiguration;
	}

	/**
	 * @return the initialState
	 */
	public final StateVertix getInitialState() {
		return initialState;
	}

	/**
	 * @return the startTime
	 */
	public final long getStartTime() {
		return startTime;
	}

	/**
	 * @return the exactEventPath
	 */
	public List<Eventable> getExactEventPath() {
		return exactEventPath.get();
	}

	/**
	 * @param exactEventPath
	 *            the exactEventPath to set
	 */
	public void setExactEventPath(List<Eventable> exactEventPath) {
		this.exactEventPath.set(exactEventPath);
	}

}
