package com.brewmapp.presentation.view.impl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.presentation.support.navigation.FragmentInterractor;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.CustomDrawerArrowDrawable;
import nl.psdcompany.duonavigationdrawer.widgets.CustomDuoDrawerToggle;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.support.navigation.MainNavigator;
import com.brewmapp.presentation.view.contract.MainView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;
import ru.frosteye.ovsa.presentation.navigation.impl.SimpleNavAction;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.utils.Cons.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.utils.Cons.REQUEST_CODE_REFRESH_STATE;

public class MainActivity extends BaseActivity implements MainView, FlexibleAdapter.OnItemClickListener,
        FragmentInterractor {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    //    @BindView(R.id.common_toolbar_spinner) Spinner toolbarSpinner;
    @BindView(R.id.common_toolbar_dropdown) View toolbarDropdown;
    @BindView(R.id.common_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle) TextView toolbarSubTitle;
    @BindView(R.id.activity_main_drawer) DuoDrawerLayout drawer;
    @BindView(R.id.activity_main_menu) RecyclerView menu;
    @BindView(R.id.activity_main_userName) TextView userName;
    @BindView(R.id.activity_main_avatar) ImageView avatar;
    @BindView(R.id.activity_main_profileHeader) View profileHeader;

    @Inject MainPresenter presenter;
    @Inject MainNavigator navigator;

    private FlexibleAdapter<MenuField> adapter;
    private List<MenuField> menuItems;
    private List<String> dropdownItems;
    private @MenuRes int menuToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        drawer.setMarginFactor(0.5f);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_menu_toggle)).getBitmap();
        DuoDrawerToggle drawerToggle = new CustomDuoDrawerToggle(this, toolbar, drawer,
                new CustomDrawerArrowDrawable(getResources(), bitmap),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close, navigator::onDrawerClosed);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        profileHeader.setOnClickListener(v -> {
            MenuField.unselectAll(menuItems);
            adapter.notifyDataSetChanged();
            navigator.onNavigatorAction(new SimpleNavAction(MenuField.PROFILE));
        });
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        navigator.onAttachView(this);
        navigator.onNavigatorAction(new SimpleNavAction(presenter.getActiveFragment()));
        navigator.onDrawerClosed();

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void showUser(User user) {
        Picasso.with(this).load(user.getThumbnail()).fit().into(avatar);
        userName.setText(user.getFormattedName());
    }

    @Override
    public void showMenuItems(List<MenuField> fields) {
        this.menuItems = fields;
        adapter = new FlexibleAdapter<>(fields, this);
        menu.setLayoutManager(new LinearLayoutManager(this));
        menu.setAdapter(adapter);
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        menuToShow = fragment.getMenuToInflate();
        invalidateOptionsMenu();
        processTitleDropDown(fragment, 0);
        processSetActionBar(0);
        navigator.setActionBarItemDelegate(fragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, fragment)
                .commit();
    }

    @Override
    public void processTitleDropDown(BaseFragment baseFragment, int selected) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        if(baseFragment.getTitleDropDown() != null && !baseFragment.getTitleDropDown().isEmpty()) {
            dropdownItems = baseFragment.getTitleDropDown();
            toolbarDropdown.setVisibility(View.VISIBLE);
            actionBar.setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(baseFragment.getTitle());
            if(baseFragment instanceof View.OnClickListener) {
                toolbarSubTitle.setOnClickListener(((View.OnClickListener) baseFragment));
            }
        } else {
            toolbarDropdown.setVisibility(View.GONE);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void processSpinnerTitleSubtitle(String subtitle) {
        toolbarSubTitle.setText(getString(R.string.arrow_down, subtitle));
    }

    @Override
    public void processStartActivityWithRefresh(Intent intent) {
        startActivityForResult(intent,REQUEST_CODE_REFRESH_ITEMS);
    }

    @Override
    public void processSetActionBar(int position) {
        if (position == 2) {
            menuToShow = R.menu.search_add;
        } else if (position == Actions.ACTION_FILTER) {
            menuToShow = R.menu.filter;
        } else
            menuToShow = R.menu.search;

        invalidateOptionsMenu();
    }

    @Override
    public void showDrawer(boolean shown) {
        if(shown) {
            drawer.openDrawer();
        } else {
            drawer.closeDrawer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(menuToShow != 0) {
            getMenuInflater().inflate(menuToShow, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return navigator.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(int position) {
        MenuField field = adapter.getItem(position);
        if(field.getId() == MenuField.LOGOUT) {
            presenter.onLogout();
            startActivityAndClearTask(StartActivity.class);
            finish();
            return true;
        }
        MenuField.unselectAll(menuItems);
        field.setSelected(true);
        adapter.notifyDataSetChanged();
        navigator.onMenuItemSelected(field);
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REFRESH_ITEMS) {
            if (resultCode == RESULT_OK) {
                refreshItems();
            }
        }else if(requestCode == REQUEST_CODE_REFRESH_STATE){
            if(requestCode==RESULT_OK){
                refreshState();
            }
        }
    }

    public void refreshState() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof EventsFragment)
                ((EventsFragment) fragment).refreshState();
    }

    public void refreshItems() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof EventsFragment)
                ((EventsFragment) fragment).refreshItems(false);
    }
}
