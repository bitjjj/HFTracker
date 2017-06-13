package com.oppsis.app.hftracker.adapter;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.util.I18nTextUtils;

public class ManagerListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundObject> mHoldings;
	private Set<String> mSelectedManagers;
	
	public ManagerListAdapter(Context context, List<FundObject> holdings) {
		super();
		this.mContext = context;
		this.mHoldings = holdings;

	}
	
	public ManagerListAdapter(Context context, List<FundObject> holdings,Set<String> selectedManagers) {
		this(context, holdings);
		
		this.mSelectedManagers = selectedManagers;
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
	
	public Set<String> getSelectedManagers(){
		return mSelectedManagers;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundObject fundObject = mHoldings.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_selector_list_item, null);
			
			holder.fundName = (TextView)convertView.findViewById(R.id.manager_item_fund_name);
			holder.name = (TextView)convertView.findViewById(R.id.manager_item_name);
			holder.managerPic = (ImageView)convertView.findViewById(R.id.manager_pic);
			holder.cb = (CheckBox)convertView.findViewById(R.id.manager_item_cb);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.cb.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) { 
		    	 CheckBox cb = (CheckBox) v;
		    	 if(cb.isChecked()){
		    		 mSelectedManagers.add(fundObject.getManagerInfo());
		    	 }
		    	 else{
		    		 mSelectedManagers.remove(fundObject.getManagerInfo());
		    	 }
		     }
		}); 
		
		holder.name.setText(I18nTextUtils.getManagerName(mContext, fundObject));
		holder.fundName.setText(I18nTextUtils.getFundName(mContext, fundObject));
		
		//mImageLoader.loadImage(fundObject.getManagerIconPicXXH(), holder.managerPic);
		Glide.with(mContext).load(fundObject.getManagerIconPicXXH()).placeholder(R.drawable.person_image_empty).into(holder.managerPic);
		
		if(mSelectedManagers.contains(fundObject.getManagerInfo())){
			holder.cb.setChecked(true);
		}
		else{
			holder.cb.setChecked(false);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView name,fundName;
		ImageView managerPic;
		CheckBox cb;
	}

}
