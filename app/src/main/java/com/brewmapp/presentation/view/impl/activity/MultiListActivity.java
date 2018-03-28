package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.widget.FinderView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
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
    @BindView(R.id.activity_add_interest_text_info_empty_list)    TextView start_search;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.mulilist_activity_button_review_layout)    LinearLayout button_review_layout;
    //endregion

    //region Inject
    @Inject    MultiListPresenter presenter;
    //endregion

    //region Private
    private FlexibleModelAdapter<IFlexible> adapter;
    private FullSearchPackage fullSearchPackage;
    private EndlessRecyclerOnScrollListener scrollListener;
    private String mode;
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
                fullSearchPackage.setPage(currentPage-1);
                sendQuery();
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
                start_search.setText(R.string.text_hint_search);
                fullSearchPackage.setType(Keys.TYPE_BEER);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_beer));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                //endregion
                break;
            case MODE_SHOW_AND_SELECT_RESTO:
                //region Prepare Resto
                start_search.setText(R.string.text_hint_search);
                fullSearchPackage.setType(Keys.TYPE_RESTO);
                setTitle(R.string.action_find_beer);
                finder.setHintString(getString(R.string.hint_find_resto));
                finder.setListener(string -> prepareQuery(string));
                recyclerview.addOnScrollListener(scrollListener);
                //endregion
                break;
            case MODE_SHOW_AND_SELECT_FRIENDS:
                //region Prepare Frends
                start_search.setText(R.string.text_hint_search);
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
                start_search.setText(R.string.text_view_while_empty_review);
                toolbarSearch.setVisibility(View.GONE);
                start_search.setVisibility(View.GONE);
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
                start_search.setText(R.string.text_view_while_empty_review);
                button_review_layout.setVisibility(View.VISIBLE);
                button_review_layout.setOnClickListener(this);
                toolbarSearch.setVisibility(View.GONE);
                start_search.setVisibility(View.GONE);
                setTitle(R.string.action_text_title_reviews);
                try {
                    int id_resto=Integer.valueOf(getIntent().getData().toString());
                    presenter.loadReviewsResto(id_resto);
                }catch (Exception e){commonError(e.getMessage());}
                //endregion
                break;
            case MODE_SHOW_ALL_MY_EVALUATION:
                //region Prepare Evaluation
                start_search.setText(R.string.text_view_while_empty_evaluation);
                swipe.setEnabled(false);
                toolbarSearch.setVisibility(View.GONE);
                start_search.setVisibility(View.GONE);
                setTitle(R.string.action_text_title_my_evaluation);
                presenter.loadMyEvaluation(0);
                //endregion
                break;
            default:
                commonError();
        }

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
        adapter.addItems(0,list);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);

        start_search.setVisibility(adapter.getItemCount()==0?View.VISIBLE:View.GONE);
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
                    case MODE_SHOW_AND_SELECT_FRIENDS:
                        if(fullSearchPackage.getStringSearch().length() == 0) {
                            fullSearchPackage.setPage(0);
                            appendItems(new ArrayList<>());
                        }else
                            presenter.sendQueryFullSearch(fullSearchPackage);
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
                Intent intent = new Intent(this, InterestListActivity.class);
                intent.putExtra(getString(R.string.key_serializable_extra), (Serializable) payload);
                intent.setAction(String.valueOf(Actions.ACTION_SELECT_MODEL));
                setResult(RESULT_OK, intent);
                finish();
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

    @Override
    public void onClick(View view) {
        switch (mode){
            case MODE_SHOW_REVIEWS_RESTO:
                Starter.RestoDetailActivity_With_SCROLL(this,String.valueOf(getIntent().getData().toString()), RestoDetailView.ACTION_SCROLL_TO_ADD_REVIEWS);
                break;
        }
    }
    //endregion

}
