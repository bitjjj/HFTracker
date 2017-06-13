package com.oppsis.app.hftracker.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilterCallback;

public class OldFilterDialogFragment extends DialogFragment implements
		FundFilterCallback {
	private FilterPanelView mFilterPanelView;
	private int mTabIndex;
	private FundObject mFund;

	public static OldFilterDialogFragment newInstance(FundObject fund, int tabIndex) {
		OldFilterDialogFragment f = new OldFilterDialogFragment();

		Bundle args = new Bundle();
		args.putInt("tabIndex", tabIndex);
		args.putParcelable("fund", fund);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTabIndex = getArguments().getInt("tabIndex");
		mFund = getArguments().getParcelable("fund");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_filter_dialog, container);
		mFilterPanelView = (FilterPanelView) view.findViewById(R.id.navdrawer);

		mFilterPanelView.initUI(mFund, mTabIndex);
		mFilterPanelView.setFundFilterCallback(this);

		getDialog().setTitle(getString(R.string.filter_dialog_title));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}

		getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.filter_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void filter(FundFilter setting) {
		
		this.dismiss();
	}
}
