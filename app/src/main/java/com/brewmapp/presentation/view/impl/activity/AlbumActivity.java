package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.data.entity.wrapper.PhotoInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AlbumPresenter;
import com.brewmapp.presentation.view.contract.AlbumView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;

public class AlbumActivity extends BaseActivity implements AlbumView {

    @BindView(R.id.activity_album_list) RecyclerView list;
    @BindView(R.id.activity_album_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.common_toolbar) Toolbar toolbar;

    @Inject AlbumPresenter presenter;

    private int albumId;
    private String albumTitle;
    private FlexibleAdapter<PhotoInfo> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    @Override
    protected void initView() {
        this.albumId = getIntent().getIntExtra(Keys.ALBUM_ID, 0);
        this.albumTitle = getIntent().getStringExtra(Keys.ALBUM_TITLE);
        setTitle(albumTitle);
        enableBackButton();
        swipe.setOnRefreshListener(() -> presenter.onRequestPhotos(albumId));
        adapter = new FlexibleAdapter<>(new ArrayList<>(), this);
        list.setLayoutManager(new GridLayoutManager(this, 3));
        list.setAdapter(adapter);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
        if(enabled) swipe.setRefreshing(false);
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
            showSelect(this, R.array.avatar_options_mini, (text, position) -> {
                switch (position) {
                    case 0:
                        takeFromGallery();
                        break;
                    case 1:
                        takePhoto();
                        break;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void takePhoto() {
        RxPaparazzo.single(this)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.targetUI().onImageReady(response.data().getFile());
                });
    }

    private void takeFromGallery() {
        RxPaparazzo.single(this)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.targetUI().onImageReady(response.data().getFile());
                });
    }

    public void onImageReady(File file) {
        presenter.onUploadPhoto(albumId, file);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.onRequestPhotos(albumId);
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
    public void showPhotos(AlbumPhotos photos) {
        adapter.updateDataSet(photos.getModels());
    }
}
