package de.meisterfuu.animexx.RPG;

import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RPGNewPost extends SherlockActivity {

	private EditText edAnswer;
	private CheckBox Aktion;
	private CheckBox InTime;
	private TextView Info;
	
	
	private long id, avatar_id, char_id;
	private String char_name, rpg_name, avatar_url;
	protected Editor tempo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rpgnew_post);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("RPG");
		actionBar.setHomeButtonEnabled(true);
		
		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
			avatar_id = bundle.getLong("avatar_id");
			char_id = bundle.getLong("char_id");
			char_name = bundle.getString("char_name");
			rpg_name = bundle.getString("rpg_name");
			avatar_url = bundle.getString("avatar_url");
		} else
			finish();

		edAnswer = (EditText) findViewById(R.id.ed_answer);
		
		edAnswer.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View arg0, boolean gainFocus) {
				if (!gainFocus) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edAnswer.getWindowToken(), 0);
				}
			}

		});
		
		tempo = getSharedPreferences("RPG_HISTORY",0).edit();		
		edAnswer.setText(getSharedPreferences("RPG_HISTORY",0).getString("text_"+id, ""));		
		edAnswer.addTextChangedListener(SaveRPG);
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edAnswer.getWindowToken(), 0);
		
		Aktion = (CheckBox) findViewById(R.id.chAktion);
		InTime = (CheckBox) findViewById(R.id.chInTime);
		Info = (TextView) findViewById(R.id.tx_info);
		
		

	
	}
	
	private void send() {
		 if (edAnswer.getText().toString().length() != 0) {
			sendData();
		} else {
			Request.doToast("Der RPG Post ist leer", this);
		}
	}

	private void sendData() {

		final RPGNewPost temp = this;
		final ProgressDialog dialog = ProgressDialog.show(temp, "", "Senden...", true);
		new Thread(new Runnable() {

			public void run() {
				try {
					String erg = Request.SignSend(Request.sendRPGPost(edAnswer.getText().toString(), id, InTime.isChecked(), Aktion.isChecked(), char_id, avatar_id));
					JSONObject jsonResponse = new JSONObject(erg);
					jsonResponse.getInt("return");
					temp.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							edAnswer.setText("");
							Request.doToast("Erfolgreich gesendet", temp);
							Editor tempo = getSharedPreferences("RPG_HISTORY",0).edit();
							tempo.remove("text_"+id);
							tempo.commit();
							temp.finish();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Request.doToast("Fehler beim Senden", temp);
						}
					});
				}

			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.rpgnew_post, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_post:
			send();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private TextWatcher SaveRPG = new TextWatcher() {
		

		public void afterTextChanged(Editable s) {
			tempo.commit();
		}


		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}


		public void onTextChanged(CharSequence s, int start, int before, int count) {
			tempo.putString("text_"+id, ""+s);

		}

	};

}
