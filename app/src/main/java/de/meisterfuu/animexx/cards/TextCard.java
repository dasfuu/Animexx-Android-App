package de.meisterfuu.animexx.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

import de.meisterfuu.animexx.R;


public class TextCard extends Card {

	private boolean ShowTitle;

	public TextCard(String title, String description, boolean ShowTitle){
		super(title, description);
		this.ShowTitle = ShowTitle;
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_text, null);
		
		((TextView) view.findViewById(R.id.description)).setText(desc);
		if(!ShowTitle){
			((TextView) view.findViewById(R.id.title)).setVisibility(View.GONE);
			((View) view.findViewById(R.id.line)).setVisibility(View.GONE);
		}
		((TextView) view.findViewById(R.id.title)).setText(title);
		
		return view;
	}

    @Override
    public boolean convert(View convertCardView) {
        return false;
    }


}