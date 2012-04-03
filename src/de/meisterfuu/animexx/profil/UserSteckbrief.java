package de.meisterfuu.animexx.profil;

import org.apache.http.client.methods.HttpGet;

import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class UserSteckbrief extends Activity  implements UpDateUI {

	String userid;
	String username;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Request.config =  PreferenceManager.getDefaultSharedPreferences(this);    	
    
        
    		HttpGet[] HTTPs = new HttpGet[1];
    		try {
				HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/miglieder/steckbrief/?user_id"+userid+"&api=2");
    	     	new TaskRequest(this).execute(HTTPs);
			} catch (Exception e) {
				e.printStackTrace();
			}


	}
    
    
	public void UpDateUi(String[] s) {
		// TODO Auto-generated method stub
		
	}

}
