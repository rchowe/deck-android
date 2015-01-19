
package com.rchowe.thedeck;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CardSelectionActivity extends Activity
{
	private CardSelectionAdapter adapter = null;
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_card_selection );
		setupListView();
	}

	private void setupListView()
    {
		adapter = new CardSelectionAdapter(this, R.layout.activity_card_selection, MainActivity.getCards());
		ListView listview = (ListView) findViewById(R.id.cardSelectionView);
		listview.setAdapter( adapter );
    }
	
	class CardSelectionAdapter extends ArrayAdapter<Card>
	{
		private List<Card> cards;
		
		/**
		 * Creates a new card selection adapter.
		 * @param context
		 * @param textViewResourceId
		 * @param cards
		 */
		public CardSelectionAdapter(Context context, int textViewResourceId, List<Card> cards)
		{
			super(context, textViewResourceId, cards);
			this.cards = cards;
			Log.v("DECK", "Count: " + getCount());
		}
		
		private class ViewHolder
		{
			TextView code;
			CheckBox name;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			
			if ( convertView == null )
			{
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.card_info, null);
				
				holder = new ViewHolder();
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				holder.code = (TextView) convertView.findViewById(R.id.cardTitleView1);
				convertView.setTag(holder);
				holder.name.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick( View v )
						{
							CheckBox cb = (CheckBox) v;
							Card card = (Card) cb.getTag();
							
							card.enabled = cb.isChecked();
						}
					});
			}
			
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Card card = cards.get(position);
			if ( card != null && holder != null ) {
				Log.v("Position", "card(" + position + "): " + card.title);
				holder.code.setText(card.text);
				holder.name.setText(card.title);
				holder.name.setChecked(card.enabled);
				holder.name.setTag(card);
			}
			else if ( card == null ) {
				Log.v("Position", "card(" + position + "): (null)");
			}
			else if ( holder == null ) {
				Log.v("Position", "card(" + position + "): (null holder)");
			}
			
			return convertView;
		}
	}
}
