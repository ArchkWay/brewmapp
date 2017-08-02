package ru.frosteye.beermap.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.entity.MenuField;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.presentation.presenter.contract.MainPresenter;
import ru.frosteye.beermap.presentation.view.contract.MainView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.common_toolbar) Toolbar navigationToolbar;
    @BindView(R.id.activity_main_drawer) DuoDrawerLayout drawer;
    @BindView(R.id.activity_main_menu) RecyclerView menu;
    @BindView(R.id.activity_main_userName) TextView userName;
    @BindView(R.id.activity_main_avatar) ImageView avatar;

    @Inject MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        setSupportActionBar(navigationToolbar);
        drawer.setMarginFactor(0.5f);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_toggle);
        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawer, navigationToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_toggle);
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
        FlexibleAdapter<MenuField> adapter = new FlexibleAdapter<>(fields);
        menu.setLayoutManager(new LinearLayoutManager(this));
        menu.setAdapter(adapter);
    }
}
