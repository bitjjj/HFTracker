package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;

public class AnalysisManagerListAdapter extends ArrayAdapter<String> {
	
	private List<String> mManagers;
	private Context mContext;
	private int mResId;
	
	public AnalysisManagerListAdapter(Context context, int resourceId,List<String> managers) {
		super(context, resourceId, managers);
		mManagers = managers;
		mContext = context;
		mResId = resourceId;

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String info = mManagers.get(position);
		
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			
			convertView = LayoutInflater.from(mContext).inflate(mResId, parent, false);
			holder.img = (ImageView)convertView.findViewById(R.id.manager_pic);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}

		//mImageLoader.loadImage(FundObject.getManagerPicInfo(info), holder.img);
		Glide.with(mContext).load(FundObject.getManagerPicInfo(info)).placeholder(R.drawable.person_image_empty).into(holder.img);
		return convertView;
	}
	
	public List<String> getSelectedManagers(){
		return mManagers;
	}
	
	static class ViewHolder{
		ImageView img;
	}
}
