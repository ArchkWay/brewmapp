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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.data.entity.Location;
import com.brewmapp.data.pojo.GeolocatorResultPackage;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.tool.HashTagHelper;
import com.brewmapp.presentation.view.contract.ResultTask;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
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

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;

import ru.frosteye.ovsa.tool.TextTools;

public class NewPostActivity extends BaseActivity implements
        NewPostView,
        FlexibleAdapter.OnItemLongClickListener {


    //region BindView
    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_newPost_attach_file) View attactFile;
    @BindView(R.id.activity_newPost_attach_photo) View attachPhoto;
    @BindView(R.id.activity_newPost_attach_location) View attachLocation;
    @BindView(R.id.activity_newPost_settings) View settings;
    @BindView(R.id.activity_newPost_input) EditText input;
    @BindView(R.id.activity_newPost_inputTitle) EditText title;
    @BindView(R.id.activity_newPost_photos) RecyclerView photos;
    @BindView(R.id.activity_newPost_location) TextView location;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    //endregion

    //region Inject
    @Inject NewPostPresenter presenter;
    @Inject HashTagHelper hashTagHelper;
    @Inject ActiveBox activeBox;
    //endregion

    //region Private
    private FlexibleAdapter<PhotoPreviewInfo> adapter;
    private Post post = new Post();
    private boolean inputChangeLocked = false;
    //endregion

    //region Impl NewPostActivity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        processBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                post.setHashTag(hashTagHelper.getSingleHashTag(post.getText()));
                highlightHashTag();
                post.setRepost_id(getIntent().getStringExtra(getString(R.string.key_repost_id)));
                post.setRepost_model(getIntent().getStringExtra(getString(R.string.key_repost_model)));
                presenter.onPostReady(post, new ResultTask() {
                    @Override
                    public void onError(Throwable e) {
                        showMessage(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        complete();
                    }
                });
                return false;
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getTitle());

        title.setVisibility(View.GONE);
        title.setText(R.string.without_tite);

        enableBackButton();
        registerTextChangeListeners(s -> {
            if(inputChangeLocked) return;
            post.setText(TextTools.extractTrimmed(input));
            post.setName(TextTools.extractTrimmed(title));
            invalidateOptionsMenu();
        }, input, title);
        photos.setNestedScrollingEnabled(false);
        attachLocation.setOnClickListener(v -> {
            //startActivityForResult(new Intent(this, PickLocationActivity.class),RequestCodes.REQUEST_PICK_LOCATION);
            requestLastLocation(new Callback<android.location.Location>() {
                @Override
                public void onResult(android.location.Location location) {
                    if(location!=null) {
                        DialogSelectAddress dialogSelectAddress = new DialogSelectAddress();
                        dialogSelectAddress.setLocation(new Location(location));
                        dialogSelectAddress.showDialog(getSupportFragmentManager(), new DialogSelectAddress.OnSelectAddress() {
                            @Override
                            public void onOk(Location location) {
                                Intent intent = new Intent();
                                ru.frosteye.ovsa.data.entity.SimpleLocation simpleLocation = new ru.frosteye.ovsa.data.entity.SimpleLocation(location.getLocation().getLat(), location.getLocation().getLon());
                                GeolocatorResultPackage geolocatorResultPackage = new GeolocatorResultPackage(location.getFormatLocation(), simpleLocation);
                                intent.putExtra(Keys.LOCATION, geolocatorResultPackage);
                                onActivityResult(RequestCodes.REQUEST_PICK_LOCATION, RESULT_OK, intent);
                            }
                        });
                    }
                }
            });
        });
        attachPhoto.setOnClickListener(v -> takePhoto());
        attactFile.setOnClickListener(v -> takeFromGallery());
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewPostSettingsActivity.class);
            intent.putExtra(Keys.POST, post);
            startActivityForResult(intent, RequestCodes.REQUEST_PICK_POST_SETTINGS);
        });
        photos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlexibleAdapter<>(new ArrayList<>(), this);
        photos.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case FilePickerConst.REQUEST_CODE_PHOTO:
                    if(data != null) {
                        List<String> photoPaths = new ArrayList<>();
                        photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                        for(String path: photoPaths) {
                            addPhoto(new File(path));
                        }
                    }
                    break;
                case RequestCodes.REQUEST_PICK_LOCATION:
                    GeolocatorResultPackage result = ((GeolocatorResultPackage) data.getSerializableExtra(Keys.LOCATION));
                    post.setLat(result.getLocation().getLat());
                    post.setLon(result.getLocation().getLng());
                    location.setVisibility(View.VISIBLE);
                    location.setText(result.getAddress());
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
    //endregion

    //region Impl NewPostView
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
    public void addPhoto(PhotoPreviewInfo photo) {
        photos.setVisibility(View.VISIBLE);
        post.getPhotoIds().add(photo.getModel().getId());
        adapter.addItem(photo);
        invalidateOptionsMenu();
    }

    @Override
    public void complete() {
        setResult(RESULT_OK);
        showMessage(getString(R.string.post_created));
        finish();
    }

    @Override
    public void onItemLongClick(int position) {
//        post.getPhotoIds().remove(position);
        post.getFilesToUpload().remove(position);
        adapter.removeItem(position);
        invalidateOptionsMenu();
        if(adapter.getItemCount() == 0) photos.setVisibility(View.GONE);
    }
    //endregion

    //region Functions
    private void highlightHashTag() {
        inputChangeLocked = true;
        input.setText(hashTagHelper.formatNewPost(TextTools.extract(input)));
        input.setSelection(TextTools.extract(input).length());
        inputChangeLocked = false;
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
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setActivityTheme(R.style.AppThemeWithActionBar)
                .pickPhoto(this);
        /*RxPaparazzo.single(this)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() != RESULT_OK) {
                        return;
                    }
                    response.targetUI().addPhoto(response.data().getFile());
                });*/
    }

    public void addPhoto(File file) {
        photos.setVisibility(View.VISIBLE);
        UploadPhotoResponse newItem = new UploadPhotoResponse(file);
        post.getFilesToUpload().add(newItem);
        adapter.addItem(new PhotoPreviewInfo(newItem));
        invalidateOptionsMenu();
//        presenter.onUploadPhotoRequest(file);
    }

    private void processBack() {
//        if(!post.isStarted()) {
        finish();
//            return;
//        }
//        Confirm.create(this)
//                .title(R.string.confirm_stop_edit)
//                .message(R.string.confirm_cancel_post)
//                .yes(R.string.stay, (dialog, which) -> {
//
//                })
//                .no(R.string.leave, (dialog, which) -> finish()).show();
    }

    //endregion

}
