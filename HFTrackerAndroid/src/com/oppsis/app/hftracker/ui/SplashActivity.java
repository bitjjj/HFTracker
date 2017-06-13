package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.oppsis.app.hftracker.R;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends FragmentActivity{
	
	@ViewById(R.id.proverbs_content)protected TextView mContent;
	@ViewById(R.id.proverbs_author)protected TextView mAuthor;
	private Thread mSplashThread;
	private final static int MAX = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final SplashActivity sPlashScreen = this;

		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(1500);
					}
				} catch (InterruptedException ex) {
				}

				finish();

				Intent intent = new Intent(sPlashScreen,MainActivity_.class);

				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
			}
		};

		mSplashThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}
	
	@AfterViews
	protected void initUI(){
		int index = (int)(Math.random()*MAX);
		String proverb = getResources().getStringArray(R.array.proverbs)[index];
		mContent.setText(proverb.split("\\|")[0]);
		mAuthor.setText(proverb.split("\\|")[1]);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

}
