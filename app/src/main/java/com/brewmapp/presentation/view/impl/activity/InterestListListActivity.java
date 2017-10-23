package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Product;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;
import com.brewmapp.presentation.view.contract.InterestListView;
import com.brewmapp.utils.Cons;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_INTEREST;

public class InterestListListActivity extends BaseActivity implements InterestListView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_interest_list)  RecyclerView recyclerView;
    @BindView(R.id.activity_interest_swipe)    RefreshableSwipeRefreshLayout swipe;

    @Inject    InterestListPresenter presenter;

    private FlexibleModelAdapter<IFlexible> adapter;
    private LoadInterestPackage loadInterestPackage;

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
        swipe.setOnRefreshListener(this::refreshItems);
        loadInterestPackage =new LoadInterestPackage();
        loadInterestPackage.setPage(0);
        sendQuery();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                startActivityForResult(new Intent(Cons.SEARCH_BEER,null,this, AddInterestActivity.class), REQUEST_INTEREST);
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
                    Product product= (Product) data.getSerializableExtra(getString(R.string.key_serializable_extra));
                    return;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processAction(int action, Object payload) {

    }

    private void refreshItems() {
        swipe.setRefreshing(true);
        sendQuery();
    }

    private void sendQuery() {
        presenter.sendQuery(loadInterestPackage);
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
