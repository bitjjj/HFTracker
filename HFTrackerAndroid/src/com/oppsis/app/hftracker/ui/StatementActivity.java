package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.text.Html;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;

@EActivity(R.layout.activity_statement)
public class StatementActivity extends BaseSwipeAndActionBarActivity{
	@ViewById(R.id.statement_text)
	protected TextView statementTextView;
	
	@AfterViews
	public void initUI() {
		
		super.initUI(getString(R.string.notice_label));
		
		statementTextView.setText(Html.fromHtml(getString(R.string.statement)));
	}
}
