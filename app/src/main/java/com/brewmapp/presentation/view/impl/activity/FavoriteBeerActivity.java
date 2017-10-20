package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.FavoriteBeerPresenter;
import com.brewmapp.presentation.view.contract.FavoriteBeerView;
import com.brewmapp.utils.Cons;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class FavoriteBeerActivity extends BaseActivity implements FavoriteBeerView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @Inject    FavoriteBeerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_beer);
    }


    @Override
    protected void initView() {
        enableBackButton();
    }

    @Override
    protected void attachPresenter() {

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
                startActivity(new Intent(Cons.SEARCH_BEER,null,this, AddFavoriteBeerActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected void inject(PresenterComponent component) {

    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

}
