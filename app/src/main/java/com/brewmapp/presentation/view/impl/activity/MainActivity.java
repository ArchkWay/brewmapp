package com.brewmapp.presentation.view.impl.activity;

import android.annotation.SuppressLint;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.presentation.support.navigation.FragmentInterractor;
import com.brewmapp.presentation.view.impl.fragment.BeerMapFragment;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import io.paperdb.Paper;
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

import com.transitionseverywhere.TransitionManager;

import ru.frosteye.ovsa.presentation.navigation.impl.SimpleNavAction;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_MAP_RESULT;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_STATE;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_SEARCH_CODE;

public class MainActivity extends BaseActivity implements MainView, FlexibleAdapter.OnItemClickListener,
        FragmentInterractor {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.common_toolbar_dropdown) LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle) TextView toolbarSubTitle;
    @BindView(R.id.activity_main_drawer) DuoDrawerLayout drawer;
    @BindView(R.id.activity_main_menu) RecyclerView menu;
    @BindView(R.id.activity_main_userName) TextView userName;
    @BindView(R.id.activity_main_text_view_check_connection) TextView check_connection;
    @BindView(R.id.activity_main_avatar) ImageView avatar;
    @BindView(R.id.activity_main_profileHeader) View profileHeader;
    @BindView(R.id.activity_main_container) FrameLayout container;
    @BindView(R.id.activity_main_visible_container)    RelativeLayout visible_container;

    @Inject MainPresenter presenter;
    @Inject MainNavigator navigator;

    private FlexibleAdapter<MenuField> adapter;
    private List<MenuField> menuItems;
    private @MenuRes int menuToShow;

    public static final String KEY_FIRST_FRAGMENT = "first_fragment";
    public static final String MODE_DEFAULT = "default";
    public static final String MODE_ONLY_EVENT_FRAGMENT = "event_fragment";
    public static final String MODE_ONLY_MAP_FRAGMENT = "map_fragment";
    public static final String SEARCH_FRAGMENT = "search_fragment";

    DuoDrawerToggle drawerToggle;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //getIntent().getData().getPath()
        processShowDrawer(false,true);
        Paper.init(this);
        Paper.book().destroy();
    }

    @Override
    protected void initView() {
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarDropdown.setGravity(Gravity.CENTER_HORIZONTAL);
        mode = presenter.parseMode(getIntent());
   }

    private void setDrawer() {
        drawer.setMarginFactor(0.5f);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_menu_toggle)).getBitmap();
        drawerToggle = new CustomDuoDrawerToggle(this, toolbar, drawer,
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
    public void showFragment(BaseFragment fragment) {
        if (fragment instanceof SearchFragment) {
            mode = SEARCH_FRAGMENT;
        } else {
            mode = presenter.parseMode(getIntent());
        }
        fragment.setArguments(presenter.prepareArguments(getIntent(),container));
        menuToShow = fragment.getMenuToInflate();
        invalidateOptionsMenu();
        processTitleDropDown(fragment, 0);
        if(menuToShow == 0) processSetActionBar(0);
        navigator.setActionBarItemDelegate(fragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, fragment)
                .commit();
    }

    @Override
    public void showFilterFragment(BaseFragment searchFragment) {
        navigator.setActionBarItemDelegate(searchFragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, searchFragment)
                .addToBackStack(SearchFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void processTitleDropDown(BaseFragment baseFragment, int selected) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;

        if (baseFragment.getTitleDropDown() != null && !baseFragment.getTitleDropDown().isEmpty()) {
        actionBar.setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(baseFragment.getTitle());
        if (baseFragment.getTitleDropDown() != null && !baseFragment.getTitleDropDown().isEmpty()) {
            toolbarSubTitle.setVisibility(View.VISIBLE);
            if(mode.equals(MODE_DEFAULT)) {
                if (baseFragment instanceof View.OnClickListener) {
                    toolbarSubTitle.setOnClickListener(((View.OnClickListener) baseFragment));
                }
                toolbarSubTitle.setVisibility(View.VISIBLE);
            }else {
                toolbarSubTitle.setVisibility(View.GONE);
            }
        } else {
            toolbarSubTitle.setVisibility(View.GONE);
            }
        } else {
            toolbarSubTitle.setVisibility(View.GONE);
        }

        toolbarTitle.setText(baseFragment.getTitle());
    }

    @Override
    public void processSpinnerTitleSubtitle(String subtitle) {
        toolbarSubTitle.setText(getString(R.string.arrow_down, subtitle));
    }

    @Override
    public void processStartActivityWithRefresh(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void processSetActionBar(int position) {
        if (position == 2) {
            menuToShow = R.menu.search_add;
        } else if (position == Actions.ACTION_FILTER) {
            menuToShow = R.menu.filter;
        } else {
            menuToShow = R.menu.search;
        }

        invalidateOptionsMenu();
    }

    @Override
    public void processSetFilterFragmentActionBar(SearchFragment searchFragment) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        toolbarTitle.setText(searchFragment.getTitle());
    }

    @Override
    public synchronized void processShowDrawer(boolean show, boolean smooth) {
        if(smooth) {
            TransitionManager.getDefaultTransition().setDuration(250);
            TransitionManager.beginDelayedTransition(drawer);
        }
        container.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        container.setVisibility(show?View.VISIBLE:View.INVISIBLE);
        toolbar.setVisibility(show?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showDrawer(boolean shown) {
        if(shown)
            drawer.openDrawer();
        else
            drawer.post(() -> {
                drawer.closeDrawer();
                processShowDrawer(false,true);
            });
    }

    @Override
    public void successCheckEnvironment(User user, List<MenuField> fields) {
        check_connection.setVisibility(View.GONE);
        Picasso.with(this).load(user.getThumbnail()).fit().into(avatar);
        userName.setText(user.getFormattedName());
        this.menuItems = fields;
        adapter = new FlexibleAdapter<>(fields, this);
        menu.setLayoutManager(new LinearLayoutManager(this));
        menu.setAdapter(adapter);

        switch (mode){
            case MODE_DEFAULT:
                setDrawer();
                break;
            case MODE_ONLY_EVENT_FRAGMENT:
                enableBackButton();
                navigator.storeCodeActiveFragment(MenuField.EVENTS);
                navigator.storeCodeTebEventFragment(
                        getIntent().getIntExtra(RequestCodes.INTENT_EXTRAS,EventsFragment.TAB_EVENT)
                );
                setResult(RESULT_OK);
                break;
            case MODE_ONLY_MAP_FRAGMENT:
                enableBackButton();
                navigator.storeCodeActiveFragment(MenuField.MAP);
                setResult(RESULT_OK);
                break;
        }

        navigator.onAttachView(this);
        navigator.onNavigatorAction(new SimpleNavAction(presenter.getActiveFragment()));
        navigator.onDrawerClosed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mode){
            case MODE_DEFAULT:
                if(menuToShow != 0)  getMenuInflater().inflate(menuToShow, menu);
                break;
            case MODE_ONLY_EVENT_FRAGMENT:
            case MODE_ONLY_MAP_FRAGMENT:
                if (menu!=null) menu.clear();
                getMenuInflater().inflate(R.menu.stub, menu);
                break;
            case SEARCH_FRAGMENT:
                if (menu!=null) menu.clear();
                getMenuInflater().inflate(R.menu.stub, menu);
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            default:
              return navigator.onOptionsItemSelected(item);
        }
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_REFRESH_ITEMS) {
                refreshItems();
            } else if (requestCode == REQUEST_CODE_REFRESH_STATE) {
                refreshState();
            } else if (requestCode == REQUEST_CODE_MAP_RESULT) {
                showMapResult(data.getBooleanExtra("isBeer", false),
                        data.getIntExtra("checkBox", 0));
            } else if (requestCode == REQUEST_SEARCH_CODE) {
                showSearchFilter(data);
            }
        }
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @SuppressLint("RestrictedApi")
    public void refreshState() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof EventsFragment)
                ((EventsFragment) fragment).refreshState();
            else if(fragment instanceof ProfileFragment)
                ((ProfileFragment) fragment).refreshState();
    }
    @SuppressLint("RestrictedApi")
    public void refreshItems() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof EventsFragment)
                ((EventsFragment) fragment).refreshItems(false);
    }
    @SuppressLint("RestrictedApi")
    public void showResultOnMap() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if(fragment instanceof ProfileFragment)
                ((ProfileFragment) fragment).refreshItems();
    }
    @SuppressLint("RestrictedApi")
    public void showMapResult(boolean isBeer, int checkBox) {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof BeerMapFragment) {
                ((BeerMapFragment) fragment).showResult(isBeer, checkBox);
            }
    }

    @SuppressLint("RestrictedApi")
    public void showSearchFilter(Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            if (fragment instanceof SearchFragment) {
                ((SearchFragment) fragment).showResult(data);
            }
    }

//    @Override
//    public void onBackPressed() {
//        Log.i("size", String.valueOf(getSupportFragmentManager().getFragments().size()));
//        if (getSupportFragmentManager().getFragments().size() > 1) {
//            for (Fragment fragment : getSupportFragmentManager().getFragments())
//                if (fragment instanceof SearchFragment) {
//                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                }
//        } else {
//            super.onBackPressed();
//        }
//    }
}
