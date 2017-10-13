package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
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
import com.brewmapp.presentation.view.impl.activity.EventDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.activity.SaleDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.SearchActivity;
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
                presenter.onLikePost(((Post) payload));
                break;
            case Actions.ACTION_LIKE_SALE:
                presenter.onLikeSale(((Sale) payload));
                break;
            case Actions.ACTION_SELECT_EVENT:
                activeBox.setActive(payload);
                startActivity(new Intent(getActivity(), EventDetailsActivity.class));
                break;
            case Actions.ACTION_SELECT_SALE:
                activeBox.setActive(payload);
                startActivity(new Intent(getActivity(), SaleDetailsActivity.class));
                break;
            case Actions.ACTION_SALE_SHARE:
                presenter.onShareSale(((Sale) payload));
                break;
            case Actions.ACTION_SHARE_POST:
                presenter.onSharePost(((Post) payload));
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

    @Override
    public void showShareDialog(int resource_items,Object o) {
        new DialogShare(
                getActivity(),
                getResources().getStringArray(resource_items),
                (dialog, which) -> {
                    switch (which){
                        case 0:
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                            break;
                        case 1:
                            Intent intent=new Intent(getActivity(),NewPostActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            showMessage(" разработке");
                            break;
                        case 3:
                            presenter.onDeleteNewsTask((Post)o);
                            break;
                    }
                });

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
                startActivity(new Intent(getActivity(),NewPostActivity.class));
                break;
        }
    }

    private void refreshItems() {
        swipe.setRefreshing(true);
        list.removeOnScrollListener(scrollListener);
        adapter.clear();
        loadNewsPackage.setPage(0);
        presenter.onLoadItems(loadNewsPackage);



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class DialogShare extends AlertDialog.Builder {
        public DialogShare(@NonNull Context context, String[] items, DialogInterface.OnClickListener onClickListener) {
            super(context);
            //setItems(items, onClickListener);
            final ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            ((TextView) view.findViewById(android.R.id.text1)).setTextColor(position == 3 ? Color.RED : Color.BLACK);
                            return view;
                        }
                    };
            for (String s : items) arrayAdapter.add(s);
            setAdapter(arrayAdapter, onClickListener);
            show();
        }
    }
}
