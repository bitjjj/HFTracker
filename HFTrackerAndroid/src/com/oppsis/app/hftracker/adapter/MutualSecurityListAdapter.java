package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.content.Context;
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

public class MutualSecurityListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundHolding> mMutualHoldings;
	
	public MutualSecurityListAdapter(Context context, List<FundHolding> holdings) {
		super();
		this.mContext = context;
		this.mMutualHoldings = holdings;

	}
	
	
	@Override
	public int getCount() {
		return mMutualHoldings.size();
	}

	@Override
	public Object getItem(int position) {
		return mMutualHoldings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundHolding mutualHolding = mMutualHoldings.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.mutual_security_list_item, null);
			holder.port = (TextView)convertView.findViewById(R.id.mutual_security_item_port);
			holder.shares = (TextView)convertView.findViewById(R.id.mutual_security_item_shares);
			holder.pic = (ImageView)convertView.findViewById(R.id.mutual_security_pic);
			//holder.imgBtn = (ImageButton)convertView.findViewById(R.id.mutual_security_expandable_button);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.shares.setText(Constants.US_DECIMAL_FORMAT.format(mutualHolding.getShares()));
		holder.port.setText(mutualHolding.getPort() + Constants.PERCENTAGE_MARK);
		//mImageLoader.loadImage(FundLocalInfoManager.getFundProperty(mContext, mutualHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XH), holder.pic);
		Glide.with(mContext).load(FundLocalInfoManager.getFundProperty(mContext, mutualHolding.getFund_name(), FundObject.PROPERTY_MANAGER_ICON_PIC_XH))
			.placeholder(R.drawable.person_image_empty)
			.into(holder.pic);

		
		return convertView;
	}
	
	static class ViewHolder{
		TextView port,shares;
		ImageView pic;
		//ImageButton imgBtn;
	}

}
