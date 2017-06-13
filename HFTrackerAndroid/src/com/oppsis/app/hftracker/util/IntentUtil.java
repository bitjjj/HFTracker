package com.oppsis.app.hftracker.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.oppsis.app.hftracker.R;

public class IntentUtil {

	public static void openLink(String link,Context context){
		Uri uri = Uri.parse(link);   
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);    
        context.startActivity(intent);    
	}
	
	public static void openEmail(String emailAddress,Context context){
		Intent intent = new Intent(android.content.Intent.ACTION_SEND); 
		intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {emailAddress});
		intent.setType("*/*"); 
		context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email_label)));
	}
	
	public static void openSetting(Context context){
		Intent intent = new Intent(Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}
	
	public static Intent getShareIntent(String text,Uri uri){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

	    shareIntent.setType("*/*");
	    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
	    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
	    return shareIntent;
	}
	
	/*public static Intent getShareIntent(Context context,String text,int resId){
		return getShareIntent(text,getDrawableUri(context, resId));
	}*/
	
	public static Intent getShareIntent(String text){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		shareIntent.setType("text/plain");
	    return shareIntent;
	}
	
	public static Intent getShareIntent(Uri uri){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
	    return shareIntent;
	}
	
	/*public static Intent getShareIntent(Context context,int resId){
		return getShareIntent(getDrawableUri(context, resId));
	}*/
	
	public static void shareTextAndImg(Context context,String text,Uri uri){
	    context.startActivity(Intent.createChooser(getShareIntent(text, uri), context.getResources().getText(R.string.share_label)));
	}
	
	/*public static void shareTextAndImg(Context context,String text,int resId){
		context.startActivity(Intent.createChooser(getShareIntent(text, getDrawableUri(context,resId)), context.getResources().getText(R.string.share_label)));
	}*/
	
	public static void shareText(Context context,String text){
		
		context.startActivity(Intent.createChooser(getShareIntent(text), context.getResources().getText(R.string.share_label)));
	}
	
	public static void shareImg(Context context,Uri uri){
	    context.startActivity(Intent.createChooser(getShareIntent(uri), context.getResources().getText(R.string.share_label)));
	}
	
	/*public static void shareImg(Context context,int resId){
		context.startActivity(Intent.createChooser(getShareIntent(getDrawableUri(context,resId)), context.getResources().getText(R.string.share_label)));
	}*/
	
	/*public static Uri getDrawableUri(Context context,int resId){
		return Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
	}*/
	
}
