package com.oppsis.app.hftracker.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.adapter.MutualSecurityExpandableListAdapter;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;

//FIXME:【暂时忽略，待有反馈再说】当shares为0时，点击来看不到自己的持仓
@EActivity(R.layout.activity_mutual_security)
public class MutualSecurityActivity extends BaseSwipeAndActionBarActivity{

	@ViewById(R.id.mutual_security_list)protected ExpandableListView mSecurityList;
	@ViewById(R.id.loading_view) protected View mLoadingView;
	@ViewById(R.id.mutual_security_header_date)protected TextView mHeaderDateText;
	
	@Extra
	protected FundHolding holding;
	@Extra
	protected String date;
	
	private MutualSecurityExpandableListAdapter mSecurityListAdapter;
	private List<FundHolding> mSecurityResults;
	private HashMap<String, List<FundHolding>> mSecurityResultsChild;
	private ActionMode mActionMode;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

    }
	
	
	@AfterViews
	public void initUI(){
		super.initUI(holding.getSecurity());
		
		mSecurityList.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {

				if(mActionMode == null){
					return false;
				}else{
					parent.setItemChecked(groupPosition,!parent.isItemChecked(groupPosition));
					return true;
				}
			}
		});
		
		mSecurityList.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				if(mActionMode == null){
					loadChildData(groupPosition);
				}
			}
		});
		mSecurityList.setEmptyView(mLoadingView);
		mSecurityList.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE_MODAL);
		mSecurityList.setMultiChoiceModeListener(choiceModeListener);
		
		mHeaderDateText.setText(date);
		
		loadData();
	}
	
	
	@Background(id="mutual_security")
	protected void loadData(){
		mAPIFacade.getHoldingMutualList(holding, date, new DefaultListCallback<FundHolding>(this) {
			
			@Override
			public void onSuccess(List<FundHolding> result) {
				mSecurityResults = result;
				mSecurityResultsChild = new HashMap<String, List<FundHolding>>();
				for(int i=0; i<mSecurityResults.size(); i++){
					List<FundHolding> childList = new ArrayList<FundHolding>();
					childList.add(null);
					
					mSecurityResultsChild.put(mSecurityResults.get(i).getFund_name(), childList);
				}
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						mSecurityListAdapter = new MutualSecurityExpandableListAdapter(MutualSecurityActivity.this, mSecurityResults,mSecurityResultsChild);
						mSecurityList.setAdapter(mSecurityListAdapter);
					}
				});
			}
		});
	}
	
	@Background(id="mutual_security")
	protected void loadChildData(final int groupPosition){
		mAPIFacade.getHoldingHistoryList(mSecurityResults.get(groupPosition), new DefaultListCallback<FundHolding>(this) {
			@Override
			public void onSuccess(final List<FundHolding> result) {
				mSecurityResultsChild.get(mSecurityResults.get(groupPosition).getFund_name()).clear();
				mSecurityResultsChild.get(mSecurityResults.get(groupPosition).getFund_name()).addAll(result);
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						mSecurityListAdapter.notifyDataSetChanged();
					}
				});
			}
		});
	}

	private MultiChoiceModeListener choiceModeListener = new MultiChoiceModeListener(){

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {

			case R.id.menu_item_mutual_security_comparator:
				SparseBooleanArray checkedItemPositions = mSecurityList.getCheckedItemPositions();
				int itemCount = mSecurityList.getCount();
				List<String> names = new ArrayList<String>();
				for(int i = itemCount-1; i >= 0; i--){
					if(checkedItemPositions.get(i)){
						names.add(mSecurityResults.get(i).getFund_name());
					}
		        }
				checkedItemPositions.clear();
				MutualSecurityComparatorActivity_.intent(MutualSecurityActivity.this).names(names.toArray(new String[0])).holding(holding).start();
				overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
				mode.finish();
				
				
				break;
				
			default:
				return false;
				
			}
			return true;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_contextual_mutual_security, menu);
			
			for(int i=0; i<mSecurityResults.size(); i++){
				mSecurityList.collapseGroup(i);
			}
			
			mActionMode = mode;
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			
			int count = mSecurityList.getCheckedItemCount();
			mode.setTitle(String.format(getString(R.string.selected_items_tip), count));
			
		}
	};
	
}
