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
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;
import com.brewmapp.presentation.view.contract.InterestListView;
import com.brewmapp.presentation.view.impl.widget.InterestView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_INTEREST;

public class    InterestListListActivity extends BaseActivity implements InterestListView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_interest_list)  RecyclerView recyclerView;
    @BindView(R.id.activity_interest_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.activity_interest_text_save)    TextView text_save_intesest;

    @Inject    InterestListPresenter presenter;

    private FlexibleModelAdapter<IFlexible> adapter;
    private LoadInterestPackage loadInterestPackage;
    private ArrayList<Serializable> serializableArrayListAdd =new ArrayList<>();
    private ArrayList<Interest> interestArrayListRemove =new ArrayList<>();

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
                interestArrayListRemove.add(((InterestView)((ModelViewHolder) viewHolder).getFrontView()).getModel());
                visibleTextSave();
                //adapter.removeItemWithDelay();
                //((ModelViewHolder) viewHolder).getFrontView().getModel()
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
        text_save_intesest.setOnClickListener(v->{presenter.addInterest(serializableArrayListAdd);presenter.removeInterest(interestArrayListRemove);});

        sendQueryListInterests();

        visibleTextSave();
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
                startActivityForResult(new Intent(loadInterestPackage.getFilterInterest(),null,this, AddInterestActivity.class), REQUEST_INTEREST);
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
                    serializableArrayListAdd.add(data.getSerializableExtra(getString(R.string.key_serializable_extra)));
                    visibleTextSave();
                    return;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void visibleTextSave() {
        if(serializableArrayListAdd.size()==0&& interestArrayListRemove.size()==0)
            text_save_intesest.setVisibility(View.GONE);
        else
            text_save_intesest.setVisibility(View.VISIBLE);
    }

    private void processAction(int action, Object payload) {

    }

    @Override
    public void refreshItems() {
        swipe.setRefreshing(true);
        sendQueryListInterests();
    }


    public void sendQueryListInterests() {
        swipe.setRefreshing(true);
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
