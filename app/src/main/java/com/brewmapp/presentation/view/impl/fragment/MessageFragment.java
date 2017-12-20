package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.view.contract.MessageFragmentView;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogManageContact;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.storage.ResourceHelper;

import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import static android.app.Activity.RESULT_OK;

public class  MessageFragment extends BaseFragment implements MessageFragmentView {


    @BindView(R.id.fragment_friends_search) FinderView search;
    @BindView(R.id.fragment_friends_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_friends_list) RecyclerView list;

    @Inject    MessageFragmentPresenter presenter;

    private FlexibleAdapter<IFlexible> adapter;
    private List<IFlexible> original;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message;
    }


    @Override
    public void enableControls(boolean enabled, int code) {
        swipe.setRefreshing(!enabled);
    }

    @Override
    protected void initView(View view) {

        setHasOptionsMenu(true);

        search.setListener(string -> {
            adapter.setSearchText(string);
            adapter.filterItems(original);
        });

        swipe.setOnRefreshListener(() -> presenter.loadFriends(false));
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), (code, payload) -> {
            try {
                switch (((Contact) payload).getStatus()) {
                    case 1:
                        Intent intent=new Intent(MultiFragmentActivityView.MODE_CHAT, null, getActivity(), MultiFragmentActivity.class);
                        intent.putExtra(RequestCodes.INTENT_EXTRAS,((Contact) payload).getUser());
                        startActivity(intent);
                        break;
                    default:
                        new DialogManageContact(getActivity(), getActivity().getSupportFragmentManager(), payload, presenter);
                }

            }catch (Exception e){}
        }
            );
        list.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
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
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.messages);
    }

    @Override
    public void showFriends(List<IFlexible> list) {
        original = list;
        adapter.updateDataSet(list);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu!=null) menu.clear();
        inflater.inflate(R.menu.search,menu);
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onBarAction(int id) {
        switch (id){
            case R.id.action_search:
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
    protected void prepareView(View view) {
        super.prepareView(view);
        if(interractor()!=null)   view.post(() -> interractor().processShowDrawer(true,true));
    }

}
