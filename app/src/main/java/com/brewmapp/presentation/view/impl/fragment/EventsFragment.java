
package com.brewmapp.presentation.view.impl.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.FilterAdapter;
import com.brewmapp.data.entity.FilterBeerField;
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
import ru.frosteye.ovsa.presentation.view.widget.swipe.SwipeRefreshLayoutBottom;
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

import com.brewmapp.presentation.view.impl.activity.SelectCategoryActivity;
import com.brewmapp.presentation.view.impl.fragment.Simple.CreateBeerFragment;
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
    @BindView(R.id.fragment_events_swipe)    SwipeRefreshLayoutBottom swipeBottom;
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
    private List<FilteredTitle> dropdownItems;
    private final String MODE_DEFAULT="0";
//    private final String MODE_TABS_INVISIBLE ="1";
    private String mode=MODE_DEFAULT;
    private ArrayList<IFlexible> arrayList=new ArrayList<IFlexible>();
    //endregion

    //region Public
    public static final int TAB_REVIEWS = 0;
    public static final int TAB_NEWS = 1;
    public static final int TAB_EVENT = 2;
    public static final int TAB_SALE = 3;
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
//        if(getArguments().get(Keys.RELATED_MODEL)!=null&&getArguments().get(Keys.RELATED_ID)!=null)
//            mode = MODE_TABS_INVISIBLE;

        //interractor().processSetActionBar(0);
        tabsView.setItems(Arrays.asList(tabContent), this);
        initFilterItems();
        filterList.setOnItemClickListener(this);
        interractor().processSpinnerTitleSubtitle(this.getTitleDropDown().get(0));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        swipeBottom.setOnRefreshListener(new SwipeRefreshLayoutBottom.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewsPackage.setPage(loadNewsPackage.getPage()+1);
                presenter.onLoadItems(loadNewsPackage);

            }
        });
        list.setLayoutManager(manager);

        adapter = new FlexibleModelAdapter<>(arrayList, this::processAction);
        list.setAdapter(adapter);

        //setActiveTab
        //tabsView.setVisibility(mode.equals(MODE_TABS_INVISIBLE)?View.GONE:View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        onTabSelected(tabsView.getTabs().getTabAt(TAB_REVIEWS));
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
        return -1;
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
            case TAB_REVIEWS:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.filter_review));
            case TAB_NEWS:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_news));
            case TAB_EVENT:
                return Arrays.asList(ResourceHelper.getResources().getStringArray(R.array.events_filter_events));
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
        Log.v("xzxz", "---DBG EventsFragment appendItems size="+ list.size());

        setEmpty(loadNewsPackage.getPage() == 0 && list.isEmpty());

        if(loadNewsPackage.getPage() == 0) {
            arrayList.clear();
        }

        int prevSize=arrayList.size();
        arrayList.addAll(list);
//        adapter.notifyItemRangeInserted(prevSize,list.size());
        adapter.notifyDataSetChanged();

    }

    @Override
    public void enableControls(boolean enabled, int code) {
        if(enabled) {
            mListener.stopProgressParentActivity();
            swipeBottom.setRefreshing(false);
        }else {
            if(loadNewsPackage.getPage() == 0)
                mListener.StartProgressBarInActivity();
        }
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

        switch (loadNewsPackage.getMode()) {
            case TAB_REVIEWS:{

                if (loadNewsPackage.getFilter() == 2) {
                    Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                    intent.putExtra(Actions.PARAM1, SelectCategoryActivity.CHOOSE_REVIEWS_RELATED_MODELS);
                    intent.putExtra(Actions.PARAM2, 0);
                    //                intent.putExtra(Actions.PARAM3,new StringBuilder().append(filterId).toString());
                    //                intent.putExtra(Actions.PARAM4,new StringBuilder().append(filterTxt).toString());
                    startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
                }
                else if(loadNewsPackage.getFilter() == 3){
                    Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                    intent.putExtra(Actions.PARAM1, SelectCategoryActivity.CHOOSE_COUNTRY);
                    intent.putExtra(Actions.PARAM2, 0);
                    startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);



                }
                else {
                    refreshItems(false);
                }
                break;
            }
            case TAB_NEWS:{
                if(loadNewsPackage.getFilter() == 2) {
                    DateTools.showDateDialogRange(getActivity(), (startDate, endDate) -> {
                        loadNewsPackage.setDateFrom(startDate);
                        loadNewsPackage.setDateTo(endDate);
                        presenter.onLoadItems(loadNewsPackage);
                    }, Calendar.getInstance());
                }
                else if (loadNewsPackage.getFilter() == 4) {
                    Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                    intent.putExtra(Actions.PARAM1, SelectCategoryActivity.CHOOSE_NEWS_RELATED_MODELS);
                    intent.putExtra(Actions.PARAM2, 0);
                    startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
                }
                else if(loadNewsPackage.getFilter() == 5){
                    Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                    intent.putExtra(Actions.PARAM1, SelectCategoryActivity.CHOOSE_COUNTRY);
                    intent.putExtra(Actions.PARAM2, 0);
                    startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
                }
                else {
                    refreshItems(false);
                }
                break;
            }
            case TAB_EVENT:{
                if(loadNewsPackage.getFilter() == 4) {
                    DateTools.showDateDialogRange(getActivity(), (startDate, endDate) -> {
                        loadNewsPackage.setDateFrom(startDate);
                        loadNewsPackage.setDateTo(endDate);
                        presenter.onLoadItems(loadNewsPackage);
                    }, Calendar.getInstance());
                }
                else if(loadNewsPackage.getFilter() == 5){
                    Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                    intent.putExtra(Actions.PARAM1, SelectCategoryActivity.CHOOSE_COUNTRY);
                    intent.putExtra(Actions.PARAM2, 0);
                    startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);
                }
                else {
                    refreshItems(false);
                }
                break;
            }
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
        interractor().processSetActionBar(tab.getPosition() == TAB_NEWS ? R.menu.search_add : R.menu.search);
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

        arrayList.clear();
        adapter.notifyDataSetChanged();
        loadNewsPackage.setPage(0);

        try {loadNewsPackage.setResto_id(getArguments().getString(Keys.RELATED_ID));}catch (Exception e){}

        if(getArguments().getString(Keys.RELATED_MODEL) != null) {
            try {
                loadNewsPackage.setRelated_model(getArguments().getString(Keys.RELATED_MODEL));
            }
            catch (Exception e) {
            }
        }

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
//        addDataTotitles();

        FilterAdapter filterAdapter = new FilterAdapter(getContext(), dropdownItems);
        filterList.setAdapter(filterAdapter);
    }
    private void addDataTotitles() {
        switch (loadNewsPackage.getMode()) {
            case TAB_REVIEWS:{
//                dropdownItems.get(2).setTitle(dropdownItems.get(2).getTitle()+ loadNewsPackage.get);

                break;
            }
            case TAB_NEWS:{

                break;
            }
            case TAB_EVENT:{

                break;
            }
        }
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
                case TAB_REVIEWS:
                    this.empty.setText(R.string.no_events);
                    break;
                case TAB_NEWS:
                    this.empty.setText(R.string.no_news);
                    break;
                case TAB_EVENT:
                    this.empty.setText(R.string.no_events);
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

        void StartProgressBarInActivity();

        void stopProgressParentActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.REQUEST_SEARCH_CODE:
                if(resultCode== Activity.RESULT_OK){
                    String param1=data.getStringExtra(Actions.PARAM1);
                    String param3=data.getStringExtra(Actions.PARAM3);
                    String param4=data.getStringExtra(Actions.PARAM4);

                    switch (param1){
                        case SelectCategoryActivity.CHOOSE_NEWS_RELATED_MODELS:{
                            loadNewsPackage.setRelated_model(param3);
                            dropdownItems.get(4).setTitle(ResourceHelper.getResources().getStringArray(R.array.events_filter_news)[4] +" "+ param4);
                            break;
                        }
                        case SelectCategoryActivity.CHOOSE_REVIEWS_RELATED_MODELS:{
                            loadNewsPackage.setRelated_model(param3);
                            dropdownItems.get(2).setTitle(ResourceHelper.getResources().getStringArray(R.array.filter_review)[2] +" "+ param4);
                            break;
                        }
                        case SelectCategoryActivity.CHOOSE_CITY:{
                            loadNewsPackage.setCity_Id(param3);

                            switch (loadNewsPackage.getMode()){
                                case TAB_REVIEWS:{
                                    dropdownItems.get(3).setTitle(ResourceHelper.getResources().getStringArray(R.array.filter_review)[3]+" "+ param4);
                                    break;
                                }
                                case TAB_NEWS:{
                                    dropdownItems.get(5).setTitle(ResourceHelper.getResources().getStringArray(R.array.events_filter_news)[5] +" "+ param4);
                                    break;
                                }
                                case TAB_EVENT:{
                                    dropdownItems.get(5).setTitle(ResourceHelper.getResources().getStringArray(R.array.events_filter_events)[5] +" "+ param4);
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    refreshItems(false);
//                    switch (data.getIntExtra(Actions.PARAM2,Integer.MAX_VALUE)) {
//                        case FilterBeerField.CITY:
//                            try {
//                                wrapperParamsBeer.addParam(Keys.COUNTRY_ID,param3.split(",")[0]);
//                                beer_country.setText(param4.split(",")[0]);
//                                mListener.invalidateOptionsMenu();
//                            }catch (Exception e){}
//                            break;
//                        case FilterBeerField.TYPE:
//                            try {
//                                wrapperParamsBeer.addParam(Keys.TYPE_ID,param3.split(",")[0]);
//                                beer_type.setText(param4.split(",")[0]);
//                                mListener.invalidateOptionsMenu();
//                            }catch (Exception e){}
//                            break;
//                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
