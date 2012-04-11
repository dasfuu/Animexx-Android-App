package de.meisterfuu.animexx;

import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class TaskRequest extends AsyncTask<HttpRequestBase, Integer, String[]> {
	public UpDateUI GUI;

	public TaskRequest(UpDateUI GUI) {
		super();
		this.GUI = GUI;
	}

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

	protected void onProgressUpdate(Integer... progress) {

	}

	protected void onPostExecute(String[] result) {
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
