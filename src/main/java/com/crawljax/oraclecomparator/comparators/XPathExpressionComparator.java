/**
 * 
 */
package com.crawljax.oraclecomparator.comparators;

import com.crawljax.oraclecomparator.AbstractComparator;
import com.crawljax.util.Helper;
import com.crawljax.util.XPathHelper;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Oracle which can ignore element/attributes by xpath expression.
 * 
 * @author dannyroest@gmail.com (Danny Roest)
 * @version $Id$
 */
public class XPathExpressionComparator extends AbstractComparator {

	private static final Logger LOGGER =
	        Logger.getLogger(XPathExpressionComparator.class.getName());

	private final List<String> expressions = new ArrayList<String>();

	/**
	 * @param expressions
	 *            the xpath expressions to ignore
	 */
	public XPathExpressionComparator(List<String> expressions) {
		this.expressions.addAll(expressions);
	}

	/**
	 * @param expressions
	 *            the xpath expressions to ignore
	 */
	public XPathExpressionComparator(String... expressions) {
		for (String expression : expressions) {
			this.expressions.add(expression);
		}
	}

	/**
	 * 
	 */
	public XPathExpressionComparator() {
	}

	/**
	 * @param originalDom
	 *            The original DOM.
	 * @param newDom
	 *            The new DOM.
	 */
	public XPathExpressionComparator(String originalDom, String newDom) {
		super(originalDom, newDom);
	}

	/**
	 * Add another expression.
	 * 
	 * @param expression
	 *            The expression.
	 */
	public void addExpression(String expression) {
		expressions.add(expression);
	}

	/**
	 * @param dom
	 *            the dom to ignore the xpath expressions from
	 * @return the stripped dom with the elements found with the xpath expressions
	 */
	public String stripXPathExpressions(String dom) {
		String curExpression = "";
		try {
			Document doc = Helper.getDocument(dom);
			for (String expression : expressions) {
				curExpression = expression;
				NodeList nodeList = XPathHelper.evaluateXpathExpression(doc, expression);

				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
						((Attr) node).getOwnerElement().removeAttribute(node.getNodeName());
					} else if (node.getNodeType() == Node.ELEMENT_NODE) {
						Node parent = node.getParentNode();
						parent.removeChild(node);
					}

				}
			}
			dom = Helper.getDocumentToString(doc);
		} catch (Exception e) {
			LOGGER.error("Error with stripping XPath expression: " + curExpression, e);
		}
		return dom;
	}

	@Override
	public boolean isEquivalent() {
		setOriginalDom(stripXPathExpressions(getOriginalDom()));
		setNewDom(stripXPathExpressions(getNewDom()));
		return super.compare();
	}

}
