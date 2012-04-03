package de.meisterfuu.animexx;


import org.apache.http.client.methods.HttpRequestBase;
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
        	return new String[] {"Error"};
		}
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String[] result) {
    	if (result[0].equals("Error")){
    		GUI.DoError();
    	}else{
    		GUI.UpDateUi(result);
    	}
    }
}
