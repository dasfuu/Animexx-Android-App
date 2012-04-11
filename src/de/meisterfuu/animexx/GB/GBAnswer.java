package de.meisterfuu.animexx.GB;


import org.apache.http.client.methods.HttpPost;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GBAnswer extends Activity  implements UpDateUI {

	TextView An;
	TextView Nachricht;
	ProgressDialog dialog;
	Button Send;
	String xAnID = "";
	String xAn = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gbanswer);

		An = (TextView) findViewById(R.id.edGBAn);
		Nachricht = (TextView) findViewById(R.id.edGB);
		Send = (Button) findViewById(R.id.BtGBSend);

		if (this.getIntent().hasExtra("an")) {
			Bundle bundle = this.getIntent().getExtras();
			xAn = bundle.getString("an");
			xAnID = bundle.getString("ID");			
		}


		An.setText(xAn);

		Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				send();
			}
		});

	}

	public void onBackPressed() {
		finish();
		return;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void send() {
		dialog = ProgressDialog.show(this, "","Sende Eintrag...", true);
		
		HttpPost[] HTTPs = new HttpPost[1];
		try {
			HTTPs[0] = Request.sendGB(""+Nachricht.getText(), xAnID);
			new TaskRequest(this).execute(HTTPs);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void UpDateUi(String[] s) {
		dialog.dismiss();
		finish();
	}

	public void DoError() {
		dialog.dismiss();
		Request.doToast("Senden gescheitert!", getApplicationContext());
	}

}
