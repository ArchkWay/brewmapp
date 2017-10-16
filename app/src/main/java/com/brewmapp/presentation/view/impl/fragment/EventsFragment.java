package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import javax.inject.Inject;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.EventsView;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.impl.SimpleTabSelectListener;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;
import ru.frosteye.ovsa.tool.DateTools;

import com.brewmapp.R;
import com.brewmapp.presentation.view.contract.ResultTask;
import com.brewmapp.presentation.view.contract.ResultDialog;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.EventDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.activity.PostDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.SaleDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.SearchActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EventsFragment extends BaseFragment implements EventsView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.fragment_events_tabs) TabsView tabsView;
    @BindView(R.id.fragment_events_list) RecyclerView list;
    @BindView(R.id.fragment_events_swipe) RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_events_empty) TextView empty;

    @Inject EventsPresenter presenter;
    @Inject ActiveBox activeBox;

    private String[] tabContent = ResourceHelper.getResources()
            .getStringArray(R.array.event_types);

    private LoadNewsPackage loadNewsPackage = new LoadNewsPackage();
    private FlexibleModelAdapter<IFlexible> adapter;
    private EndlessRecyclerOnScrollListener scrollListener;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_events;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        if(enabled) swipe.setRefreshing(false);
    }

    @Override
    protected void initView(View view) {

        tabsView.setItems(Arrays.asList(tabContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadNewsPackage.dropAll();
                loadNewsPackage.setMode(tab.getPosition());
                interractor().processTitleDropDown(EventsFragment.this, loadNewsPackage.getFilter());
                interractor().processSetActionBar(tab.getPosition());
                presenter.storeTabActive(tab.getPosition());
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadNewsPackage.setPage(currentPage - 1);
                presenter.onLoadItems(loadNewsPackage);
            }
        };
        list.setLayoutManager(manager);
        list.addOnScrollListener(scrollListener);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(this::refreshItems);

    }

    private void processAction(int action, Object payload) {
        switch (action) {
            case Actions.ACTION_LIKE_POST:
            case Actions.ACTION_LIKE_EVENT:
            case Actions.ACTION_LIKE_SALE:
                presenter.onLike((ILikeable)payload,this);
                break;
            case Actions.ACTION_SELECT_EVENT:
                activeBox.setActive(payload);
                startActivity(new Intent(getActivity(), EventDetailsActivity.class));
                break;
            case Actions.ACTION_SELECT_SALE:
                activeBox.setActive(payload);
                startActivity(new Intent(getActivity(), SaleDetailsActivity.class));
                break;
            case Actions.ACTION_SELECT_POST:
                activeBox.setActive(payload);
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), PostDetailsActivity.class));
                break;
            case Actions.ACTION_SHARE_POST:
            case Actions.ACTION_SHARE_SALE:
            case Actions.ACTION_SHARE_EVENT:
                new DialogShare((BaseActivity) getActivity(),(ILikeable) payload, () -> refreshItems());
                break;
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
    public int getMenuToInflate() {
        return R.menu.search_add;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return tabContent[loadNewsPackage.getMode()];
    }

    @Override
    public void appendItems(List<IFlexible> list) {
        setEmpty(loadNewsPackage.getPage() == 0 && list.isEmpty());
        if(loadNewsPackage.getPage() == 0) {
            adapter.clear();
            scrollListener.reset();
            this.list.addOnScrollListener(scrollListener);
        }
        adapter.addItems(adapter.getItemCount(), list);

    }

    @Override
    public void setTabActive(int i) {
        tabsView.getTabs().getTabAt(i).select();
    }


    private void setEmpty(boolean empty) {
        if(!empty) {
            this.empty.setVisibility(View.GONE);
        } else {
            this.empty.setVisibility(View.VISIBLE);
            switch (loadNewsPackage.getMode()) {
                case 0:
                    this.empty.setText(R.string.no_events);
                    break;
                case 1:
                    this.empty.setText(R.string.no_sales);
                    break;
                case 2:
                    this.empty.setText(R.string.no_news);
                    break;
            }
        }

    }

    @Override
    public void refreshState() {

        adapter.notifyDataSetChanged();

    }

    @Override
    public List<String> getTitleDropDown() {
        switch (loadNewsPackage.getMode()) {
            case 0:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_events));
            case 1:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_sales));
            case 2:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_news));
        }
        return super.getTitleDropDown();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loadNewsPackage.setFilter(position);
        if(loadNewsPackage.getFilter() == 2) {
            DateTools.showDateDialogRange(getActivity(), (startDate, endDate) -> {
                loadNewsPackage.setDateFrom(startDate);
                loadNewsPackage.setDateTo(endDate);
                presenter.onLoadItems(loadNewsPackage);
            }, Calendar.getInstance());
        } else {
            refreshItems();
        }
    }

    @Override
    public void onBarAction(int id) {
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.action_add:
                interractor().processStartActivityWithRefresh(new Intent(getActivity(),NewPostActivity.class));
                break;
        }
    }

    public void refreshItems() {
        swipe.setRefreshing(true);
        list.removeOnScrollListener(scrollListener);
        adapter.clear();
        loadNewsPackage.setPage(0);
        presenter.onLoadItems(loadNewsPackage);



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
