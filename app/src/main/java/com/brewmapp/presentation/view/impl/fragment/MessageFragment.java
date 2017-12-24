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
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.view.contract.MessageFragmentView;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
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

        swipe.setOnRefreshListener(() -> presenter.loadDialogs(false));
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), (code, payload) -> {
                        Intent intent=new Intent(MultiFragmentActivityView.MODE_CHAT, null, getActivity(), MultiFragmentActivity.class);
                        User user=((Contact) payload).getFriend_info();
                        User friend=new User();
                        friend.setId(user.getId());
                        friend.setFirstname(user.getFirstname());
                        friend.setLastname(user.getLastname());
                        intent.putExtra(RequestCodes.INTENT_EXTRAS,friend);
                        startActivity(intent);
                    });
        list.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        presenter.setItemTouchHelper(list);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadDialogs(false);
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
    public void showDialogs(List<IFlexible> list) {
        original = list;
        adapter.updateDataSet(list);
    }

    @Override
    public void commonError(String... messages) {
        getActivity().runOnUiThread(()->((MainActivity)getActivity()).commonError(messages));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu!=null) menu.clear();
        inflater.inflate(R.menu.stub,menu);
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
    protected void prepareView(View view) {
        super.prepareView(view);
        if(interractor()!=null)   view.post(() -> interractor().processShowDrawer(true,true));
    }

}
