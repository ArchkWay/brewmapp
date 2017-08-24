package com.brewmapp.presentation.view.impl.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import javax.inject.Inject;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.EventsView;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.impl.SimpleTabSelectListener;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;
import ru.frosteye.ovsa.tool.DateTools;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsFragment extends BaseFragment implements EventsView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.fragment_events_tabs) TabsView tabsView;
    @BindView(R.id.fragment_events_list) RecyclerView list;
    @BindView(R.id.fragment_events_swipe) RefreshableSwipeRefreshLayout swipe;

    @Inject EventsPresenter presenter;

    private String[] tabContent = ResourceHelper.getResources()
            .getStringArray(R.array.event_types);

    private LoadNewsPackage loadNewsPackage = new LoadNewsPackage();
    private FlexibleAdapter<IFlexible> adapter;
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
        list.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        adapter = new FlexibleAdapter<>(new ArrayList<>());
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(this::refreshItems);
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
        return R.menu.search;
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
        if(loadNewsPackage.getPage() == 0) {
            adapter.clear();
        }
        adapter.addItems(adapter.getItemCount(), list);
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
        } else refreshItems();
    }

    private void refreshItems() {
        swipe.setRefreshing(true);
        adapter.clear();
        loadNewsPackage.setPage(0);
        presenter.onLoadItems(loadNewsPackage);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
