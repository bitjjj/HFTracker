package com.oppsis.app.hftracker.ui.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.APIFacade;
import com.oppsis.app.hftracker.api.util.DefaultListCallback;
import com.oppsis.app.hftracker.model.FundDateList;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.UIUtils;

public class FilterPanelView extends ScrollView{

	private Context mContext;
	
	private Spinner mFundDateSpinner;
	private Spinner mFundSortFieldSpinner;
	private Spinner mFundSortDirectionSpinner;
	private LinearLayout mSecurityTypeSectionLayout;
	private LinearLayout mSortSectionLayout;
	private CheckBox mSecurityAllCheckBox;
	private Button mDoneButton;
	private List<CheckBox> mSecurityTypeCheckBoxs;
	
	private APIFacade mApiFacade;
	private boolean mIsInit = false;
	private FundFilterCallback mFilterCallback;
	
	public FilterPanelView(Context context) {
		this(context, null, 0);
	}

	public FilterPanelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FilterPanelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		this.mContext = context;
		
		LayoutInflater.from(context).inflate(R.layout.navdrawer_filter_panel, this, true);    
		initViews(mContext);
	}

	private void initViews(Context context) {

		mFundDateSpinner = (Spinner)findViewById(R.id.fund_date_list);
		mFundSortFieldSpinner = (Spinner)findViewById(R.id.fund_sort_field_list);
		mFundSortDirectionSpinner = (Spinner)findViewById(R.id.fund_sort_direction_list);
		mSecurityTypeSectionLayout = (LinearLayout)findViewById(R.id.fund_security_type_section);
		mSortSectionLayout = (LinearLayout)findViewById(R.id.fund_sort_section);
		
		mSecurityAllCheckBox = (CheckBox)findViewById(R.id.fund_security_type_all);
		mSecurityTypeCheckBoxs = UIUtils.getViewsByTag(this,Constants.SECURITY_TYPE_PREFIX,CheckBox.class);
		
		mDoneButton = ((Button)findViewById(R.id.filter_setting_done));
		
	}
	
	private void initEvent() {
		for(CheckBox cb:mSecurityTypeCheckBoxs){
			cb.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {

					if(view.getTag().toString().contains(Constants.SECURITY_TYPE_ALL)){
						for(CheckBox cbInner:mSecurityTypeCheckBoxs){
							if(cbInner.getId() != mSecurityAllCheckBox.getId()){
								cbInner.setChecked(false);
							}
						}
					}
					else{
						boolean typeChecked = false;
						for(CheckBox cbInner:mSecurityTypeCheckBoxs){
							if(cbInner.getId() != mSecurityAllCheckBox.getId()){
								typeChecked |= cbInner.isChecked();
							}
						}
						if(typeChecked){
							mSecurityAllCheckBox.setChecked(false);
						}
					}
				}
				
			});
		}
		
		mDoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFilterCallback.filter(collectFilter());
				
			}
		});
	}

	public void initUI(FundObject fund,int tabIndex){
		if(!mIsInit){
			mIsInit = true;
			
			initEvent();
			
			loadSorterSpinner();
			
			mApiFacade = new APIFacade(mContext.getApplicationContext());
			mApiFacade.getDateList(fund, new DefaultListCallback<FundDateList>(mContext) {
				@Override
				public void onSuccess(List<FundDateList> result) {
					final String[] dataList = new String[result.size()];
					for(int i=0; i<result.size(); i++){
						dataList[i] = result.get(i).getDateStr();
					}
					
					ArrayAdapter<CharSequence> dataListAdapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_item, android.R.id.text1, dataList);
					dataListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					mFundDateSpinner.setAdapter(dataListAdapter);
					
					mDoneButton.setEnabled(true);
				}
			});
		}
	}
	
	private void loadSorterSpinner(){
		ArrayAdapter<CharSequence> sortFieldsAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.sort_fields,android.R.layout.simple_spinner_item);
		sortFieldsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFundSortFieldSpinner.setAdapter(sortFieldsAdapter);
		
		ArrayAdapter<CharSequence> sortDirectionsAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.sort_directions,android.R.layout.simple_spinner_item);
		sortDirectionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFundSortDirectionSpinner.setAdapter(sortDirectionsAdapter);
	}
	

	public void adjustUISection(int tabIndex){
		
		if(tabIndex == Constants.ACTIONBAR_LIST_INDUSTRY){
			mSecurityTypeSectionLayout.setVisibility(View.GONE);
			mSortSectionLayout.setVisibility(View.GONE);
		}
		else{
			mSecurityTypeSectionLayout.setVisibility(View.VISIBLE);
			mSortSectionLayout.setVisibility(View.VISIBLE);
		}
	}
	
	private FundFilter collectFilter(){
		FundFilter filter = new FundFilter();
		filter.setDateStr(mFundDateSpinner.getSelectedItem().toString());
		
		if(mSortSectionLayout.getVisibility() == View.VISIBLE){
			filter.setSort(Constants.SORT_FIELDS.get(mFundSortFieldSpinner.getSelectedItemPosition()));
			filter.setDirection(Constants.SORT_DIRECTIONS.get(mFundSortDirectionSpinner.getSelectedItemPosition()));
		}
		
		if(mSecurityTypeSectionLayout.getVisibility() == View.VISIBLE){
			String types = "";
			if(mSecurityAllCheckBox.isChecked()){
				types = Constants.SECURITY_TYPES_ALL;
			}
			else{
				for(CheckBox cb:mSecurityTypeCheckBoxs){
					if(cb.isChecked()){
						types += Constants.SECURITY_TYPES_MAP.get(cb.getTag()) + ",";
					}
				}
			}
			filter.setSecurityTypes(types);
		}

		return filter;
	}
	
	public void setFundFilterCallback(FundFilterCallback callback){
		mFilterCallback = callback;
	}
	
	public static interface FundFilterCallback{
		void filter(FundFilter setting);
	}
	
	public static class FundFilter{
		private String dateStr;
		private String securityTypes;
		private String sort;
		private String direction;
		
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		public String getDateStr() {
			return dateStr;
		}
		public void setDateStr(String dateStr) {
			this.dateStr = dateStr;
		}
		public String getSecurityTypes() {
			return securityTypes;
		}
		public void setSecurityTypes(String securityTypes) {
			this.securityTypes = securityTypes;
		}
		public String getSort() {
			return sort;
		}
		public void setSort(String sort) {
			this.sort = sort;
		}
		
		
		public String toString(){
			return dateStr + "/" + securityTypes + "/" + sort + "/" + direction;
		}
	}
	
}
