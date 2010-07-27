package com.crawljax.condition.browserwaiter;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.state.Identification;

/**
 * Checks whether an element is visible.
 * 
 * @author dannyroest@gmail.com (Danny Roest)
 * @version $Id$
 */
@ThreadSafe
public class ExpectedVisibleCondition implements ExpectedCondition {

	private final Identification identification;

	/**
	 * Constructor.
	 * 
	 * @param identification
	 *            identification to use.
	 */
	public ExpectedVisibleCondition(Identification identification) {
		this.identification = identification;
	}

	@Override
	@GuardedBy("browser")
	public boolean isSatisfied(EmbeddedBrowser browser) {
		synchronized (browser) {
			return browser.isVisible(identification);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.identification;
	}

}
