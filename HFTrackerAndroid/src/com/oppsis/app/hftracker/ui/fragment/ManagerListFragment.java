package com.oppsis.app.hftracker.ui.fragment;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.ManagerIconListAdapter;
import com.oppsis.app.hftracker.event.ChangeFundEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;

@EFragment(R.layout.fragment_manager_icon_list)
public class ManagerListFragment extends Fragment {

	@ViewById(R.id.manager_list)protected ListView mManagerList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@Bean protected OttoBus mBus;
	
	private ManagerIconListAdapter mIconAdapter;
	private List<FundObject> mFundItems;
	
	@AfterViews
	protected void initUI(){
		loadFundList();
	}
	
	@Background
	protected void loadFundList(){
		mFundItems = FundLocalInfoManager.getFundList(getActivity());
		showFundList();
	}
	
	@UiThread
	protected void showFundList() {
		mLoadingView.setVisibility(View.GONE);
		if(mIconAdapter == null){
			mIconAdapter = new ManagerIconListAdapter(getActivity(), mFundItems);
		}
		
		mManagerList.setAdapter(mIconAdapter);
		
		if(mFundItems.size() > 0){
			mBus.post(new ChangeFundEvent(mFundItems.get(0)));
		}
	}
	
	@ItemClick(R.id.manager_list)
	protected void managerItemSelect(FundObject fund){
		mBus.post(new ChangeFundEvent(fund));
	}
	
	
	
}
