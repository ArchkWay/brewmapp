package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.container.Albums;
import com.brewmapp.data.entity.wrapper.AlbumInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AlbumsPresenter;
import com.brewmapp.presentation.view.contract.AlbumsView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;

import ru.frosteye.ovsa.presentation.view.dialog.Confirm;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.stub.listener.SelectListener;

public class AlbumsActivity extends BaseActivity implements AlbumsView, FlexibleAdapter.OnItemClickListener,
        FlexibleAdapter.OnItemLongClickListener {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_list_list) RecyclerView list;
    @BindView(R.id.activity_list_swipe) SwipeRefreshLayout swipe;

    @Inject AlbumsPresenter presenter;

    private FlexibleAdapter<AlbumInfo> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(this, AddAlbumActivity.class), RequestCodes.REQUEST_CREATE_ALBUM);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            presenter.onLoadAlbums();
        }
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        if(enabled) {
            if(code == CONTROLS_CODE_PROGRESS) {
                showTopBarLoading(true);
                swipe.setRefreshing(false);
            } else {
                showTopBarLoading(false);
                swipe.setRefreshing(false);
            }
        } else {
            showTopBarLoading(true);
        }
    }

    @Override
    protected void initView() {
        enableBackButton();
        swipe.setOnRefreshListener(() -> presenter.onLoadAlbums());
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new ListDivider(this, ListDivider.VERTICAL_LIST));
        adapter = new FlexibleAdapter<>(new ArrayList<>(), this);
        list.setAdapter(adapter);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.onLoadAlbums();
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
    public void showAlbums(Albums albums) {
        adapter.updateDataSet(albums.getModels());
    }

    @Override
    public boolean onItemClick(int position) {
        AlbumInfo albumInfo = adapter.getItem(position);
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra(Keys.ALBUM_ID, albumInfo.getModel().getId());
        intent.putExtra(Keys.ALBUM_TITLE, albumInfo.getModel().getName());
        startActivity(intent);
        return false;
    }

    @Override
    public void onItemLongClick(int position) {
        AlbumInfo albumInfo = adapter.getItem(position);
        showSelect(this, R.array.album_actions, (text, position1) -> {
            switch (position1) {
                case 0:
                    Intent intent = new Intent(this, AddAlbumActivity.class);
                    intent.putExtra(Keys.ALBUM, new Album.SimpleAlbum(albumInfo.getModel()));
                    startActivityForResult(intent, RequestCodes.REQUEST_EDIT_ALBUM);
                    break;
                case 1:
                    Confirm.create(this)
                            .title(R.string.album_removing)
                            .message(R.string.are_you_sure)
                            .yes(R.string.yes, (dialog, which) -> presenter.onRemoveAlbum(albumInfo.getModel()))
                            .no(R.string.ovsa_string_cancel, (dialog, which) -> dialog.cancel()).show();
                    break;
            }
        });
    }
}
