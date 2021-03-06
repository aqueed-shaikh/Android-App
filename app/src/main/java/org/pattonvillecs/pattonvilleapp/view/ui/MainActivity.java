/*
 * Copyright (C) 2017 - 2018 Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, Nathan Skelton, Pattonville School District
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.pattonvillecs.pattonvilleapp.view.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.pattonvillecs.pattonvilleapp.R;
import org.pattonvillecs.pattonvilleapp.intro.PattonvilleAppIntro;
import org.pattonvillecs.pattonvilleapp.preferences.PreferenceUtils;
import org.pattonvillecs.pattonvilleapp.preferences.SettingsActivity;
import org.pattonvillecs.pattonvilleapp.service.model.links.SchoolListType;
import org.pattonvillecs.pattonvilleapp.view.ui.about.AboutActivity;
import org.pattonvillecs.pattonvilleapp.view.ui.calendar.CalendarFragment;
import org.pattonvillecs.pattonvilleapp.view.ui.calendar.pinned.CalendarPinnedFragment;
import org.pattonvillecs.pattonvilleapp.view.ui.directory.DirectoryListFragment;
import org.pattonvillecs.pattonvilleapp.view.ui.home.HomeFragment;
import org.pattonvillecs.pattonvilleapp.view.ui.links.PowerschoolActivity;
import org.pattonvillecs.pattonvilleapp.view.ui.links.SchoolListActivity;
import org.pattonvillecs.pattonvilleapp.view.ui.news.NewsFragment;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Activity that handles navigation through each fragment and accessible activities
 *
 * @author Nathan Skelton
 */
public class MainActivity extends DaggerAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.PSD_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAppIntro();

        mTabLayout = findViewById(R.id.main_tab_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) { // First run
            mNavigationView.setCheckedItem(R.id.nav_home);
            this.onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.nav_home));
        }

        Log.d("SELECTED SCHOOLS", "These are selected: " + PreferenceUtils.getSelectedSchoolsSet(this));
    }

    private void checkAppIntro() {
        SharedPreferences sharedPreferences = PreferenceUtils.getSharedPreferences(this);

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = sharedPreferences.getBoolean(PreferenceUtils.APP_INTRO_FIRST_START_PREFERENCE_KEY, true);

        //  If the activity has never started before...
        if (isFirstStart) {
            Intent i = new Intent(this, PattonvilleAppIntro.class);

            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {

            // Determine if current fragment is a HomeFragment
            boolean shouldExit = getSupportFragmentManager()
                    .findFragmentById(R.id.content_default) instanceof HomeFragment;

            if (!shouldExit) {
                // If it isn't, switch to HomeFragment
                mNavigationView.getMenu().getItem(0).setChecked(true);
                replaceFragment(HomeFragment.newInstance());

            } else {
                // Else close the app
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = HomeFragment.newInstance();
                break;

            case R.id.nav_news:
                fragment = NewsFragment.newInstance();
                break;

            case R.id.nav_calendar:
                fragment = CalendarFragment.newInstance();
                break;

            case R.id.nav_directory:
                fragment = DirectoryListFragment.newInstance();
                break;

            case R.id.nav_nutrislice:
                launchNutrislice();
                break;

            case R.id.nav_peachjar:
                startActivity(SchoolListActivity.newInstance(this, SchoolListType.PEACHJAR));
                break;

            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.nav_powerschool:
                startActivity(new Intent(this, PowerschoolActivity.class));
                break;

            case R.id.nav_activities:
                launchWebsite("http://pirates.psdr3.org");
                break;

            case R.id.nav_psd:
                launchWebsite("http://www.psdr3.org");
                break;

            case R.id.nav_moodle:
                launchWebsite("http://moodle.psdr3.org");
                break;

            case R.id.nav_feedback:
                launchWebsite("https://goo.gl/forms/0ViHrODjYSDlz8BG3");
                break;

            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        if (fragment != null) {
            replaceFragment(fragment);
        }

        supportInvalidateOptionsMenu();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_default, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void launchNutrislice() {
        if (PreferenceUtils.getNutrisliceIntent(getApplicationContext())) {
            PackageManager packageManager = getPackageManager();
            String nutrisliceId = getString(R.string.package_name_nutrislice);

            // If app is installed, launch it
            Intent nutrisliceIntent = packageManager.getLaunchIntentForPackage(nutrisliceId);
            if (nutrisliceIntent != null) {
                startActivity(nutrisliceIntent);
            } else {
                // Else launch the store page
                launchWebsite("https://play.google.com/store/apps/details?id=" + nutrisliceId);
            }
        } else {
            startActivity(SchoolListActivity.newInstance(this, SchoolListType.NUTRISLICE));
        }
    }

    private void launchWebsite(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        //Handles proper nav drawer item clicked
        int id;
        if (fragment instanceof NewsFragment) id = R.id.nav_news;
        else if (fragment instanceof DirectoryListFragment) id = R.id.nav_directory;
        else if (fragment instanceof CalendarFragment) id = R.id.nav_calendar;
        else if (fragment instanceof CalendarPinnedFragment) id = R.id.nav_calendar;
        else return;
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setCheckedItem(id);
        }
    }
}
