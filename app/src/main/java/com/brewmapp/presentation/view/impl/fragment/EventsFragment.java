
package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
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
import com.brewmapp.presentation.view.contract.OnLocationInteractionListener;
import com.brewmapp.presentation.view.impl.activity.EventDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.activity.PostDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.SaleDetailsActivity;

import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

public class EventsFragment extends BaseFragment implements
        EventsView,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        TabLayout.OnTabSelectedListener
{

    //region BindView
    @BindView(R.id.fragment_events_tabs) TabsView tabsView;
    @BindView(R.id.fragment_events_list) RecyclerView list;
    @BindView(R.id.fragment_events_swipe) RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_events_empty) TextView empty;
    @BindView(R.id.dark_layout) LinearLayout darkBackGround;
    @BindView(R.id.filter_list) ListView filterList;
    //endregion

    //region Inject
    @Inject EventsPresenter presenter;
    @Inject ActiveBox activeBox;
    //endregion

    //region Private
    private String[] tabContent = ResourceHelper.getResources().getStringArray(R.array.event_types);
    private LoadNewsPackage loadNewsPackage = new LoadNewsPackage();
    private FlexibleModelAdapter<IFlexible> adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private List<FilteredTitle> dropdownItems;
    private final String MODE_DEFAULT="0";
    private final String MODE_TABS_INVISIBLE ="1";
    private String mode=MODE_DEFAULT;
    private ArrayList<IFlexible> arrayList=new ArrayList<IFlexible>();
    //endregion

    //region Public
    public static final int TAB_EVENT = 1;
    public static final int TAB_SALE = 2;
    public static final int TAB_NEWS = 0;
    private OnFragmentInteractionListener mListener;
    private OnLocationInteractionListener mLocationListener;
    //endregion

    //region Impl EventsFragment
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_events;
    }

    @Override
    protected void initView(View view) {
        if(getArguments().get(Keys.RELATED_MODEL)!=null&&getArguments().get(Keys.RELATED_ID)!=null)
            mode = MODE_TABS_INVISIBLE;

        interractor().processSetActionBar(0);
        tabsView.setItems(Arrays.asList(tabContent), this);
        initFilterItems();
        filterList.setOnItemClickListener(this);
        interractor().processSpinnerTitleSubtitle(this.getTitleDropDown().get(0));
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
        adapter = new FlexibleModelAdapter<>(arrayList, this::processAction);
        list.setAdapter(adapter);
        swipe.setOnRefreshListener(() -> refreshItems(false));
        //setActiveTab
        tabsView.setVisibility(mode.equals(MODE_TABS_INVISIBLE)?View.GONE:View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        onTabSelected(tabsView.getTabs().getTabAt(TAB_NEWS));
//        int storedNumberTab=presenter.getStoredActiveTab();
//        TabLayout.Tab tab=tabsView.getTabs().getTabAt(storedNumberTab);
//        assert tab != null;
//        if(tabsView.getTabs().getSelectedTabPosition()==storedNumberTab)
//            onTabSelected(tab);
//        else
//            tab.select();
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
    public void onBarAction(int id) {
        switch (id) {
            case R.id.action_search:
                showMessage(getString(R.string.message_develop));
                break;
            case R.id.action_add:
                interractor().processStartActivityWithRefresh(new Intent(getActivity(), NewPostActivity.class), REQUEST_CODE_REFRESH_ITEMS);
                break;
        }
    }

    @Override
    public List<String> getTitleDropDown() {
        switch (loadNewsPackage.getMode()) {
            case 1:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_events));
            case 2:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_sales));
            case 0:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_news));
        }
        return super.getTitleDropDown();
    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mLocationListener = mListener.getLocationListener();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    //endregion

    //region Impl EventsView
    @Override
    public void appendItems(List<IFlexible> list) {
        setEmpty(loadNewsPackage.getPage() == 0 && list.isEmpty());
        if(loadNewsPackage.getPage() == 0) {
            arrayList.clear();
            scrollListener.reset();
        }
        int prevSize=arrayList.size();
        arrayList.addAll(list);
        adapter.notifyItemRangeInserted(prevSize,list.size());

    }

    @Override
    public void enableControls(boolean enabled, int code) {
        if(enabled) swipe.setRefreshing(false);
    }

    @Override
    public void refreshState() {
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.dark_layout)
    public void darkLayoutClicked() {
        showLogicAnimation();
    }
    //endregion

    //region User Events
    @Override
    public void onClick(View v) {
        initFilterItems();
        showLogicAnimation();
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        loadNewsPackage.dropAll();
        loadNewsPackage.setMode(tab.getPosition());
        interractor().processTitleDropDown(EventsFragment.this, loadNewsPackage.getFilter());
        interractor().processSetActionBar(tab.getPosition());
        presenter.storeTabActive(tab.getPosition());
        hideFilterLayout();
        if (dropdownItems != null)
            dropdownItems.clear();

        if(mode.equals(MODE_DEFAULT)) {
            mLocationListener.requestCity(result -> {
                if (result != null)
                    loadNewsPackage.setCity_Id(String.valueOf(result.getId()));
                else
                    loadNewsPackage.setCity_Id(null);
                refreshItems(true);
            });
        }else {
            loadNewsPackage.setCity_Id(null);
            refreshItems(true);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //endregion

    //region Function
    public void refreshItems(boolean tabSelected) {
        swipe.setRefreshing(true);
        arrayList.clear();
        adapter.notifyDataSetChanged();
        scrollListener.reset();
        loadNewsPackage.setPage(0);

        try {loadNewsPackage.setResto_id(getArguments().getString(Keys.RELATED_ID));}catch (Exception e){}
        try {loadNewsPackage.setRelated_model(getArguments().getString(Keys.RELATED_MODEL));}catch (Exception e){}

        presenter.onLoadItems(loadNewsPackage);
        if (tabSelected) {
            interractor().processSpinnerTitleSubtitle(this.getTitleDropDown().get(0));
        }
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

    private void initAdapter() {
        FilterAdapter filterAdapter = new FilterAdapter(getContext(), dropdownItems);
        filterList.setAdapter(filterAdapter);
    }

    private void processAction(int action, Object payload) {
        switch (action) {
            case Actions.ACTION_REFRESH_FRAGMENT_CONTENT:
                refreshItems(false);
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
                startActivity(new Intent(getActivity(), PostDetailsActivity.class));
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

    private void fillDropDownList() {
        dropdownItems = new ArrayList<>();
        for (String title : this.getTitleDropDown()) {
            dropdownItems.add(new FilteredTitle(title, false));
        }
    }

    //endregion

    public interface OnFragmentInteractionListener {
        OnLocationInteractionListener getLocationListener();
    }

}
