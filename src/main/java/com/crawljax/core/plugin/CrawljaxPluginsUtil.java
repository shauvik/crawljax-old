package com.crawljax.core.plugin;

import java.util.List;

import org.apache.log4j.Logger;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.invariant.Invariant;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.ProxyConfiguration;
import com.crawljax.core.state.StateVertix;
import com.crawljax.util.PropertyHelper;

/**
 * Class for invoking the plugin. The methods in this class are invoked from the Crawljax Core.
 * 
 * @author Danny Roest dannyroest@gmail.com
 * @author Stefan Lenselink S.R.Lenselink@student.tudelft.nl
 * @author Ali Mesbah amesbah@gmail.com
 * @version $Id$
 */
public final class CrawljaxPluginsUtil {

	/**
	 * Make a new Log4j object used to do the logging.
	 */
	private static final Logger LOGGER = Logger.getLogger(CrawljaxPluginsUtil.class.getName());

	/**
	 * Non instanceable constructor; does nothing never used, this constructor prevents the
	 * CrawljaxPluginsUtil to be instantiated as a Object. All methods of this class must be used
	 * statically.
	 * 
	 * @throws CrawljaxException
	 *             this exception is always thrown when instanced.
	 */
	private CrawljaxPluginsUtil() throws CrawljaxException {
		LOGGER.fatal("As this contructor is private and never used interal "
		        + "in the CrawljaxPluginsUtil, this message may never appear");
		throw new CrawljaxException("Called private never used contructor CrawljaxPluginsUtil()");
	}

	/**
	 * Load the Plugins.
	 */
	public static void loadPlugins() {
		if (PropertyHelper.getCrawljaxConfiguration() == null
		        || PropertyHelper.getCrawljaxConfiguration().getPlugins().size() == 0) {
			LOGGER.warn("No plugins loaded because CrawljaxConfiguration is empty");
			return;
		}
		for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
			/**
			 * Log the name of the plugin loaded
			 */
			LOGGER.info(plugin.getClass().getName());
		}
	}

	/**
	 * load and run the PreCrawlingPlugins. PreCrawlingPlugins are plugins that are ran before the
	 * crawling starts and before the initial url has been loaded. This kind of plugins can be used
	 * to do for example 'once in a crawlsession' operations like logging in or reset the database
	 * to a 'clean' state. The argument offered to the Plugin is a the current running instance of
	 * EmbeddedBrowser. Warning the instance of the browser offered is not a clone but the current
	 * and after wards used browser instance, changes and operations may cause 'strange' behaviour.
	 * 
	 * @see EmbeddedBrowser
	 * @param browser
	 *            the browser instance to load to the plugin.
	 */
	public static void runPreCrawlingPlugins(EmbeddedBrowser browser) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof PreCrawlingPlugin) {
					LOGGER.debug("Running preCrawlingPlugin " + plugin.getClass().getName());
					((PreCrawlingPlugin) plugin).preCrawling(browser);
				}
			}
		}
	}

	/**
	 * load and run the OnUrlLoadPlugins. The OnURLloadPlugins are run just after the Browser has
	 * gone to the initial url. Not only the first time but also every time the Core navigates back.
	 * Warning the instance of the browser offered is not a clone but the current and after wards
	 * used browser instance, changes and operations may cause 'strange' behaviour.
	 * 
	 * @param browser
	 *            the embedded browser instance to load in the plugin.
	 */
	public static void runOnUrlLoadPlugins(EmbeddedBrowser browser) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof OnUrlLoadPlugin) {
					((OnUrlLoadPlugin) plugin).onUrlLoad(browser);
				}
			}
		}
	}

	/**
	 * load and run the OnNewStatePlugins. OnNewStatePlugins are plugins that are ran when a new
	 * state was found. This also happens for the Index State. Warning the session is not a clone,
	 * chaning the session can cause strange behaviour of Crawljax.
	 * 
	 * @param session
	 *            the session to load in the plugin
	 */
	public static void runOnNewStatePlugins(CrawlSession session) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof OnNewStatePlugin) {
					((OnNewStatePlugin) plugin).onNewState(session);
				}
			}
		}
	}

	/**
	 * Run the OnInvariantViolation plugins when an Invariant is violated. Invariant are checked
	 * when the state machine is updated that is when the dom is changed after a click on a
	 * clickable. When a invariant fails this kind of plugins are executed. Warning the session is
	 * not a clone, chaning the session can cause strange behaviour of Crawljax.
	 * 
	 * @param invariant
	 *            the failed invariants
	 * @param session
	 *            the session to load in the plugin
	 */
	public static void runOnInvriantViolationPlugins(Invariant invariant, CrawlSession session) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof OnInvariantViolationPlugin) {
					((OnInvariantViolationPlugin) plugin)
					        .onInvariantViolation(invariant, session);
				}
			}
		}
	}

	/**
	 * load and run the postCrawlingPlugins. PostCrawlingPlugins are executed after the crawling is
	 * finished Warning: changing the session can change the behavior of other post crawl plugins.
	 * It is not a clone!
	 * 
	 * @param session
	 *            the session to load in the plugin
	 */
	public static void runPostCrawlingPlugins(CrawlSession session) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof PostCrawlingPlugin) {
					((PostCrawlingPlugin) plugin).postCrawling(session);
				}
			}
		}
	}

	/**
	 * load and run the onRevisitStateValidator. As a difference to other SessionPlugins this plugin
	 * needs an explicit current state because the session.getCurrentState() does not contain the
	 * correct current state because we are in back-tracking
	 * 
	 * @param session
	 *            the session to load in the plugin
	 * @param currentState
	 *            the state the 'back tracking' operation is currently in
	 */
	public static void runOnRevisitStatePlugins(CrawlSession session, StateVertix currentState) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof OnRevisitStatePlugin) {
					((OnRevisitStatePlugin) plugin).onRevisitState(session, currentState);
				}
			}
		}
	}

	/**
	 * load and run the PreStateCrawlingPlugins. Method that is called before the current state is
	 * crawled (before firing events on the current DOM state). Example: filter candidate elements.
	 * Warning the session and candidateElements are not clones, changes will result in changed
	 * behaviour.
	 * 
	 * @param session
	 *            the crawl session.
	 * @param candidateElements
	 *            the elements which crawljax is about to crawl
	 */
	public static void runPreStateCrawlingPlugins(CrawlSession session,
	        List<CandidateElement> candidateElements) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof PreStateCrawlingPlugin) {
					((PreStateCrawlingPlugin) plugin)
					        .preStateCrawling(session, candidateElements);
				}
			}
		}
	}

	/**
	 * Load and run the proxyServerPlugins. proxyServerPlugins are used to Starts the proxy server
	 * and provides Crawljax with the correct settings such as port number. Warning the config
	 * argument is not a clone, changes will influence the behaviour of the Browser. Changes should
	 * be returned as new Object.
	 * 
	 * @param config
	 *            The ProxyConfiguration to use.
	 */
	public static void runProxyServerPlugins(ProxyConfiguration config) {
		if (PropertyHelper.getCrawljaxConfiguration() != null) {
			for (Plugin plugin : PropertyHelper.getCrawljaxConfiguration().getPlugins()) {
				if (plugin instanceof ProxyServerPlugin) {
					((ProxyServerPlugin) plugin).proxyServer(config);
				}
			}
		}
	}

}
