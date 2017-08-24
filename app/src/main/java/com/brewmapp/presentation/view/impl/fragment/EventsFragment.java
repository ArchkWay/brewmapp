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
    private int mode = MODE_EVENTS;
    private int filter = 0;
    private int page;

    private FlexibleAdapter<IFlexible> adapter;

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
                mode = tab.getPosition();
                filter = 0;
                interractor().processTitleDropDown(EventsFragment.this, filter);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        return tabContent[mode];
    }

    @Override
    public void appendItems(List<IFlexible> list) {

    }

    @Override
    public List<String> getTitleDropDown() {
        switch (mode) {
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
        filter = position;
        if(filter == 2) {
            LoadNewsPackage loadNewsPackage = new LoadNewsPackage(mode, filter, page);
            DateTools.showDateDialogRange(getActivity(), new DateTools.RangePickerCallback() {
                @Override
                public void onDateSelected(Date startDate, Date endDate) {

                }
            }, Calendar.getInstance());
        } else refreshItems();
    }

    private void refreshItems() {
        refreshItems(null);
    }

    private void refreshItems(LoadNewsPackage loadNewsPackage) {
        page = 0;
        swipe.setRefreshing(true);
        adapter.clear();
        presenter.onLoadItems(loadNewsPackage == null ? new LoadNewsPackage(mode, filter, page) : loadNewsPackage);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
