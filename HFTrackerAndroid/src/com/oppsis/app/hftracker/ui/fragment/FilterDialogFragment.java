package com.oppsis.app.hftracker.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.event.ChangeFundFilterEvent;
import com.oppsis.app.hftracker.event.OttoBus;
import com.oppsis.app.hftracker.pojo.FundObject;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilter;
import com.oppsis.app.hftracker.ui.widget.FilterPanelView.FundFilterCallback;

@EFragment(R.layout.fragment_filter_dialog)
public class FilterDialogFragment extends DialogFragment implements
		FundFilterCallback {
	
	@ViewById(R.id.navdrawer)protected FilterPanelView mFilterPanelView;
	@FragmentArg protected int tabIndex;
	@FragmentArg protected FundObject fund;
	@Bean protected OttoBus mBus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@AfterViews
	protected void initUI(){
		mFilterPanelView.initUI(fund, tabIndex);
		mFilterPanelView.adjustUISection(tabIndex);
		mFilterPanelView.setFundFilterCallback(this);

		getDialog().setTitle(getString(R.string.filter_dialog_title));
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
		mBus.post(new ChangeFundFilterEvent(setting));
		this.dismiss();
	}
}
