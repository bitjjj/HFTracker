package com.oppsis.app.hftracker.adapter;

import java.util.Date;
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
import com.oppsis.app.hftracker.model.FundNews;
import com.oppsis.app.util.DateUtils;

public class FundNewsListAdapter extends BaseAdapter{

	private Context mContext;
	private List<FundNews> mNews;
	
	public FundNewsListAdapter(Context context, List<FundNews> holdings) {
		super();
		this.mContext = context;
		this.mNews = holdings;

	}
	
	
	@Override
	public int getCount() {
		return mNews.size();
	}

	@Override
	public Object getItem(int position) {
		return mNews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundNews fundNews = mNews.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.fund_detail_news_list_item, null);
			holder.title = (TextView)convertView.findViewById(R.id.fund_news_title);
			holder.date = (TextView)convertView.findViewById(R.id.fund_news_date);
			holder.pic = (ImageView)convertView.findViewById(R.id.fund_news_pic);
			
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		holder.title.setText(Html.fromHtml(fundNews.getTitle()));
		holder.date.setText(DateUtils.format(new Date(Long.valueOf(fundNews.getTimeMark())), DateUtils.TYPE_DATE));
		
		//mImageLoader.loadImage(fundNews.getPicUrl(), holder.pic);
		Glide.with(mContext).load(fundNews.getPicUrl()).placeholder(R.drawable.placeholder).into(holder.pic);

		return convertView;
	}
	
	static class ViewHolder{
		TextView title,date;
		ImageView pic;
	}

}
