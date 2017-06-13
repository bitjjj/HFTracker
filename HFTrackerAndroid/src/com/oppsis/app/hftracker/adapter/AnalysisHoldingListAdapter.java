package com.oppsis.app.hftracker.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundAnalysis;
import com.oppsis.app.hftracker.pojo.StockQuoteObject;

public class AnalysisHoldingListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundAnalysis> mHoldings;
	private Map<String, StockQuoteObject> mStockData;

	public AnalysisHoldingListAdapter(Context context, List<FundAnalysis> holdings,Map<String, StockQuoteObject> stockData) {
		super();
		this.mContext = context;
		this.mHoldings = holdings;
		this.mStockData = stockData;
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
	
	public void setStockData(Map<String, StockQuoteObject> stockData){
		mStockData = stockData;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundAnalysis analysisHolding = mHoldings.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.analysis_holding_list_item, null);
			
			holder.ticker = (TextView)convertView.findViewById(R.id.analysis_holding_item_ticker);
			holder.name = (TextView)convertView.findViewById(R.id.analysis_holding_item_name);
			holder.price = (TextView)convertView.findViewById(R.id.analysis_holding_item_price);
			holder.change = (TextView)convertView.findViewById(R.id.analysis_holding_item_change);
			holder.changePercent = (TextView)convertView.findViewById(R.id.analysis_holding_item_change_percent);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		String ticker = analysisHolding.getTicker();
		holder.ticker.setText(ticker);
		holder.name.setText(analysisHolding.getSecurity());
		
		if(mStockData.containsKey(ticker)){
			StockQuoteObject stockData = mStockData.get(ticker);
			holder.price.setText(String.valueOf(stockData.getPrice()));
			holder.change.setText(String.valueOf(stockData.getChange()));
			holder.changePercent.setText(stockData.getChangePercent());
			
			if(stockData.getChange() > 0){
				holder.change.setTextColor(Color.GREEN);
				holder.changePercent.setTextColor(Color.GREEN);
			}
			else if(stockData.getChange() < 0){
				holder.change.setTextColor(Color.RED);
				holder.changePercent.setTextColor(Color.RED);
			}
			else{
				holder.change.setTextColor(Color.BLACK);
				holder.changePercent.setTextColor(Color.BLACK);
			}
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView ticker,name,price,change,changePercent;
	}

}
