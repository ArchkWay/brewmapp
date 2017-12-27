package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;
import com.brewmapp.presentation.view.contract.InterestListView;
import com.brewmapp.presentation.view.impl.widget.InterestView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_INTEREST;


public class InterestListActivity extends BaseActivity implements InterestListView{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_interest_list)  RecyclerView recyclerView;
    @BindView(R.id.activity_interest_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_interest_text_save)    TextView text_save_intesest;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;

    @Inject    InterestListPresenter presenter;

    private FlexibleModelAdapter<IFlexible> adapter;
    private LoadInterestPackage loadInterestPackage;
    private HashMap<Serializable,Serializable> hmAdd =new HashMap<>();
    private HashMap<Interest,Interest> hmRemove =new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_list);
    }

    @Override
    protected void initView() {

        enableBackButton();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter= new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                try {
                    for(int i=0;i<adapter.getItemCount();i++){
                        Interest cur=((InterestInfo)adapter.getItem(i)).getModel();
                        Interest swipe=((InterestView)((ModelViewHolder) viewHolder).getFrontView()).getModel();
                        if(cur==swipe){
                            adapter.removeItem(i);
                            hmRemove.put(cur,cur);
                            visibleTextSave();
                            return;
                        }
                    }
                }catch (Exception e){

                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipe.setOnRefreshListener(this::refreshItems);
        loadInterestPackage =new LoadInterestPackage();
        loadInterestPackage.setPage(0);
        loadInterestPackage.setFilterInterest(getIntent().getAction());
        switch (loadInterestPackage.getFilterInterest()){
            case Keys.CAP_BEER:
                setTitle(R.string.fav_beer);
                break;
            case Keys.CAP_RESTO:
                setTitle(R.string.fav_bars);
                break;
            default: {
                finish();
                return;
            }
        }
        text_save_intesest.setOnClickListener(v->{
            presenter.storeInterest(hmAdd,hmRemove);hmAdd.clear();hmRemove.clear();
        });

        toolbarTitle.setText(getSupportActionBar().getTitle());
        toolbarSubTitle.setVisibility(View.GONE);

        refreshInterests();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivityForResult(new Intent(loadInterestPackage.getFilterInterest(),null,this, MultiListActivity.class), REQUEST_INTEREST);
            default:
                return super.onOptionsItemSelected(item);
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
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_INTEREST:
                if(resultCode==RESULT_OK){
                    switch (Integer.valueOf(data.getAction())){
                        case Actions.ACTION_VIEW_MODEL:
                            setResult(RESULT_OK);
                            refreshInterests();
                            return;
                        case Actions.ACTION_SELECT_MODEL:
                            Serializable serializable = data.getSerializableExtra(getString(R.string.key_serializable_extra));
                            if (serializable instanceof Beer) {
                                Beer beer = (Beer) serializable;
                                presenter.requestOneBeer(beer.getId());
                            } else if (serializable instanceof Resto) {
                                Resto resto = (Resto) serializable;
                                presenter.requestOneResto(resto.getId());
                            } else {
                                return;
                            }
                            return;
                    }
                }
                break;
            case REQUEST_CODE_REFRESH_ITEMS:{
                if(resultCode==RESULT_OK) {
                    setResult(RESULT_OK);
                    refreshInterests();
                }
            }break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void visibleTextSave() {
        if(hmAdd.size()==0&& hmRemove.size()==0)
            text_save_intesest.setVisibility(View.INVISIBLE);
        else
            text_save_intesest.setVisibility(View.VISIBLE);
    }

    private void processAction(int action, Object payload) {
        Interest interest=(Interest)payload;
        switch (interest.getRelated_model()){
            case Keys.CAP_RESTO:{
                Intent intent = new Intent(this, RestoDetailActivity.class);
                intent.putExtra(Keys.RESTO_ID, interest);
                startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
            }break;
            case Keys.CAP_BEER:{
                Intent intent = new Intent(this, BeerDetailActivity.class);
                intent.putExtra(getString(R.string.key_serializable_extra), interest);
                startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
            }
        }
    }

    @Override
    public void refreshItems() {
        swipe.setRefreshing(true);
        refreshInterests();
    }

    @Override
    public void addOneItemBeer(Beer beer) {
        hmAdd.put(beer, beer);
        adapter.addItem(new InterestInfo(beer));
        adapter.notifyDataSetChanged();
        visibleTextSave();
    }

    @Override
    public void addOneItemResto(Resto resto) {
            hmAdd.put(resto, resto);
            adapter.addItem(new InterestInfo(resto));
            adapter.notifyDataSetChanged();
        visibleTextSave();
    }

    public void refreshInterests() {
        hmAdd.clear();hmRemove.clear();
        swipe.setRefreshing(true);
        visibleTextSave();
        presenter.requestInterests(loadInterestPackage);
    }

    @Override
    public void appendItems(List<IFlexible> iFlexibles) {
        swipe.setRefreshing(false);

        if(loadInterestPackage.getPage()==0)
            adapter.clear();
        adapter.addItems(adapter.getItemCount(), iFlexibles);
        adapter.notifyDataSetChanged();


    }


    @Override
    public void onError() {
        swipe.setRefreshing(false);
    }
}
