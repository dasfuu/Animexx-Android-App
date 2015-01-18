package de.meisterfuu.animexx;

import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class TaskRequest extends AsyncTask<HttpRequestBase, Integer, String[]> {
	private UpDateUI GUI;
	public boolean finished = false;

	public TaskRequest(UpDateUI GUI) {
		super();
		attach(GUI);
	}

	public void detach() {
		GUI=null;
	}

	public void attach(UpDateUI GUI) {
	    this.GUI=GUI;
	}

	@Override
	protected String[] doInBackground(HttpRequestBase... requests) {
		int count = requests.length;
		String[] s = new String[count];
		try {
			for (int i = 0; i < count; i++) {
				s[i] = Request.SignSend(requests[i]);
			}
			return s;
		} catch (Exception e) {
			return new String[] { "Error" };
		}
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

	}

	@Override
	protected void onPostExecute(String[] result) {
		finished = true;
		if (result[0].equals("Error")) {
			GUI.DoError();
		} else {
			try {
				JSONObject jsonResponse = new JSONObject(result[0]);
				if(jsonResponse.getBoolean("success") == true) GUI.UpDateUi(result); else GUI.DoError();
			} catch (JSONException e) {
				e.printStackTrace();
				GUI.DoError();
			}
		}
	}
}
