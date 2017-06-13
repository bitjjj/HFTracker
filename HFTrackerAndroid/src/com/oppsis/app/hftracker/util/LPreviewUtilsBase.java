/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oppsis.app.hftracker.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oppsis.app.hftracker.R;

public class LPreviewUtilsBase {
    protected Activity mActivity;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBarDrawerToggleWrapper mDrawerToggleWrapper;

    LPreviewUtilsBase(Activity activity) {
        mActivity = activity;
    }

    public ActionBarDrawerToggleWrapper setupDrawerToggle(DrawerLayout drawerLayout,
            final DrawerLayout.DrawerListener drawerListener) {
        mDrawerToggle = new ActionBarDrawerToggle(mActivity, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerListener.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerListener.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                drawerListener.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawerListener.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggleWrapper = new ActionBarDrawerToggleWrapper();
        return mDrawerToggleWrapper;
    }

    public void trySetActionBar() {
        // Do nothing pre-L
    }

    public boolean hasLPreviewAPIs() {
        return false;
    }

    public boolean shouldChangeActionBarForDrawer() {
        return true;
    }

    public void showHideActionBarIfPartOfDecor(boolean show) {
        // pre-L, action bar is always part of the window decor
        if (show) {
            mActivity.getActionBar().show();
        } else {
            mActivity.getActionBar().hide();
        }
    }

    public void setMediumTypeface(TextView textView) {
        textView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    }

    public class ActionBarDrawerToggleWrapper {
        public void syncState() {
            if (mDrawerToggle != null) {
                mDrawerToggle.syncState();
            }
        }

        public void onConfigurationChanged(Configuration newConfig) {
            if (mDrawerToggle != null) {
                mDrawerToggle.onConfigurationChanged(newConfig);
            }
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            if (mDrawerToggle != null) {
                return mDrawerToggle.onOptionsItemSelected(item);
            }
            return false;
        }
    }

    public void startActivityWithTransition(Intent intent, View clickedView,
            String sharedElementName) {
        mActivity.startActivity(intent);
    }

    public void setViewName(View v, String viewName) {
        // Can't do this pre-L
    }

    public void postponeEnterTransition() {
        // Can't do this pre-L
    }

    public void startPostponedEnterTransition() {
        // Can't do this pre-L
    }

    public int getStatusBarColor() {
        // On pre-L devices, you can have any status bar color so long as it's black.
        return Color.BLACK;
    }

    public void setStatusBarColor(int color) {
        // Only black.
    }

    public void setViewElevation(View v, float elevation) {
        // Can't do this pre-L
    }

    
}