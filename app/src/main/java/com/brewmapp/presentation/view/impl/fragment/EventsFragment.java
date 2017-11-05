
package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.FilterAdapter;
import com.brewmapp.data.entity.FilteredTitle;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.EventsView;

import butterknife.BindView;
import butterknife.OnClick;
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

import static com.brewmapp.utils.Cons.REQUEST_CODE_REFRESH_ITEMS;

public class EventsFragment extends BaseFragment implements EventsView, View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.fragment_events_tabs) TabsView tabsView;
    @BindView(R.id.fragment_events_list) RecyclerView list;
    @BindView(R.id.fragment_events_swipe) RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_events_empty) TextView empty;
    @BindView(R.id.dark_layout) LinearLayout darkBackGround;
    @BindView(R.id.filter_list) ListView filterList;

    @Inject EventsPresenter presenter;
    @Inject ActiveBox activeBox;

    private String[] tabContent = ResourceHelper.getResources().getStringArray(R.array.event_types);
    private LoadNewsPackage loadNewsPackage = new LoadNewsPackage();
    private FlexibleModelAdapter<IFlexible> adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private List<FilteredTitle> dropdownItems;

    public static final int TAB_EVENT=0;
    public static final int TAB_SALE=1;
    public static final int TAB_POST=2;

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
                loadNewsPackage.parseIntent(getActivity().getIntent());
                interractor().processTitleDropDown(EventsFragment.this, loadNewsPackage.getFilter());
                interractor().processSetActionBar(tab.getPosition());
                presenter.storeTabActive(tab.getPosition());
                hideFilterLayout();
                if (dropdownItems != null) {
                    dropdownItems.clear();
                }
                refreshItems(true);
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
        swipe.setOnRefreshListener(() -> refreshItems(false));
        initFilterItems();
        filterList.setOnItemClickListener(this);
        interractor().processSpinnerTitleSubtitle(this.getTitleDropDown().get(0));
    }

    private void fillDropDownList() {
        dropdownItems = new ArrayList<>();
        for (String title : this.getTitleDropDown()) {
            dropdownItems.add(new FilteredTitle(title, false));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshItems(false);
    }

    private void processAction(int action, Object payload) {
        switch (action) {
            case Actions.ACTION_SELECT_EVENT:
                activeBox.setActive(payload);
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), EventDetailsActivity.class), REQUEST_CODE_REFRESH_ITEMS
                );
                break;
            case Actions.ACTION_SELECT_SALE:
                activeBox.setActive(payload);
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), SaleDetailsActivity.class), REQUEST_CODE_REFRESH_ITEMS
                );
                break;
            case Actions.ACTION_SELECT_POST:
                activeBox.setActive(payload);
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), PostDetailsActivity.class), REQUEST_CODE_REFRESH_ITEMS
                );
                break;
        }
    }

    private void showLogicAnimation() {
        animDarkBackground(darkBackGround);
        animFlowBrandList(filterList);
    }

    private void animFlowBrandList(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), filterList.isShown() ? R.anim.fadeout : R.anim.fadein);
        viewToAnimate.startAnimation(animation);
        viewToAnimate.setVisibility(filterList.isShown() ? View.GONE : View.VISIBLE);
    }

    private void animDarkBackground(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), filterList.isShown() ? R.anim.fadeout : R.anim.fadein);
        viewToAnimate.startAnimation(animation);
        viewToAnimate.setVisibility(filterList.isShown() ? View.GONE : View.VISIBLE);
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
    public void onBarAction(int id) {
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.action_add:
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), NewPostActivity.class), REQUEST_CODE_REFRESH_ITEMS);
                break;
        }
    }

    public void refreshItems(boolean tabSelected) {
        swipe.setRefreshing(true);
        list.removeOnScrollListener(scrollListener);
        adapter.clear();
        loadNewsPackage.setPage(0);
        presenter.onLoadItems(loadNewsPackage);
        if (tabSelected) {
            interractor().processSpinnerTitleSubtitle(this.getTitleDropDown().get(0));
        }
    }

    @OnClick(R.id.dark_layout)
    public void darkLayoutClicked() {
        showLogicAnimation();
    }

    @Override
    public void onClick(View v) {
        initFilterItems();
        showLogicAnimation();
    }

    private void hideFilterLayout() {
        filterList.setVisibility(View.GONE);
        darkBackGround.setVisibility(View.GONE);
    }

    private void initFilterItems() {
        if (dropdownItems == null || dropdownItems.size() == 0) {
            fillDropDownList();
        }
        initAdapter();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadNewsPackage.setFilter(position);
        interractor().processSpinnerTitleSubtitle(getTitleDropDown().get(position));
        if(loadNewsPackage.getFilter() == 2) {
            DateTools.showDateDialogRange(getActivity(), (startDate, endDate) -> {
                loadNewsPackage.setDateFrom(startDate);
                loadNewsPackage.setDateTo(endDate);
                presenter.onLoadItems(loadNewsPackage);
            }, Calendar.getInstance());
        } else {
            refreshItems(false);
        }

        for (int i = 0; i < dropdownItems.size(); i++) {
            if (position == i) {
                dropdownItems.get(position).setSelected(true);
            } else {
                dropdownItems.get(i).setSelected(false);
            }
        }
        initAdapter();
        showLogicAnimation();
    }

    private void initAdapter() {
        FilterAdapter filterAdapter = new FilterAdapter(getContext(), dropdownItems);
        filterList.setAdapter(filterAdapter);
    }

}
