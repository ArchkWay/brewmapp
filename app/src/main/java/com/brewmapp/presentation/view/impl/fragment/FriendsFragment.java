package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.data.entity.wrapper.FriendsTitleInfo;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.InviteActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.widget.FinderView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import static android.app.Activity.RESULT_OK;

public class FriendsFragment extends BaseFragment implements FriendsView
{

    //region BindView
    @BindView(R.id.fragment_friends_addFriend) View addFriend;
    @BindView(R.id.fragment_friends_search) FinderView search;
    @BindView(R.id.fragment_friends_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_friends_requests) RecyclerView requests;
    @BindView(R.id.fragment_friends_friends) RecyclerView friends;
    @BindView(R.id.fragment_friends_find_friends) RecyclerView find_friends;
    @BindView(R.id.fragment_friends_container_show_friends)    LinearLayout container_show_friends;
    @BindView(R.id.fragment_friends_container_find_friends)    LinearLayout container_find_friends;
    @BindView(R.id.fragment_friends_progressBar)    RelativeLayout progressBar;
    @BindView(R.id.fragment_friends_text_info_pre_search)    TextView text_info_pre_search;

    //endregion

    //region Inject
    @Inject FriendsPresenter presenter;
    //endregion

    //region Private
    private FlexibleAdapter<IFlexible> adapter_friends;
    private FlexibleAdapter<IFlexible> adapter_find_friends;
    private FlexibleAdapter<IFlexible> adapter_requests;
    private List<IFlexible> original_friends=new ArrayList<>();
    private List<IFlexible> original_find_friends=new ArrayList<>();
    private List<IFlexible> original_requests=new ArrayList<>();
    private final int MODE_SHOW_FRIENDS=0;
    private final int MODE_FIND_FRIENDS=1;
    private FullSearchPackage fullSearchPackage=new FullSearchPackage();
    private boolean enabled_control=true;
    private int mode=MODE_SHOW_FRIENDS;
    private int cnt_char_for_start_search;

    //endregion

    //region Impl FriendsFragment
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        addFriend.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), InviteActivity.class), RequestCodes.REQUEST_INVITE_FRIEND);
        });

        search.setListener(string -> {
            setMode(TextUtils.isEmpty(string)?MODE_SHOW_FRIENDS:MODE_FIND_FRIENDS);
            prepQueryFriends(string);
            tuneModeView();
        });
        search.setEnabled(false);

        swipe.setOnRefreshListener(() -> presenter.loadFriends(false));
        adapter_friends = new FlexibleModelAdapter<>(original_friends, (code, payload) -> presenter.onClickItem(code, payload,(BaseActivity) getActivity()));
        friends.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        friends.setLayoutManager(new LinearLayoutManager(getActivity()));
        friends.setAdapter(adapter_friends);

        adapter_requests = new FlexibleModelAdapter<>(original_requests, (code, payload) -> presenter.onClickItem(code, payload,(BaseActivity)getActivity()));
        requests.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        requests.setLayoutManager(new LinearLayoutManager(getActivity()));
        requests.setAdapter(adapter_requests);


        adapter_find_friends= new FlexibleModelAdapter<>(original_find_friends, (code, payload) -> presenter.onClickItem(code, payload,(BaseActivity)getActivity()));
        find_friends.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        find_friends.setLayoutManager(new LinearLayoutManager(getActivity()));
        find_friends.setAdapter(adapter_find_friends);
        find_friends.setOnTouchListener((v, event) -> {
            if(search.isFocused())
                search.clearFocus();
            return false;
        });

        presenter.setItemTouchHelper(friends);

        cnt_char_for_start_search=getResources().getInteger(R.integer.cnt_char_for_start_search);

        tuneModeView();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.add,menu);
        inflater.inflate(R.menu.stub,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadFriends(false);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return -1;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.friends);
    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onBarAction(int id) {
        switch (id){
            case R.id.action_add:
                startActivityForResult(
                        new Intent(
                                Keys.CAP_USER_FRIENDS,
                                null,getActivity(),
                                MultiListActivity.class),
                        RequestCodes.REQUEST_INTEREST
                );
                break;
            default:
                super.onBarAction(id);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case RequestCodes.REQUEST_INTEREST:
                if(resultCode==RESULT_OK){
                    presenter.requestNewFriend(data);
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        search.clearFocus();
    }


    //endregion

    //region Impl FriendsView
    @Override
    public void enableControls(boolean enabled, int code) {
        swipe.setRefreshing(!enabled);
        enabled_control=enabled;
    }

    @Override
    public void showFriends(List<IFlexible> list) {
        search.setEnabled(true);
        switch (mode){
            case MODE_SHOW_FRIENDS:
                //region Show friend
                original_requests.clear();
                original_friends.clear();

                Iterator<IFlexible> iterator=list.iterator();
                while (iterator.hasNext()){

                    IFlexible iFlexible=iterator.next();

                    //region get status
                    int status=0;
                    if(iFlexible instanceof FriendsTitleInfo)
                        status =((FriendsTitleInfo)iFlexible).getStatus();
                    else if (iFlexible instanceof ContactInfo){
                        status =((ContactInfo)iFlexible).getModel().getStatus();
                    }
                    //endregion

                    //region fill items
                    switch (status){
                        case FriendsView.FRIENDS_REQUEST_IN:
                        case FriendsView.FRIENDS_REQUEST_OUT:
                            original_requests.add(iFlexible);
                            break;
                        case FriendsView.FRIENDS_NOW:
                            original_friends.add(iFlexible);
                            break;
                    }
                    //endregion
                }

                if(original_requests.size()>0)
                    original_requests.add(0,new FriendsTitleInfo(getString(R.string.title_friends_request),FRIENDS_DEFAULT));

                adapter_requests.notifyDataSetChanged();
                adapter_friends.notifyDataSetChanged();
                //endregion
                break;
            case MODE_FIND_FRIENDS:
                //region Find Friends
                if(original_find_friends.size()==0&&list.size()>0){
                    original_find_friends.add(0,new FriendsTitleInfo(getString(R.string.action_find_friends),FRIENDS_DEFAULT));
                }
                Iterator<IFlexible> infoIterator=list.iterator();
                while (infoIterator.hasNext())
                    original_find_friends.add(new ContactInfo(new Contact(((UserInfo) infoIterator.next()).getModel())));

                //original_find_friends.addAll(list);
                adapter_find_friends.notifyItemRangeInserted(0,original_find_friends.size());
                //endregion
                break;
        }
        tuneModeView();
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void ModeShowFrendsON() {
        setMode(MODE_SHOW_FRIENDS);
        tuneModeView();
    }
    //endregion

    //region Functions
    private void tuneModeView() {

        switch (mode){
            case MODE_FIND_FRIENDS:
                //region Tune MODE_FIND_FRIENDS
                container_find_friends.setVisibility(View.VISIBLE);
                container_show_friends.setVisibility(View.GONE);
                text_info_pre_search.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                find_friends.setVisibility(View.GONE);

                if(enabled_control){
                    String strSearch=fullSearchPackage.getStringSearch();
                    if(enabled_control&&strSearch.length()<=cnt_char_for_start_search){
                        text_info_pre_search.setVisibility(View.VISIBLE);
                        text_info_pre_search.setText(R.string.text_continue_input);
                        text_info_pre_search.setTextColor(Color.GRAY);
                    }else if(original_find_friends.size()==0){
                        text_info_pre_search.setVisibility(View.VISIBLE);
                        text_info_pre_search.setText(R.string.text_not_found_nothing);
                        text_info_pre_search.setTextColor(Color.RED);
                    }else {
                        find_friends.setVisibility(View.VISIBLE);
                    }
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }

                //endregion
                break;
            case MODE_SHOW_FRIENDS:
                //region Tune MODE_SHOW_FRIENDS
                container_find_friends.setVisibility(View.GONE);
                container_show_friends.setVisibility(View.VISIBLE);
                //endregion
                break;
        }

    }

    private void prepQueryFriends(String string) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(string);
        fullSearchPackage.setType(Keys.TYPE_USER);
        //adapter_find_friends.notifyItemRangeRemoved(0,original_find_friends.size());
        original_find_friends.clear();
        adapter_find_friends.notifyDataSetChanged();
        if(string.length()>cnt_char_for_start_search)
            QueryFriends(fullSearchPackage);

    }

    private void QueryFriends(FullSearchPackage fullSearchPackage) {
        presenter.searchFriends(fullSearchPackage);
    }

    //endregion


}
