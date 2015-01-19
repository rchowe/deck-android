
package com.rchowe.thedeck;


/**
 * @author RC Howe
 * A class describing a card.
 */
public class Card
{
	public final String title;
	public final String text;
	public final String flavortext;
	public boolean enabled = true;
	
	/**
	 * Creates a new card with the given title and text
	 * @param title
	 * @param text
	 */
	Card(String title, String text)
	{
		this(title, text, null);
	}
	
	/**
	 * Creates a new card with the given title, text, and flavortext
	 * @param title
	 * @param text
	 * @param flavortext
	 */
	Card(String title, String text, String flavortext)
	{
		if ( title == null || text == null )
			throw new NullPointerException("Title or text is null");
		
		this.title = title;
		this.text = text;
		this.flavortext = flavortext;
	}
}
