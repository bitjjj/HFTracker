package com.oppsis.app.hftracker.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.base.BaseFragment;
import com.oppsis.app.hftracker.ui.util.ManagerListBarCallback;
import com.oppsis.app.hftracker.util.FundFuncUtils;

public class AnalysisBaseFragment extends BaseFragment{

	protected boolean mIsVisibleToUser = true,mReload = false;
	protected String mDate;
	protected List<String> mManagerList;
	protected ManagerListBarCallback mStatusChangeListener;
	protected GestureDetector mGesture;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mManagerList = new ArrayList<String>();
    }
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		mIsVisibleToUser = isVisibleToUser;
	}
	
	protected void setTouchEvent(View view){
		view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        });
	}

	protected void changeAnalysisDate(String date){
		if(!date.equals(mDate)){
			mDate = date;
			if(mManagerList != null && mManagerList.size() != 0){
				mReload = true;
			}
		}
	}
	
	protected void changeAnalysisManagers(List<String> selectedManagers){
		if(!FundFuncUtils.isSameManagerList(mManagerList, selectedManagers)){
			mManagerList = selectedManagers;
			if(!isNeedDateEvent() || mDate != null){
				mReload = true;
			}

		}
	}
	
	protected void checkIfLoadingData(){
		if(mIsVisibleToUser && mReload){
			mReload = false;
			
			List<String> managerList = getManagerNameList();
			if(managerList.size() == 0){
				clearData();
			}
			else{
				loadData(managerList);
			}
			
		}

	}
	
	protected List<String> getManagerNameList(){
		List<String> managerNameList = new ArrayList<String>();
		for(String manager:mManagerList){
			managerNameList.add(FundObject.getManagerNameInfo(manager));
		}
		return managerNameList;
	}
	
	protected void loadData(List<String> managerNameList){
		throw new UnsupportedOperationException("You need implement this method in subclass");
	}
	
	protected void clearData(){
	}
	
	protected boolean isNeedDateEvent(){
		return true;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mStatusChangeListener = ((ManagerListBarCallback) activity);

		mGesture = new GestureDetector(getActivity(),
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onDown(MotionEvent e) {
						return true;
					}

					@Override
					public boolean onDoubleTap(MotionEvent e) {
						mStatusChangeListener.toggleManagerListBar();
						return super.onDoubleTap(e);
					}
					
					/*@Override
				    public boolean onFling(MotionEvent e1, MotionEvent e2, 
				            float velocityX, float velocityY) {
				        return super.onFling(e1, e2, velocityX, velocityY);
				    }


				    @Override
				    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				            float distanceY) {
				        
				        return super.onScroll(e1, e2, distanceX, distanceY);
				    }


				    @Override
				    public boolean onSingleTapUp(MotionEvent event) {
				        
				        return super.onSingleTapConfirmed(event);
				    }
				    
				    @Override
				    public boolean onDoubleTapEvent(MotionEvent event) {
				      
				        return super.onDoubleTapEvent(event);
				    }

				    @Override
				    public boolean onSingleTapConfirmed(MotionEvent event) {
				       
				        return super.onSingleTapConfirmed(event);
				    }*/
				});

	}

}
