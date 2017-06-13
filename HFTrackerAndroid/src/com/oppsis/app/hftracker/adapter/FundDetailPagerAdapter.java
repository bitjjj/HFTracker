package com.oppsis.app.hftracker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oppsis.app.hftracker.util.Constants;

public class FundDetailPagerAdapter extends FragmentPagerAdapter{
	private Context ctx;

	public FundDetailPagerAdapter(FragmentManager fm,Context ctx) {
		super(fm);
		this.ctx = ctx;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return ctx.getString(Constants.FUND_DETAIL_FRAGMENT_TITLE_IDS[position]);
	}

	@Override
	public int getCount() {
		return Constants.FUND_DETAIL_FRAGMENT_NAMES.length;
	}

	@Override
	public Fragment getItem(int position) {

		return Fragment.instantiate(ctx,Constants.FUND_DETAIL_FRAGMENT_NAMES[position]);

	}
}
