package com.oppsis.app.hftracker.ui.base;

import com.oppsis.app.hftracker.api.APIFacade;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment{
	
	protected APIFacade mAPIFacade;
	protected Handler mMainHandler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mAPIFacade = new APIFacade(getActivity().getApplicationContext());
    }
}
