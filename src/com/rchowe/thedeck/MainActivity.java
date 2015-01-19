
package com.rchowe.thedeck;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{
	private static List<Card> cards;
	private List<Integer> shoe;
	
	public static List<Card> getCards() {
		return cards;
	}
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		if ( cards == null )
			cards = loadCards();
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if ( id == R.id.action_choose_cards )
		{
			Intent intent = new Intent(this, CardSelectionActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected( item );
	}
	
	public void drawCard(View view)
	{
		if ( cards == null )
			cards = loadCards();
		
		// Shuffle the shoe, if necessary
		if ( this.shoe == null || this.shoe.size() == 0 )
		{
			shuffle();
		}
		
		Card card = null;
		do {
			card = cards.get(this.shoe.remove(0).intValue());
		} while ( this.shoe.size() > 0 && card.enabled == false );
		
		if ( this.shoe.size() <= 0 )
		{
			shuffle();
			do {
				card = cards.get(this.shoe.remove(0).intValue());
			} while ( this.shoe.size() > 0 && card.enabled == false );
			
			if ( this.shoe.size() <= 0 )
			{
				Toast.makeText( getApplicationContext(), "No selected cards!", Toast.LENGTH_LONG ).show();
				return;
			}
		}
		
		showCard(card);
	}
	
	private void shuffle()
	{
		this.shoe = new ArrayList<Integer>(cards.size());
		for ( int i = cards.size()-1; i >= 0; i-- )
			this.shoe.add(i);
		Collections.shuffle(this.shoe);
	}
	
	public void showCard(Card card)
	{
		// Get views
		TextView cardTitleView      = (TextView) findViewById(R.id.cardTitleView);
		TextView cardTextView       = (TextView) findViewById(R.id.cardTextView);
		TextView cardFlavortextView = (TextView) findViewById(R.id.cardFlavortextView);

		cardTitleView.setText(card.title);
		cardTextView.setText(card.text);
		cardFlavortextView.setText(card.flavortext);
	}
	
	/**
	 * Loads a list of cards.
	 * 
	 * This tries to load the following resources:
	 * 
	 * 1. The local, up-to-date cache of cards.
	 * 2. The built-in set of cards.
	 * 
	 * @return A list of cards.
	 */
	@SuppressWarnings( "unchecked" )
    private List<Card> loadCards()
	{
		ArrayList<Card> deck = new ArrayList<Card>();

		Yaml yaml = new Yaml();

		Collection<Object> cards = ((Map<String,Collection<Object>>) yaml.load(localCardCache())).get("cards");
		for ( Object rawCard : cards )
		{
			Map<String,Object> rawCard2 = (Map<String,Object>) rawCard;
			String title = ((String) rawCard2.get( "title" )).trim();
			String text = ((String) rawCard2.get( "text" )).trim();
			String flavortext = null;
			if ( rawCard2.containsKey( "flavor_text" ) )
			{
				flavortext = ((String) rawCard2.get("flavor_text")).trim();
			}
			deck.add(new Card(title, text, flavortext));
		}
		return deck;
	}
	
	/**
	 * Gets the local card cache, or creates it from the builtin list.
	 * @return
	 */
	private synchronized InputStream localCardCache()
	{
		String filename = "cards.yaml";
		
		// Check if the file exists
		boolean found = false;
		for ( String file : fileList() )
		{
			if ( file.equals(filename) )
			{
				found = true;
				break;
			}
		}
		
		// If the file doesn't exist, use the builtin.
		if ( found )
		{
			try {
				return openFileInput(filename);
			}
			
			catch ( FileNotFoundException ex ) {}
		}
		
		System.out.println("Could not find local card cache -- using builtin.");
		return getResources().openRawResource(R.raw.builtin_cards);
	}
}
