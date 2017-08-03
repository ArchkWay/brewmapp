package ru.frosteye.beermap.presentation.view.impl.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.CustomDrawerArrowDrawable;
import nl.psdcompany.duonavigationdrawer.widgets.CustomDuoDrawerToggle;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.entity.MenuField;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.presentation.presenter.contract.MainPresenter;
import ru.frosteye.beermap.presentation.support.navigation.MainNavigator;
import ru.frosteye.beermap.presentation.view.contract.MainView;
import ru.frosteye.beermap.presentation.view.impl.fragment.BaseFragment;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class MainActivity extends BaseActivity implements MainView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.common_toolbar) Toolbar navigationToolbar;
    @BindView(R.id.activity_main_drawer) DuoDrawerLayout drawer;
    @BindView(R.id.activity_main_menu) RecyclerView menu;
    @BindView(R.id.activity_main_userName) TextView userName;
    @BindView(R.id.activity_main_avatar) ImageView avatar;

    @Inject MainPresenter presenter;
    @Inject MainNavigator navigator;

    private FlexibleAdapter<MenuField> adapter;
    private @MenuRes int menuToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        setSupportActionBar(navigationToolbar);
        drawer.setMarginFactor(0.5f);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_menu_toggle)).getBitmap();
        DuoDrawerToggle drawerToggle = new CustomDuoDrawerToggle(this, navigationToolbar, drawer,
                new CustomDrawerArrowDrawable(getResources(), bitmap),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close, navigator::onDrawerClosed);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
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
    public void showUser(User user) {
        Picasso.with(this).load(user.getThumbnail()).fit().into(avatar);
        userName.setText(user.getFormattedName());
    }

    @Override
    public void showMenuItems(List<MenuField> fields) {
        adapter = new FlexibleAdapter<>(fields);
        menu.setLayoutManager(new LinearLayoutManager(this));
        menu.setAdapter(adapter);
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        menuToShow = fragment.getMenuToInflate();
        invalidateOptionsMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, fragment)
                .commit();
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
        navigator.onMenuItemSelected(field);
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
