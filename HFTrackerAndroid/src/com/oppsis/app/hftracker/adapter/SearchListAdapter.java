package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.model.FundHolding;
import com.oppsis.app.hftracker.pojo.FundLocalInfoManager;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.FundFuncUtils;

public class SearchListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundHolding> mSearchHoldings;
	
	public SearchListAdapter(Context context, List<FundHolding> holdings) {
		super();
		this.mContext = context;
		this.mSearchHoldings = holdings;

	}
	
	
	@Override
	public int getCount() {
		return mSearchHoldings.size();
	}

	@Override
	public Object getItem(int position) {
		return mSearchHoldings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundHolding searchHolding = mSearchHoldings.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.search_list_item, null);
			holder.port = (TextView)convertView.findViewById(R.id.search_item_port);
			holder.shares = (TextView)convertView.findViewById(R.id.search_item_shares);
			holder.activity = (TextView)convertView.findViewById(R.id.search_item_activity);
			holder.date = (TextView)convertView.findViewById(R.id.search_item_date);
			holder.pic = (ImageView)convertView.findViewById(R.id.search_pic);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.shares.setText(Constants.US_DECIMAL_FORMAT.format(searchHolding.getShares()));
		holder.port.setText(searchHolding.getPort() + Constants.PERCENTAGE_MARK);
		holder.date.setText(searchHolding.getDateStr().substring(2));
		holder.activity.setText(Html.fromHtml(FundFuncUtils.getActivityDesc(searchHolding.getAction(), searchHolding.getActivity())));
		//mImageLoader.loadImage(FundLocalInfoManager.getFundProperty(mContext, searchHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XH), holder.pic);
		Glide.with(mContext).load(FundLocalInfoManager.getFundProperty(mContext, searchHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XH))
			.placeholder(R.drawable.person_image_empty).into(holder.pic);
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView port,shares,activity,date;
		ImageView pic;
		//ImageButton imgBtn;
	}

}
