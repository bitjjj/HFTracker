package com.oppsis.app.hftracker.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.FundDetailActivity_;
import com.oppsis.app.hftracker.util.I18nTextUtils;
import com.oppsis.app.hftracker.util.UIUtils;

public class FundListAdapter extends BaseAdapter {

	private int mGridPadding;
	private Context mContext;
	private List<FundObject[]> mItems = null;
	private LayoutInflater mInflater;

	public FundListAdapter(Context context, List<FundObject[]> data) {
		this.mContext = context;
		mItems = data;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mGridPadding = (int)mContext.getResources().getDimension(R.dimen.explore_grid_padding);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FundObject[] items = mItems.get(position);
		ViewHolder[] holders;

		if (convertView == null) {
			LinearLayout ll = new LinearLayout(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setLayoutParams(params);

			holders = new ViewHolder[items.length];

			for (int i = 0; i < items.length; i++) {
				View view = null;

				if (isHero(items)) {
					view = mInflater.inflate(R.layout.hf_intro_hero_list_item,ll, false);
				} else {
					view = mInflater.inflate(R.layout.hf_intro_list_item, ll,false);
				}
				LinearLayout.LayoutParams thisViewParams = setupLayoutParams(view);

				holders[i] = new ViewHolder();
				holders[i].view = view;
				holders[i].image = (ImageView) view.findViewById(R.id.hf_intro_photo);
				holders[i].title = (TextView) view.findViewById(R.id.hf_intro_title);
				holders[i].subTitle = (TextView) view.findViewById(R.id.hf_intro_subtitle);

				if (items[i].getColor() != null
						&& !"".equals(items[i].getColor())) {
					holders[i].image.setColorFilter(UIUtils.setColorAlpha(Integer.valueOf(items[i].getColor()),UIUtils.SESSION_PHOTO_SCRIM_ALPHA));
				}

				if (isHero(items)) {
					//mImageLoader.loadImage(items[i].getManagerCoverPicXXH(), holders[i].image);
					Glide.with(mContext).load(items[i].getManagerCoverPicXXH()).placeholder(R.drawable.placeholder).into(holders[i].image);
				}
				else{
					//mImageLoader.loadImage(items[i].getManagerCoverPicH(), holders[i].image);
					Glide.with(mContext).load(items[i].getManagerCoverPicH()).placeholder(R.drawable.placeholder).into(holders[i].image);
				}
				holders[i].title.setText(I18nTextUtils.getManagerName(mContext,items[i]));
				holders[i].subTitle.setText(I18nTextUtils.getFundName(mContext,items[i]));

				holders[i].view.setOnClickListener(new FundInfoClickListener(items[i]));

				ll.addView(view, thisViewParams);
			}

			convertView = ll;
			convertView.setTag(holders);
		} else {
			holders = (ViewHolder[]) convertView.getTag();
			for (int i = 0; i < holders.length; i++) {

				//mImageLoader.loadImage(items[i].getManagerCoverPicH(), holders[i].image);
				Glide.with(mContext).load(items[i].getManagerCoverPicH()).placeholder(R.drawable.placeholder).into(holders[i].image);
				holders[i].title.setText(I18nTextUtils.getManagerName(mContext,items[i]));
				holders[i].subTitle.setText(I18nTextUtils.getFundName(mContext,items[i]));
				
				holders[i].view.setOnClickListener(new FundInfoClickListener(items[i]));
			}

		}

		return convertView;
	}

	private LinearLayout.LayoutParams setupLayoutParams(View view) {
		LinearLayout.LayoutParams viewLayoutParams;
		if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			viewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
		} else {
			viewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
		
		viewLayoutParams.leftMargin = mGridPadding/2;
		viewLayoutParams.rightMargin = mGridPadding/2;
		viewLayoutParams.bottomMargin = mGridPadding;
		viewLayoutParams.width = LayoutParams.MATCH_PARENT;
		viewLayoutParams.weight = 1.0f;
		view.setLayoutParams(viewLayoutParams);
		return viewLayoutParams;
	}
	
	private boolean isHero(FundObject[] items){
		return items.length == 1 || (items[0].getHero()!= null && items[0].getHero().equals(FundObject.HERO));
	}

	@Override
	public int getItemViewType(int position) {
		FundObject[] item = mItems.get(position);
		return item.length == 1 ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
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
	
	static class ViewHolder {
		View view;
		ImageView image;
		TextView title;
		TextView subTitle;
	}
	
	public class FundInfoClickListener implements OnClickListener{

		private FundObject mFund;
		
		public FundInfoClickListener(FundObject fund){
			mFund = fund;
		}
		
		@Override
		public void onClick(View v) {
			Log.d(this.toString(), mFund.getManagerName() +" " +mFund.getFundName());
			FundDetailActivity_.intent(mContext).fund(mFund).start();
			((Activity)mContext).overridePendingTransition(R.anim.push_left_in,R.anim.no_anim);
		}
		
	}

}
