package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.wrapper.PhotoPreviewInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.NewPostPresenter;
import com.brewmapp.presentation.view.contract.NewPostView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.dialog.Confirm;
import ru.frosteye.ovsa.tool.TextTools;

public class NewPostActivity extends BaseActivity implements NewPostView, FlexibleAdapter.OnItemLongClickListener {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_newPost_attach_file) View attactFile;
    @BindView(R.id.activity_newPost_attach_photo) View attachPhoto;
    @BindView(R.id.activity_newPost_attach_location) View attachLocation;
    @BindView(R.id.activity_newPost_settings) View settings;
    @BindView(R.id.activity_newPost_input) EditText input;
    @BindView(R.id.activity_newPost_inputTitle) EditText title;
    @BindView(R.id.activity_newPost_photos) RecyclerView photos;

    @Inject NewPostPresenter presenter;

    private FlexibleAdapter<PhotoPreviewInfo> adapter;
    private Post post = new Post();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
        settings.setEnabled(enabled);
        attachPhoto.setEnabled(enabled);
        title.setEnabled(enabled);
        attachLocation.setEnabled(enabled);
        attactFile.setEnabled(enabled);
        input.setEnabled(enabled);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        processBack();
    }

    private void processBack() {
        if(!post.isStarted()) {
            finish();
            return;
        }
        Confirm.create(this)
                .title(R.string.confirm_stop_edit)
                .message(R.string.confirm_cancel_post)
                .yes(R.string.stay, (dialog, which) -> {

                })
                .no(R.string.leave, (dialog, which) -> finish()).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                presenter.onPostReady(post);
                return true;
            case android.R.id.home:
                processBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        menu.findItem(R.id.action_send).setEnabled(post.validate());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initView() {
        enableBackButton();
        registerTextChangeListeners(s -> {
            post.setText(TextTools.extractTrimmed(input));
            post.setName(TextTools.extractTrimmed(title));
            invalidateOptionsMenu();
        }, input, title);
        attachLocation.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, PickLocationActivity.class),
                    RequestCodes.REQUEST_PICK_LOCATION);
        });
        attachPhoto.setOnClickListener(v -> takePhoto());
        attactFile.setOnClickListener(v -> takeFromGallery());
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostSettingsActivity.class);
            intent.putExtra(Keys.POST, post);
            startActivityForResult(intent, RequestCodes.REQUEST_PICK_POST_SETTINGS);
        });
        photos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new FlexibleAdapter<>(new ArrayList<>(), this);
        photos.setAdapter(adapter);
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
                    response.targetUI().addPhoto(response.data().getFile());
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
                    response.targetUI().addPhoto(response.data().getFile());
                });
    }

    public void addPhoto(File file) {
        presenter.onUploadPhotoRequest(file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.REQUEST_PICK_LOCATION:
                    double lat = data.getDoubleExtra(Keys.LAT, 0);
                    double lng = data.getDoubleExtra(Keys.LNG, 0);
                    post.setLat(lat);
                    post.setLon(lng);
                    break;
                case RequestCodes.REQUEST_PICK_POST_SETTINGS:
                    boolean friendsOnly = data.getBooleanExtra(Keys.FRIENDS_ONLY, false);
                    Date delayedDate = ((Date) data.getSerializableExtra(Keys.DELAYED_DATE));
                    post.setDelayedDate(delayedDate);
                    post.setFriendsOnly(friendsOnly);
                    break;
            }
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
    public void addPhoto(PhotoPreviewInfo photo) {
        photos.setVisibility(View.VISIBLE);
        post.getPhotoIds().add(photo.getModel().getId());
        adapter.addItem(photo);
    }

    @Override
    public void complete() {
        setResult(RESULT_OK);
        showMessage(getString(R.string.post_created));
        finish();
    }

    @Override
    public void onItemLongClick(int position) {
        post.getPhotoIds().remove(position);
        adapter.removeItem(position);
        if(adapter.getItemCount() == 0) photos.setVisibility(View.GONE);
    }
}
