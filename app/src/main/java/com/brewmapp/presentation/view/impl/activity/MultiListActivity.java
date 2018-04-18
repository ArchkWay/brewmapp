package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.widget.FinderView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_PROFILE_FRIEND;

public class MultiListActivity extends BaseActivity implements
        MultiListView,
        View.OnClickListener
{

    //region BindView
    @BindView(R.id.common_toolbar_search)    Toolbar toolbarSearch;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_search_search)    FinderView finder;
    @BindView(R.id.activity_add_interest_list)    RecyclerView recyclerview;
    @BindView(R.id.activity_add_interest_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_add_interest_text_info_empty_list)    TextView text_empty_list;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.mulilist_activity_button_review_layout)    LinearLayout button_review_layout;
    @BindView(R.id.activity_add_interest_container_header) LinearLayout container_header;
    @BindView(R.id.activity_add_interest_button_create) com.brewmapp.presentation.view.impl.widget.ButtonCreate button_create;
    @BindView(R.id.activity_add_interest_progressBar)    ProgressBar progressBar;
    //endregion

    //region Inject
    @Inject    MultiListPresenter presenter;
    //endregion

    //region Private
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private String mode;
    private int id_text_empty=0;
    private final int cntCharStartSearch=4;

    //endregion

    //region Impl MultiListActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
    }

    @Override
    protected void initView() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        enableBackButton();
        swipe.setOnRefreshListener(this::refreshItems);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                //fullSearchPackage.setPage(currentPage-1);
                //sendQuery();
            }
        };
        recyclerview.setLayoutManager(manager);
        adapter= new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void attachPresenter() {
         presenter.onAttach(this);

        fullSearchPackage = new FullSearchPackage();

        mode=presenter.parseIntent(getIntent());

        switch (mode){
            case MODE_SHOW_MENU:
                //region Prepare Menu
                toolbarSearch.setVisibility(View.GONE);
                toolbarSubTitle.setVisibility(View.VISIBLE);
                setTitle(R.string.text_view_menu);
                Resto resto;
                try {
                    resto= (Resto) getIntent().getSerializableExtra(getString(R.string.key_serializable_extra));
                    toolbarSubTitle.setText(resto.getName());
                }catch (Exception e){
                    commonError(e.getMessage());
                    return;
                }
                fullSearchPackage.setPage(0);
                fullSearchPackage.setId(String.valueOf(resto.getId()));
                sendQuery();
                swipe.setEnabled(false);
                //endregion
                break;
            case MODE_SHOW_AND_SELECT_BEER:
                //region Prepare Beer
                id_text_empty=R.string.text_hint_search;
                fullSearchPackage.setType(Keys.TYPE_BEER);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_beer));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                //endregion
                break;
            case MODE_SHOW_AND_CREATE_BEER:
                //region Prepare Create Beer
                id_text_empty=R.string.text_hint_search;
                fullSearchPackage.setType(Keys.TYPE_BEER);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_beer));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                //endregion
                break;
            case MODE_SHOW_AND_CREATE_RESTO:
                //region Prepare Create Resto
                id_text_empty=R.string.text_hint_search;
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_resto));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                fillUserPosition();
                //endregion
                break;
            case MODE_SHOW_AND_SELECT_RESTO:
                //region Prepare Resto
                id_text_empty=R.string.text_hint_search;
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_resto));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                fillUserPosition();
                //endregion
                break;
            case MODE_SHOW_AND_SELECT_FRIENDS:
                //region Prepare Frends
                id_text_empty=R.string.text_hint_search;
                setTitle(R.string.action_find_friends);
                fullSearchPackage.setType(Keys.TYPE_USER);
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                //endregion
                break;
            case MODE_SHOW_HASHTAG:
                //region Prepare HashTag
                fullSearchPackage.setType(Keys.HASHTAG);
                toolbarSearch.setVisibility(View.GONE);
                try{
                    String strRequest=getIntent().getData().toString().replace("#","").replace("\n","");
                    if(strRequest.length()>0) {
                        prepareQuery(strRequest);
                        setTitle("Хэштег - "+strRequest);
                    }else
                        commonError();
                }catch (Exception e){
                    commonError(e.getMessage());
                }
                //endregion
                break;
            case MODE_SHOW_REVIEWS_BEER:
                //region Prepare ReviewBeer
                id_text_empty=R.string.text_view_while_empty_review;
                toolbarSearch.setVisibility(View.GONE);
                text_empty_list.setVisibility(View.GONE);
                setTitle(R.string.action_text_title_reviews);
                try {
                    int id_beer=Integer.valueOf(getIntent().getData().toString());
                    if(id_beer!=0)
                        presenter.loadReviewsBeer(id_beer);
                    else
                        commonError();
                }catch (Exception e){commonError(e.getMessage());}
                //endregion
                break;
            case MODE_SHOW_REVIEWS_RESTO:
                //region Prepare ReviewResto
                id_text_empty=R.string.text_view_while_empty_review;
                button_review_layout.setVisibility(View.VISIBLE);
                button_review_layout.setOnClickListener(this);
                toolbarSearch.setVisibility(View.GONE);
                text_empty_list.setVisibility(View.GONE);
                setTitle(R.string.action_text_title_reviews);
                try {
                    int id_resto=Integer.valueOf(getIntent().getData().toString());
                    presenter.loadReviewsResto(id_resto);
                }catch (Exception e){commonError(e.getMessage());}
                //endregion
                break;
            case MODE_SHOW_ALL_MY_EVALUATION:
                //region Prepare Evaluation
                id_text_empty=R.string.text_view_while_empty_evaluation;
                swipe.setEnabled(false);
                toolbarSearch.setVisibility(View.GONE);
                text_empty_list.setVisibility(View.GONE);
                setTitle(R.string.action_text_title_my_evaluation);
                presenter.loadMyEvaluation(0);
                //endregion
                break;
            default:
                commonError();
        }
        text_empty_list.setText(id_text_empty);

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
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK)
                    setResult(RESULT_OK,new Intent(String.valueOf(Actions.ACTION_VIEW_MODEL)));
                return;
            case REQUEST_PROFILE_FRIEND:
                if(resultCode==RESULT_OK) {
                    setResult(RESULT_OK, data);
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        toolbarTitle.setText(getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(getTitle());
    }
    //endregion

    //region Impl MultiListView
    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void appendItems(List<IFlexible> list) {

        adapter.clear();


        //region Prepare ButtonCreate and Items
        switch (mode) {
            case MODE_SHOW_AND_CREATE_RESTO: {
                String restoNameSearch = fullSearchPackage.getStringSearch();
                boolean restoNameFound=false;
                //region Prepare Items
                Iterator<IFlexible> iterator = list.iterator();
                ArrayList<SearchRestoInfo> arrayList = new ArrayList<>();
                while (iterator.hasNext()) {
                    SearchRestoInfo searchRestoInfo = ((SearchRestoInfo) iterator.next());
                    //region SearchRestoInfo switch to mode 1
                    arrayList.add(new SearchRestoInfo(searchRestoInfo.getModel(), 1));
                    //endregion
                    String restoName = searchRestoInfo.getModel().getName();
                    if (restoName.toLowerCase().equals(restoNameSearch.toLowerCase())) {
                        restoNameFound=true;
                    }
                }
                list = new ArrayList<>(arrayList);
                //endregion
                //region Prepare Button

                button_create.setVisibility(restoNameSearch.length()>=cntCharStartSearch?View.VISIBLE:View.GONE);
                button_create.setEnabled(!restoNameFound);
                button_create.storeNewRestoName(fullSearchPackage.getStringSearch());

                if(restoNameFound)
                    showSnackbarRed(getString(R.string.message_resto_exist, restoNameSearch));

                //endregion
            }
            break;
            case MODE_SHOW_AND_CREATE_BEER: {
                String beerNameSearch = fullSearchPackage.getStringSearch();
                boolean beerNameFound=false;
                //region Prepare Button
                Iterator<IFlexible> iterator = list.iterator();
                while (iterator.hasNext()) {
                    beerNameFound=((BeerInfo) iterator.next()).getModel().getTitle().toLowerCase().equals(beerNameSearch);
                    if(beerNameFound)
                        break;
                }

                button_create.setVisibility(beerNameSearch.length()>=cntCharStartSearch?View.VISIBLE:View.GONE);
                button_create.setEnabled(!beerNameFound);
                button_create.storeNewBeerName(fullSearchPackage.getStringSearch());
                if(beerNameFound)
                    showSnackbarRed(getString(R.string.message_beer_exist, beerNameSearch));

                //endregion
            }
            break;
        }
        //endregion

        adapter.addItems(0, list);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);

    }

    @Override
    public void onError() {
        swipe.setRefreshing(false);
    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();

    }

    @Override
    public void showProgress(boolean show) {

        //region Visible Progress
        if(show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
        //endregion

        //region Visible and content text show
        if(show) {
            text_empty_list.setVisibility(View.GONE);
        }else {
            text_empty_list.setVisibility(View.GONE);
            if(adapter.getItemCount()==0){
                text_empty_list.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(fullSearchPackage.getStringSearch())){
                    text_empty_list.setText(id_text_empty);
                    text_empty_list.setTextColor(Color.BLACK);
                }else if(fullSearchPackage.getStringSearch().length()<cntCharStartSearch){
                    text_empty_list.setText(R.string.text_more_chars);
                    text_empty_list.setTextColor(Color.BLUE);
                }else {
                    text_empty_list.setText(R.string.text_not_found_nothing);
                    text_empty_list.setTextColor(Color.RED);
                }
            }
        }
        //endregion

    }
    //endregion

    //region UserEvent
    @Override
    public void onClick(View view) {
        switch (mode){
            case MODE_SHOW_REVIEWS_RESTO:
                Starter.RestoDetailActivity_With_SCROLL(this,String.valueOf(getIntent().getData().toString()), RestoDetailView.ACTION_SCROLL_TO_ADD_REVIEWS);
                break;
        }
    }
    //endregion

    //region Functions
    private void prepareQuery(String stringSearch) {
        fullSearchPackage.setPage(0);
        fullSearchPackage.setStringSearch(stringSearch);
        sendQuery();
    }
    private void sendQuery() {

        try {
                //swipe.setRefreshing(true);
                switch (mode){
                    case MODE_SHOW_AND_SELECT_BEER:
                    case MODE_SHOW_AND_SELECT_RESTO:
                    case MODE_SHOW_AND_CREATE_RESTO:
                    case MODE_SHOW_AND_CREATE_BEER:
                    case MODE_SHOW_AND_SELECT_FRIENDS:
                        if(fullSearchPackage.getStringSearch().length() < cntCharStartSearch) {
                            fullSearchPackage.setPage(0);
                            appendItems(new ArrayList<>());
                            showProgress(false);
                        }else {
                            presenter.sendQueryFullSearch(fullSearchPackage);
                        }
                        break;
                    case MODE_SHOW_HASHTAG:
                        presenter.sentQueryQuickSearch(fullSearchPackage);
                        break;
                    case MODE_SHOW_MENU:
                        presenter.loadMenu(fullSearchPackage);
                        break;
                    default:
                        commonError();
                }
        }catch (Exception e){
            showMessage(e.getMessage());
        }
    }
    private void refreshItems() {
        sendQuery();
    }
    private void processAction(int action, Object payload) {
        switch (action){
            case Actions.ACTION_SELECT_MODEL: {
                switch (mode){
                    case MODE_SHOW_AND_SELECT_BEER:
                    case MODE_SHOW_AND_SELECT_RESTO:
                        Intent intent = new Intent(this, InterestListActivity.class);
                        intent.putExtra(getString(R.string.key_serializable_extra), (Serializable) payload);
                        intent.setAction(String.valueOf(Actions.ACTION_SELECT_MODEL));
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        processAction(Actions.ACTION_VIEW_MODEL, payload);
                }
            }break;
            case Actions.ACTION_VIEW_MODEL: {
                if(payload instanceof Resto){
                    Interest interest=new Interest();
                    Interest_info interest_info=new Interest_info();
                    interest_info.setId(String.valueOf(((Resto)payload).getId()));
                    interest.setInterest_info(interest_info);
                    Intent intent=new Intent(this, RestoDetailActivity.class);
                    intent.putExtra(Keys.RESTO_ID,interest);
                    startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
                }else if(payload instanceof Beer){
                    Intent intent=new Intent(this, BeerDetailActivity.class);
                    Interest interest=new Interest();
                    Interest_info interest_info=new Interest_info();
                    interest_info.setId(String.valueOf(((Beer)payload).getId()));
                    interest.setInterest_info(interest_info);
                    intent.putExtra(getString(R.string.key_serializable_extra),interest);
                    startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
                }
            }break;
        }
    }
    private void fillUserPosition() {
        requestCity(new Callback<City>() {
            @Override
            public void onResult(City city) {
                if(city!=null)
                    fullSearchPackage.setCity(String.valueOf(city.getId()));
                requestLastLocation(new Callback<Location>(){
                    @Override
                    public void onResult(Location location) {
                        if(location==null)
                            location=getDefaultLocation();
                        fullSearchPackage.setLat(location.getLatitude());
                        fullSearchPackage.setLon(location.getLongitude());
                    }
                } );
            }
        });
    }

    //endregion

}
