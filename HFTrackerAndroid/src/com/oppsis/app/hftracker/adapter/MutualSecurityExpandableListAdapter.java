package com.oppsis.app.hftracker.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.util.ChartGenerator;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;

public class MutualSecurityExpandableListAdapter extends BaseExpandableListAdapter{

	private Context mContext;
	private List<FundHolding> mMutualSecurityList; 
	private HashMap<String, List<FundHolding>> mMutualSecurityListChild;
	
	public MutualSecurityExpandableListAdapter(Context context, List<FundHolding> listDataHeader,
			HashMap<String, List<FundHolding>> listChildData) {
		this.mContext = context;
		this.mMutualSecurityList = listDataHeader;
		this.mMutualSecurityListChild = listChildData;

	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mMutualSecurityListChild.get(mMutualSecurityList.get(groupPosition).getFund_name())
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ChildViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.mutual_security_expandable_list_item_child, null);
			
			viewHolder.chart = (LineChart)convertView.findViewById(R.id.mutual_security_history_chart);
			viewHolder.title = (TextView)convertView.findViewById(R.id.mutual_security_history_chart_title);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ChildViewHolder)convertView.getTag();
		}
		
		ExpandableListView listView = (ExpandableListView)parent;
		if (listView.isGroupExpanded(groupPosition) && getChild(groupPosition, 0) != null ) {
			viewHolder.title.setText(mContext.getString(R.string.chart_mutual_security_history_title, 
					FundObject.getManagerName(((FundHolding)getGroup(groupPosition)).getFund_name())));
			ChartGenerator.createHoldingHistoryLineChart(mContext, viewHolder.chart, 
					mMutualSecurityListChild.get(mMutualSecurityList.get(groupPosition).getFund_name()));
		}
		else{
			viewHolder.title.setText("");
			ChartGenerator.clearChart(viewHolder.chart);
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mMutualSecurityList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mMutualSecurityList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		final FundHolding mutualHolding = mMutualSecurityList.get(groupPosition);
		HeaderViewHolder holder;
		if(convertView == null){
			holder = new HeaderViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.mutual_security_expandable_list_item, null);
			holder.port = (TextView)convertView.findViewById(R.id.mutual_security_item_port);
			holder.shares = (TextView)convertView.findViewById(R.id.mutual_security_item_shares);
			holder.activity = (TextView)convertView.findViewById(R.id.mutual_security_item_activity);
			holder.pic = (ImageView)convertView.findViewById(R.id.mutual_security_pic);
			
			convertView.setTag(holder);
		}
		else{
			holder = (HeaderViewHolder)convertView.getTag();
		}
		
		holder.shares.setText(Constants.US_DECIMAL_FORMAT.format(mutualHolding.getShares()));
		holder.port.setText(mutualHolding.getPort() + Constants.PERCENTAGE_MARK);
		holder.activity.setText(Html.fromHtml(FundFuncUtils.getActivityDesc(mutualHolding.getActivity())));
		//mImageLoader.loadImage(FundLocalInfoManager.getFundProperty(mContext, mutualHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XXH), holder.pic);
		Glide.with(mContext).load(FundLocalInfoManager.getFundProperty(mContext, mutualHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XXH))
							.placeholder(R.drawable.person_image_empty)
							.into(holder.pic);

		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	static class HeaderViewHolder{
		TextView port,shares,activity;
		ImageView pic;
		//ImageButton imgBtn;
	}
	
	static class ChildViewHolder{
		LineChart chart;
		TextView title;
	}

}
