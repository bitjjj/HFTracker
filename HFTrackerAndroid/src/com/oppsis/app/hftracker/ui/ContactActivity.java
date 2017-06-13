package com.oppsis.app.hftracker.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.baoyz.actionsheet.ActionSheet;
import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;
import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.ui.base.BaseSwipeAndActionBarActivity;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.hftracker.util.IntentUtil;
import com.umeng.fb.FeedbackAgent;

@EActivity(R.layout.activity_contact)
public class ContactActivity extends BaseSwipeAndActionBarActivity {

	@ViewById(R.id.contact_table_view)
	protected UITableView tableView;

	@AfterViews
	public void initUI() {
		super.initUI(getString(R.string.contactus_label));

		tableView.setClickListener(new ItemClickListener());
		tableView.addBasicItem(getString(R.string.contact_us_info),
				getString(R.string.contact_us_info_desc));
		tableView.addBasicItem(getString(R.string.contact_us_rate),
				getString(R.string.contact_us_rate_desc));
		tableView.addBasicItem(getString(R.string.contact_us_business),
				getString(R.string.contact_us_business_desc));
		tableView.addBasicItem(getString(R.string.contact_us_suggestion),
				getString(R.string.contact_us_suggestion_desc));
		tableView.commit();
	}

	private class ItemClickListener implements ClickListener {

		@Override
		public void onClick(int index) {
			if (index == 0) {
				showContactUS();
			} else if (index == 1) {
				IntentUtil.openLink(Constants.APP_LINK,ContactActivity.this);
			} else if (index == 2) {
				showBusinessDlg();
			}else if(index == 3){
				FeedbackAgent agent = new FeedbackAgent(ContactActivity.this);
			    agent.startFeedbackActivity();
			}

		}

	}

	private void showContactUS() {
		setTheme(R.style.ActionSheetStyleIOS7);
		ActionSheet
				.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle(getString(R.string.contact_us_i_know_label))
				.setOtherButtonTitles(getString(R.string.contact_us_qq),getString(R.string.contact_us_email))
				.setListener(new ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet,boolean isCancel) {}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet,int index) {
						switch (index) {
						case 0:// qq
							IntentUtil.openLink(getString(R.string.contact_us_qq_link),ContactActivity.this);
							break;
						case 1:// email
							IntentUtil.openEmail(getString(R.string.contact_us_email_address),ContactActivity.this);
							break;
						}
					}

				}).setCancelableOnTouchOutside(true).show();
	}

	private void showBusinessDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.contact_us_business));
		builder.setMessage(getString(R.string.contact_us_business_detail));
		
		builder.setNeutralButton(getString(R.string.sure_label),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		builder.show();
	}

}
