package de.meisterfuu.animexx.Home;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

public class HomeKontaktActivity extends SherlockFragmentActivity {

	private JazzyViewPager mJazzy;
	private MyAdapter mMyAdapter;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;
	private boolean first = true;
	long lastUpdate = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_home_kontakt);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setTitle("Feed");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setupJazziness(TransitionEffect.FlipHorizontal);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		mMyAdapter.refreshCurrent();	
	}


	private void setupJazziness(TransitionEffect effect) {
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		mJazzy.setTransitionEffect(effect);
		mMyAdapter = new MyAdapter(getSupportFragmentManager());
		mJazzy.setAdapter(mMyAdapter);
		mJazzy.setPageMargin(30);

		try {
	            Field mScroller;
	            mScroller = ViewPager.class.getDeclaredField("mScroller");
	            mScroller.setAccessible(true); 
	            Interpolator sInterpolator = new DecelerateInterpolator();
	            FixedSpeedScroller scroller = new FixedSpeedScroller(mJazzy.getContext(), sInterpolator);
	            // scroller.setFixedDuration(5000);
	            mScroller.set(mJazzy, scroller);
	        } catch (NoSuchFieldException e) {
	     	   e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	     	   e.printStackTrace();
	        } catch (IllegalAccessException e) {
	     	   e.printStackTrace();
	        }
		mJazzy.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}


			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}


			public void onPageSelected(int arg0) {
				if (arg0 == mMyAdapter.getCount() - 2) {
					mMyAdapter.pages.remove(mMyAdapter.getCount() - 1);
					mMyAdapter.notifyDataSetChanged();
				}
			}

		});
	}


	@Override
	public void onBackPressed() {
		if (mJazzy.getCurrentItem() > 0) {
			mJazzy.setCurrentItem(mMyAdapter.getCount() - 2);
		} else {
			super.onBackPressed();
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!first && ((System.currentTimeMillis() - lastUpdate) > 120000)){
			mMyAdapter.refreshCurrent();
			lastUpdate = System.currentTimeMillis();
		} else {
			first = false;
			lastUpdate = System.currentTimeMillis();
		}
	}


	public void showDetail(String pdata) {
		mMyAdapter.addDetailPage(pdata);
		mMyAdapter.notifyDataSetChanged();
		mJazzy.setCurrentItem(mMyAdapter.getCount() - 1);
	}
	
	public void showComments(String pdata) {
		mMyAdapter.addCommentPage(pdata);
		mMyAdapter.notifyDataSetChanged();
		mJazzy.setCurrentItem(mMyAdapter.getCount() - 1);
	}
	
	public void showMulti(String pdata) {
		mMyAdapter.addPage(pdata);
		mMyAdapter.notifyDataSetChanged();
		mJazzy.setCurrentItem(mMyAdapter.getCount() - 1);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.refresh:
			mMyAdapter.refreshCurrent();
			return true;
		case R.id.new_post:
			Intent newIntent = new Intent(getApplicationContext(), HomeKontaktNewFragment.class);
			startActivityForResult(newIntent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home_kontakt_menu, menu);
		

		
		return super.onCreateOptionsMenu(menu);
	}
	


	public class MyAdapter extends FragmentPagerAdapter {//FragmentStatePagerAdapter {

		public List<SherlockFragment> pages;


		public MyAdapter(FragmentManager fm) {
			super(fm);
			initPages();
		}


		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
		
		public void refreshCurrent(){
			((Refreshable)pages.get(pages.size()-1)).refresh();
		}


		/**
		 * Create the list and add the first Fragment to the ViewPager.
		 */
		private void initPages() {
			pages = new ArrayList<SherlockFragment>();
			
			if(Request.config.getBoolean("kontakt_big",true)){
				pages.add(HomeKontaktBigFragment.newInstance());
			}else{
				pages.add(HomeKontaktFragment.newInstance());
			}

		}


		/**
		 * Add new Fragment to the ViewPager.
		 */
		public void addPage(String data) {
			pages.add(HomeKontaktFragment.newInstance(data));
		}
		
		/**
		 * Add new Fragment to the ViewPager.
		 */
		public void addDetailPage(String data) {
			pages.add(HomeKontaktDetailFragment.newInstance(data));
		}
		
		/**
		 * Add new Fragment to the ViewPager.
		 */
		public void addCommentPage(String data) {
			pages.add(HomeKontaktKommentarFragment.newInstance(data));
		}
		
		/**
		 * Add new Fragment to the ViewPager.
		 */
		public void addAnswerPage(String data) {
			pages.add(HomeKontaktFragment.newInstance(data));
		}





		@Override
		public SherlockFragment getItem(int position) {
			return pages.get(position);
		}


		@Override
		public int getCount() {
			return pages.size();
		}


		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Object obj = super.instantiateItem(container, position);
			mJazzy.setObjectForPosition(obj, position);
			return obj;
		}
	}

	public class FixedSpeedScroller extends Scroller {

		private int mDuration = 800;


		public FixedSpeedScroller(Context context) {
			super(context);
		}


		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}


		@SuppressLint("NewApi")
		public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
			super(context, interpolator, flywheel);
		}


		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}


		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}
	}

}
