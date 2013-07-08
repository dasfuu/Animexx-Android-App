package de.meisterfuu.animexx.Home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloader;


public class HomeKontaktNewFragment extends SherlockFragment {

	ArrayList<HomeKontaktObject> Array = new ArrayList<HomeKontaktObject>();

	Context context;
	ImageDownloader Images = new ImageDownloader();


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list, null);

		

		return view;
	}


	public static HomeKontaktNewFragment newInstance() {
		HomeKontaktNewFragment myFragment = new HomeKontaktNewFragment();
		return myFragment;
	}




}