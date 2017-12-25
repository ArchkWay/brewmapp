package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.SearchBeer;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.SearchPresenter;
import com.brewmapp.presentation.view.contract.SearchView;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.FinderView;

import java.util.ArrayList;
import java.util.List;

import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

public class SearchActivity extends BaseActivity implements SearchView {

    @BindView(R.id.filter_category_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_search_list) RecyclerView list;
    @BindView(R.id.activity_search_search) FinderView finder;
    @BindView(R.id.activity_search_more) Button more;
    @BindView(R.id.title_toolbar)
    TextView titleToolbar;

    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage searchPackage;
    private int selectedTab;
    private EndlessRecyclerOnScrollListener scrollListener;
    private ProgressDialog dialog;
    private String[] titleContent = ResourceHelper.getResources().getStringArray(R.array.full_search);
    private int craftBeer, offer, filterBeer;

    @Inject SearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    protected void initView() {
        enableBackButton();
        searchPackage = new FullSearchPackage();
        searchPackage.setPage(0);
        if (getIntent().getExtras() != null) {
            selectedTab = getIntent().getExtras().getInt(Keys.SEARCH_RESULT, 0);
            craftBeer = getIntent().getExtras().getInt("craft", 0);
            offer = getIntent().getExtras().getInt("offer", 0);
            filterBeer = getIntent().getExtras().getInt("filter", 0);
            finder.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        }
        titleToolbar.setText(titleContent[selectedTab]);
        more.setOnClickListener(v -> startActivity(ExtendedSearchActivity.class));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                searchPackage.setPage(manager.getItemCount());
                loadResult(searchPackage);
            }
        };
        list.addOnScrollListener(scrollListener);
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        list.setLayoutManager(manager);
        adapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        list.setAdapter(adapter);
        loadResult(searchPackage);
    }

    private void loadResult(FullSearchPackage searchPackage) {
        switch (selectedTab) {
            case 0:
                showDialogProgressBar(R.string.search_resto_message);
                presenter.loadRestoList(offer, searchPackage);
                break;
            case 1:
                showDialogProgressBar(R.string.search_beer_message);
                presenter.loadBeerList(craftBeer, filterBeer, searchPackage);
                break;
            case 2:
                showDialogProgressBar(R.string.search_brewery_message);
                presenter.loadBrewery(searchPackage);
                break;
            default: break;
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
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void showDialogProgressBar(int message) {
        dialog = ProgressDialog.show(this, getString(R.string.loading),
                getString(message), true, false);
    }

    @Override
    public void hideProgressBar() {
        if (dialog != null) {
            dialog.cancel();
        }
    }


    private void processAction(int action, Object payload) {
        switch (action) {
            case FilterRestoField.NAME:
                Resto resto = (Resto) payload;
                goToRestoDetails(String.valueOf(resto.getId()));
                break;
            case FilterRestoField.BEER:
                SearchBeer beer = (SearchBeer) payload;
                goToBeerDetails(beer.getId());
                break;
            default:
                break;
        }
    }

    private void goToRestoDetails(String restoId) {
        Interest interest = new Interest();
        Interest_info interest_info = new Interest_info();
        interest_info.setId(restoId);
        interest.setInterest_info(interest_info);
        Intent intent = new Intent(this, RestoDetailActivity.class);
        intent.putExtra(RESTO_ID, interest);
        startActivity(intent);
    }

    private void goToBeerDetails(String beerId) {
        Beer beer = new Beer();
        beer.setId(beerId);
        Intent intent = new Intent(this, BeerDetailActivity.class);
        intent.putExtra(getString(R.string.key_serializable_extra), new Interest(beer));
        startActivity(intent);
    }

    @Override
    public void appendItems(List<IFlexible> list) {
        adapter.updateDataSet(list);
    }
}
