package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.pojo.SecurityDataObject;
import com.oppsis.app.hftracker.ui.StockDetailActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;

public class FundHoldingListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundHolding> mHoldings;

	public FundHoldingListAdapter(Context context, List<FundHolding> holdings) {
		super();
		this.mContext = context;
		this.mHoldings = holdings;

	}
	
	
	@Override
	public int getCount() {
		return mHoldings.size();
	}

	@Override
	public Object getItem(int position) {
		return mHoldings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundHolding fundHolding = mHoldings.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.fund_detail_holding_list_item, null);
			
			holder.ticker = (TextView)convertView.findViewById(R.id.fund_detail_item_ticker);
			holder.name = (TextView)convertView.findViewById(R.id.fund_detail_item_name);
			holder.shares = (TextView)convertView.findViewById(R.id.fund_detail_item_shares);
			holder.value = (TextView)convertView.findViewById(R.id.fund_detail_item_value);
			holder.activity = (TextView)convertView.findViewById(R.id.fund_detail_item_activity);
			holder.port = (TextView)convertView.findViewById(R.id.fund_detail_item_port);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final String ticker = fundHolding.getTicker();
		String tickerLabel = ticker,activityStr = "";
		float activity = fundHolding.getActivity();
		int action = fundHolding.getAction();
		if(!fundHolding.getSecurityType().equals(Constants.SECURITY_TYPE_STOCK)){
			tickerLabel += "(" + fundHolding.getSecurityType() + ")"; 
		}
		
		activityStr = FundFuncUtils.getActivityDesc(action, activity);
		
		holder.ticker.setText(tickerLabel);
		holder.name.setText(fundHolding.getSecurity());
		holder.shares.setText(Constants.US_DECIMAL_FORMAT.format(fundHolding.getShares()));
		holder.value.setText(Constants.US_DECIMAL_FORMAT.format(fundHolding.getValue()));
		holder.activity.setText(Html.fromHtml(activityStr));
		holder.port.setText(fundHolding.getPort() + Constants.PERCENTAGE_MARK);
		
		holder.ticker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ticker != null && !"".equals(ticker)){

					Intent i = new Intent(mContext,StockDetailActivity.class);
					SecurityDataObject data = new SecurityDataObject(fundHolding.getTicker(), fundHolding.getSecurity(), fundHolding.getFund_name());
					Bundle bundle = new Bundle();
					bundle.putParcelable("data", data);
					i.putExtras(bundle);
					
					mContext.startActivity(i);
					((Activity)mContext).overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
				}
			}
		});
		
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView ticker,name,shares,value,activity,port;
	}

}
