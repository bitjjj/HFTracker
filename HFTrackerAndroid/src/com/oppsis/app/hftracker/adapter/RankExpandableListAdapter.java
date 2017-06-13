package com.oppsis.app.hftracker.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.pojo.SecurityDataObject;
import com.oppsis.app.hftracker.pojo.StockQuoteObject;
import com.oppsis.app.hftracker.ui.StockDetailActivity;
import com.oppsis.app.hftracker.util.Constants;

public class RankExpandableListAdapter extends BaseExpandableListAdapter{

	private Context mContext;
	private List<FundAnalysis> mRankHoldingList; 
	private HashMap<String, List<Map.Entry<String, Float>>> mRankHoldingListChild;
	private Map<String, StockQuoteObject> mStockQuote;
	
	public RankExpandableListAdapter(Context context, List<FundAnalysis> listDataHeader,
			HashMap<String, List<Map.Entry<String, Float>>> listChildData,Map<String, StockQuoteObject> stockData) {
		this.mContext = context;
		this.mRankHoldingList = listDataHeader;
		this.mRankHoldingListChild = listChildData;
		this.mStockQuote = stockData;

	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mRankHoldingListChild.get(mRankHoldingList.get(groupPosition).getId()).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		Map.Entry<String, Float> childEntry = (Map.Entry<String, Float>)getChild(groupPosition, childPosition);
		
		ChildViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_expandable_list_item_child, parent,false);
			
			viewHolder.pic = (ImageView)convertView.findViewById(R.id.rank_item_child_pic);
			viewHolder.port = (TextView)convertView.findViewById(R.id.rank_item_child_port);
			viewHolder.manager = (TextView)convertView.findViewById(R.id.rank_item_child_manager);
			viewHolder.fund = (TextView)convertView.findViewById(R.id.rank_item_child_fund);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ChildViewHolder)convertView.getTag();
		}
		
		viewHolder.port.setText(childEntry.getValue() + Constants.PERCENTAGE_MARK);
		viewHolder.manager.setText(FundLocalInfoManager.getI18nFundProperty(mContext, childEntry.getKey(), FundObject.PROPERTY_MANAGER_NAME));
		viewHolder.fund.setText(FundLocalInfoManager.getI18nFundProperty(mContext, childEntry.getKey(), FundObject.PROPERTY_FUND_NAME));
		String imgUrl = FundLocalInfoManager.getFundProperty(mContext, childEntry.getKey(), FundObject.PROPERTY_MANAGER_ICON_PIC_XH);
		//mImageLoader.loadImage(imgUrl, viewHolder.pic);
		Glide.with(mContext).load(imgUrl).placeholder(R.drawable.person_image_empty).into(viewHolder.pic);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mRankHoldingListChild.get(mRankHoldingList.get(groupPosition).getId()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mRankHoldingList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mRankHoldingList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		final FundAnalysis rankHolding = mRankHoldingList.get(groupPosition);
		HeaderViewHolder holder;
		if(convertView == null){
			holder = new HeaderViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_expandable_list_item, parent,false);
			holder.ticker = (TextView)convertView.findViewById(R.id.rank_item_ticker);
			holder.security = (TextView)convertView.findViewById(R.id.rank_item_name);
			holder.price = (TextView)convertView.findViewById(R.id.rank_item_price);
			holder.changePercent = (TextView)convertView.findViewById(R.id.rank_item_change_percent);
			holder.heldCount = (TextView)convertView.findViewById(R.id.rank_item_held_count);
			holder.arrow = (ImageView)convertView.findViewById(R.id.rank_item_arrow);
			
			convertView.setTag(holder);
		}
		else{
			holder = (HeaderViewHolder)convertView.getTag();
		}
		
		final String ticker = rankHolding.getTicker();
		holder.ticker.setText(ticker);
		holder.security.setText(rankHolding.getSecurity());
		holder.heldCount.setText(rankHolding.getCount() + "");
		
		
		if(mStockQuote.containsKey(ticker)){
			StockQuoteObject stockData = mStockQuote.get(ticker);
			holder.price.setText(String.valueOf(stockData.getPrice()));
			holder.changePercent.setText(String.valueOf(stockData.getChangePercent()));
		}
		
		holder.ticker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ticker != null && !"".equals(ticker)){
					Intent i = new Intent(mContext,StockDetailActivity.class);
					SecurityDataObject data = new SecurityDataObject(rankHolding.getTicker(), rankHolding.getSecurity(), null,SecurityDataObject.ICON_HIDE);
					Bundle bundle = new Bundle();
					bundle.putParcelable("data", data);
					i.putExtras(bundle);
					
					mContext.startActivity(i);
					((Activity)mContext).overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
				}
			}
		});
		
		final ExpandableListView elv = (ExpandableListView)parent;
		if(elv.isGroupExpanded(groupPosition)){
			holder.arrow.setImageResource(R.drawable.ic_action_expand);
		}
		else{
			holder.arrow.setImageResource(R.drawable.ic_action_next_item);
		}
		
		holder.arrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tag = v.getTag().toString();
				ImageView iv = (ImageView)v;
				if(tag.equals(Constants.TRUE_STR)){
					elv.collapseGroup(groupPosition);
					iv.setImageResource(R.drawable.ic_action_next_item);
					v.setTag(Constants.FALSE_STR);
				}
				else{
					elv.expandGroup(groupPosition);
					iv.setImageResource(R.drawable.ic_action_expand);
					v.setTag(Constants.TRUE_STR);
				}
			}
		});

		
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
	
	public void setStockQuote(Map<String, StockQuoteObject> stockQuote){
		mStockQuote = stockQuote;
	}
	
	public void addStockQuote(Map<String, StockQuoteObject> stockQuote){
		mStockQuote.putAll(stockQuote);
	}
	
	static class HeaderViewHolder{
		TextView ticker,security,price,changePercent,heldCount;
		ImageView arrow;
		//ImageButton imgBtn;
	}
	
	static class ChildViewHolder{
		ImageView pic;
		TextView port,manager,fund;
	}

}
