package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.makeramen.roundedimageview.RoundedImageView;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.pojo.RegisterPackage;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.tool.TextTools;

public class RegisterActivity extends BaseActivity  {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_register_lastName) EditText lastName;
    @BindView(R.id.activity_register_name) EditText firstName;
    @BindView(R.id.activity_register_segmented) SegmentedGroup segmented;
    @BindView(R.id.activity_register_avatar) RoundedImageView avatar;
    @BindView(R.id.activity_register_avatar_placeholder) View placeholder;

    @Inject RegisterPackage registerPackage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }



    @Override
    protected void initView() {
        enableBackButton();
        registerTextChangeListeners(s -> {
            registerPackage.setFirstName(TextTools.extractTrimmed(firstName));
            registerPackage.setLastName(TextTools.extractTrimmed(lastName));
            invalidateOptionsMenu();
        }, firstName, lastName);
        segmented.setOnCheckedChangeListener((group, checkedId) -> {
            registerPackage.setGender(checkedId == R.id.activity_register_man ? 1 : 2);
            invalidateOptionsMenu();
        });
        avatar.setOnClickListener(v -> {
            showSelect(this, R.array.avatar_options, (text, position) -> {
                switch (position) {
                    case 0:
                        takeFromGallery();
                        break;
                    case 1:
                        takePhoto();
                        break;
                    case 2:
                        avatar.setImageDrawable(null);
                        registerPackage.setAvatarPath(null);
                        break;
                }
            });
        });
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
                    response.targetUI().showAvatar(response.data().getFile());
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
                    response.targetUI().showAvatar(response.data().getFile());
                });
    }

    private void showAvatar(File file) {
        registerPackage.setAvatarPath(file.getPath());
        Picasso.with(this).load(file).fit().into(avatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_next).setEnabled(registerPackage.validate());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_next) {
            Intent intent = new Intent(this, EnterPhoneActivity.class);
            intent.putExtra(RegisterPackage.KEY, registerPackage);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachPresenter() {
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }
}
