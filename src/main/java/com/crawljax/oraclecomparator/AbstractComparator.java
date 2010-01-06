package com.crawljax.oraclecomparator;

import java.util.List;

import org.custommonkey.xmlunit.Difference;

import com.crawljax.util.Helper;

/**
 * @author Danny
 * @version $Id: AbstractOracle.java 6388 2009-12-29 13:36:00Z mesbah $
 */
public abstract class AbstractComparator implements Comparator {

	private String originalDom;
	private String newDom;

	/**
	 * Constructor.
	 */
	public AbstractComparator() {

	}

	/**
	 * @param originalDom
	 *            The original DOM.
	 * @param newDom
	 *            The new DOM.
	 */
	public AbstractComparator(String originalDom, String newDom) {
		this.originalDom = originalDom;
		this.newDom = newDom;
	}

	/**
	 * @return If the original DOM and the new DOM are equal. Note: Via
	 *         OracleControllerConfiguration ignore case can be set
	 */
	protected boolean compare() {
		boolean equivalent = false;
		if (StateComparator.COMPARE_IGNORE_CASE) {
			equivalent = getOriginalDom().equalsIgnoreCase(getNewDom());
		} else {
			equivalent = getOriginalDom().equals(getNewDom());
		}
		return equivalent;
	}

	/**
	 * @return Whether they are equivalent.
	 */
	public abstract boolean isEquivalent();

	/**
	 * @return Differences between the DOMs.
	 */
	public List<Difference> getDifferences() {
		return Helper.getDifferences(getOriginalDom(), getNewDom());
	}

	/**
	 * @return The original DOM.
	 */
	public String getOriginalDom() {
		return originalDom;
	}

	/**
	 * @param originalDom
	 *            The new original DOM.
	 */
	public void setOriginalDom(String originalDom) {
		this.originalDom = originalDom;
	}

	/**
	 * @return The new DOM.
	 */
	public String getNewDom() {
		return newDom;
	}

	/**
	 * @param newDom
	 *            The new DOM.
	 */
	public void setNewDom(String newDom) {
		this.newDom = newDom;
	}

}
