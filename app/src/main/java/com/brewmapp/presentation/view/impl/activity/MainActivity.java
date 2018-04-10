package com.brewmapp.presentation.view.impl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MenuRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.ChatDialog;
import com.brewmapp.execution.exchange.response.ChatListDialogs;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.presentation.support.navigation.FragmentInterractor;
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;
import com.brewmapp.presentation.view.impl.fragment.BeerMapFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

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

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.navigation.impl.SimpleNavAction;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_STATE;

public class MainActivity extends BaseActivity
        implements
        MainView,
        FlexibleAdapter.OnItemClickListener,
        FragmentInterractor,
        SearchFragment.OnFragmentInteractionListener,
        BeerMapFragment.OnFragmentInteractionListener,
        EventsFragment.OnFragmentInteractionListener
{

    //region BindView
    @BindView(R.id.common_toolbar)
    Toolbar toolbar;
    @BindView(R.id.common_toolbar_dropdown)
    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)
    TextView toolbarSubTitle;
    @BindView(R.id.activity_main_drawer)
    DuoDrawerLayout drawer;
    @BindView(R.id.activity_main_menu)
    RecyclerView menu;
    @BindView(R.id.activity_main_userName)
    TextView userName;
    @BindView(R.id.activity_main_avatar)
    ImageView avatar;
    @BindView(R.id.activity_main_profileHeader)
    View profileHeader;
    @BindView(R.id.activity_main_container)
    FrameLayout container;
    @BindView(R.id.activity_main_visible_container)
    RelativeLayout visible_container;
    @BindView(R.id.progressToolbar)
    ProgressBar progressToolbar;
    //endregion

    //region Private
    private FlexibleAdapter<MenuField> adapter;
    private List<MenuField> menuItems;
    private @MenuRes    int menuToShow;
    private DuoDrawerToggle drawerToggle;
    private String mode;
    private BaseFragment baseFragment;
    //endregion

    //region Inject
    @Inject    public MainPresenter presenter;
    @Inject    public MainNavigator navigator;
    //endregion

    //region Static
    //public static final String KEY_FIRST_FRAGMENT = "first_fragment";
    public static final String MODE_DEFAULT = "default";
    public static final String MODE_EVENT_FRAGMENT_WITHOUT_TABS = "event_fragment";
    public static final String MODE_MAP_FRAGMENT = "map_fragment";
    public static final String MODE_SEARCH_FRAGMENT = "search_fragment";
    //endregion

    //region Impl MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        mode = presenter.parseMode(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mode){
            case MODE_DEFAULT:
                if(menuToShow > 0){
                    getMenuInflater().inflate(menuToShow, menu);
                    break;
                }
            default:
                menu.clear();
                getMenuInflater().inflate(R.menu.stub, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            case R.id.action_filter:
            case R.id.action_map:
                return super.onOptionsItemSelected(item);
            default:
                return navigator.onOptionsItemSelected(item);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_REFRESH_STATE) {
                refreshState();
            }
        }
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        if(mode.equals(MODE_DEFAULT))
            if(presenter.getActiveFragment()== MenuField.PROFILE) {
                if(!drawer.isDrawerOpen())
                    drawer.openDrawer();
            }else {
                MenuField.unselectAll(menuItems);
                adapter.notifyDataSetChanged();
                navigator.onNavigatorAction(new SimpleNavAction(MenuField.PROFILE));
                navigator.onDrawerClosed();
            }

        else
            finish();

    }

    @Override
    public void showTopBarLoading(boolean show){
        progressToolbar.setVisibility(show?View.VISIBLE:View.GONE);
    }
    //endregion

    //region User Events
    @Override
    public boolean onItemClick(int position) {

        MenuField field = adapter.getItem(position);
        if(field.getId() == MenuField.LOGOUT) {
//            presenter.onLogout();
//            startActivityAndClearTask(StartActivity.class);
//            finish();
            new DialogConfirm(getString(R.string.exit_from_app, getString(R.string.app_name)), getSupportFragmentManager(), new DialogConfirm.OnConfirm() {
                @Override
                public void onOk() {
                    finish();
                }

                @Override
                public void onCancel() {
                    drawer.closeDrawer();
                }
            });
            return true;
        }
        MenuField.unselectAll(menuItems);
        field.setSelected(true);
        adapter.notifyDataSetChanged();
        navigator.onMenuItemSelected(field);
        return true;
    }
    //endregion

    //region Impl MainView
    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void showFragment(BaseFragment fragment) {
        baseFragment = fragment;
        baseFragment .setArguments(presenter.prepareArguments(getIntent(),container));
        invalidateOptionsMenu();
        processTitleDropDown(baseFragment , 0);
        processSetActionBar(baseFragment .getMenuToInflate());
        navigator.setActionBarItemDelegate(baseFragment );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_container, baseFragment )
                .commit();
    }

    @Override
    public void showDrawer(boolean shown) {
        if(shown)
            drawer.openDrawer();
        else {
            drawer.closeDrawer();
        }
    }

    @Override
    public void successCheckEnvironment(User user) {
        Picasso.with(this).load(user.getThumbnail()).fit().into(avatar);
        userName.setText(user.getFormattedName());
        this.menuItems = MenuField.createDefault(this);
        adapter = new FlexibleAdapter<>(this.menuItems, this);
        menu.setLayoutManager(new LinearLayoutManager(this));
        menu.setAdapter(adapter);

        switch (mode){
            case MODE_DEFAULT:
                setDrawer();
                break;
            case MODE_EVENT_FRAGMENT_WITHOUT_TABS:
                enableBackButton();
                navigator.storeCodeActiveFragment(MenuField.EVENTS);
                navigator.storeCodeTebEventFragment(
                        getIntent().getIntExtra(RequestCodes.INTENT_EXTRAS,EventsFragment.TAB_EVENT)
                );
                setResult(RESULT_OK);
                break;
            case MODE_MAP_FRAGMENT:
                enableBackButton();
                navigator.storeCodeActiveFragment(MenuField.MAP);
                setResult(RESULT_OK);
                break;
            case MODE_SEARCH_FRAGMENT:
                navigator.storeCodeActiveFragment(MenuField.SEARCH);
                break;
        }

        navigator.onAttachView(this);
        navigator.onNavigatorAction(new SimpleNavAction(presenter.getActiveFragment()));
        navigator.onDrawerClosed();
    }

    @Override
    public Context getContext() {
        return this;
    }

    //endregion

    //region Impl FragmentInterractor
    @Override
    public void processTitleDropDown(BaseFragment baseFragment, int selected) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;

        if (baseFragment.getTitleDropDown() != null && !baseFragment.getTitleDropDown().isEmpty()) {
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
    public void processSetActionBar(int menu_id) {

        menuToShow=menu_id > 0?menu_id:R.menu.stub;

        invalidateOptionsMenu();
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        toolbarTitle.setText(charSequence);
    }
    //endregion

    //region Impl Common
    @Override
    public void processChangeFragment(int id) {
        MenuField.unselectAll(menuItems);
        adapter.notifyDataSetChanged();
        navigator.onNavigatorAction(new SimpleNavAction(id));
        navigator.onDrawerClosed();
    }

    @Override
    public void commonError(String... strings) {
        if(strings!=null && strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        processChangeFragment(MenuField.PROFILE);
    }

    @Override
    public OnLocationInteractionListener getLocationListener() {
        return this;
    }
    //endregion

    //region Functions
    private void setDrawer() {
        drawer.setMarginFactor(0.5f);
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_menu_toggle)).getBitmap();
        drawerToggle = new CustomDuoDrawerToggle(
                this,
                toolbar,
                drawer,
                new CustomDrawerArrowDrawable(getResources(), bitmap),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close,
                new CustomDuoDrawerToggle.Listener() {
                    @Override
                    public void onDrawerClosed() {
                        navigator.onDrawerClosed();
                    }
                    @Override
                    public void onDrawerOpen() {
                        Intent intent=new Intent(ChatService.ACTION_REQUEST_DIALOGS,null,MainActivity.this,ChatService.class);
                        intent.putExtra(
                                ChatService.RECEIVER,
                                new ChatResultReceiver(
                                        new Handler(MainActivity.this.getMainLooper()),
                                        new SimpleSubscriber<Bundle>(){
                                            @Override
                                            public void onNext(Bundle bundle) {
                                                super.onNext(bundle);
                                                ChatListDialogs chatListDialogs = (ChatListDialogs) bundle.getSerializable(ChatService.EXTRA_PARAM2);
                                                int cntUnread=0;
                                                for (ChatDialog chatDialog:chatListDialogs)
                                                    cntUnread+=chatDialog.getUnread();
                                                TextView textView= (TextView) menu.getLayoutManager().getChildAt(MenuField.MESSAGES).findViewById(R.id.view_menuField_badget);
                                                textView.setVisibility(cntUnread==0?View.GONE:View.VISIBLE);
                                                textView.setText(String.valueOf(cntUnread));

                                            }
                                        })
                        );
                        startService(intent);
                    }
                });
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        profileHeader.setOnClickListener(v -> {
            MenuField.unselectAll(menuItems);
            adapter.notifyDataSetChanged();
            navigator.onNavigatorAction(new SimpleNavAction(MenuField.PROFILE));
        });

    }

    public void refreshState() {
        if(baseFragment!=null)
            if(baseFragment instanceof ProfileFragment)
                ((ProfileFragment) baseFragment).refreshState();
    }

    //endregion

}
