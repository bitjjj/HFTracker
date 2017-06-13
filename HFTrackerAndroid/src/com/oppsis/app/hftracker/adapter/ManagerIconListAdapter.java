package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.IndustryChartActivity_;
import com.oppsis.app.hftracker.ui.NewsListDetailActivity_;
import com.oppsis.app.hftracker.ui.PortValueChartActivity_;
import com.oppsis.app.hftracker.util.I18nTextUtils;

public class ManagerIconListAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<FundObject> mItems = null;

	public ManagerIconListAdapter(Context context, List<FundObject> data) {
		this.mContext = context;
		mItems = data;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FundObject fundObject = mItems.get(position);
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.manager_icon_list_item, null);
			
			holder.iconImg = (ImageView)convertView.findViewById(R.id.manager_icon_item_pic);
			holder.menuImg = (ImageView)convertView.findViewById(R.id.manager_icon_item_menu);
			holder.title = (TextView)convertView.findViewById(R.id.manager_icon_item_title);
			holder.subTitle = (TextView)convertView.findViewById(R.id.manager_icon_item_subtitle);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.title.setText(I18nTextUtils.getManagerName(mContext, fundObject));
		holder.subTitle.setText(I18nTextUtils.getFundName(mContext, fundObject));
		
		Glide.with(mContext).load(fundObject.getManagerIconPicXH()).placeholder(R.drawable.person_image_empty).into(holder.iconImg);
		
		holder.menuImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(mContext, v);
               
                popup.getMenuInflater()
                    .inflate(R.menu.popup_menu_manager_icon, popup.getMenu());
                
                if(!mContext.getResources().getBoolean(R.bool.show_news_popup_menu)){
                	popup.getMenu().findItem(R.id.menu_item_news).setVisible(false);
                }
                
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                    	switch (item.getItemId()) {
						case R.id.menu_item_port_value:
							PortValueChartActivity_.intent(mContext).fund(fundObject).start();
							return true;
						case R.id.menu_item_industry:
							IndustryChartActivity_.intent(mContext).fund(fundObject).start();
							return true;
						case R.id.menu_item_news:
							NewsListDetailActivity_.intent(mContext).fund(fundObject).start();	
							return true;
						default:
							break;
						}
                        return false;
                    }
                });

                popup.show(); 
				
			}
		});
		
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView iconImg,menuImg;
		TextView title,subTitle;
	}

}
