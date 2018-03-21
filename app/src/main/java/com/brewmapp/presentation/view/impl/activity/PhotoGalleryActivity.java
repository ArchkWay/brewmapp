package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.execution.task.UploadPhotoTask;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.view.contract.PhotoGalleryActivityView;
import com.brewmapp.presentation.view.impl.widget.GalleryItemPhoto;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.brewmapp.execution.exchange.request.base.Keys.RESTO_ID;

public class PhotoGalleryActivity extends BaseActivity implements PhotoGalleryActivityView {


    //region BindView
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.common_toolbar_title)    TextView common_toolbar_title;
    @BindView(R.id.common_toolbar_subtitle)    TextView common_toolbar_subtitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout common_toolbar_dropdown;
    @BindView(R.id.activity_gallery_list)    RecyclerView activity_gallery_list;
    //endregion

    @Inject    RestoDetailPresenter presenter;

    //region Private
    private FlexibleModelAdapter<IFlexible> adapter;
    private ArrayList<IFlexible> content_gallery_list=new ArrayList<>();
    private ArrayList<Photo> photoArrayList;
    //endregion

    //region PhotoGalleryActivity Impl
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add) {
            showSelect(this, R.array.avatar_options_mini, (text, position) -> {
                switch (position) {
                    case 0:takeFromGallery(); break;
                    case 1:takePhoto(); break;
                }
            });
            return true;
        };
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        enableBackButton();
        common_toolbar_dropdown.setVisibility(View.VISIBLE);
        common_toolbar_subtitle.setVisibility(View.GONE);
        if(getResources().getConfiguration().orientation!=ORIENTATION_PORTRAIT)
            toolbar.setVisibility(View.GONE);
        else
            setTitle(getString(R.string.title_activity_photo_gallery));
        adapter=new FlexibleModelAdapter<>(content_gallery_list,this::clickItemGallery);
        activity_gallery_list.setLayoutManager(new GridLayoutManager(this,3));
        activity_gallery_list.setAdapter(adapter);

    }

    @Override
    protected void attachPresenter() {
        photoArrayList= (ArrayList<Photo>) getIntent().getSerializableExtra(getString(R.string.key_photo_array_list));
        if(photoArrayList==null){
            commonError();
        }else if(photoArrayList.size()==0){
            commonError();
        }else {
            Iterator<Photo>  iterator=photoArrayList.iterator();
            while (iterator.hasNext())
            content_gallery_list.add(new ItemPhoto(iterator.next()) );
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        common_toolbar_title.setText(getTitle());
    }

    //endregion

    //region PhotoGalleryActivityView
    @Override
    public void enableControls(boolean b, int i) {

    }
    //endregion

    //region Functions
    public void commonError(String... strings) {
        if(strings!=null && strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    private void clickItemGallery(int action, Object payload){
        PhotoSliderActivity.startPhotoSliderActivity(photoArrayList,this);
    }

    private void takePhoto() {

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

    void onImageReady(File file){
        Intent intent=new Intent();
        Interest interest=new Interest();
        Interest_info interest_info=new Interest_info();
        interest_info.setId(getIntent().getStringExtra(getString(R.string.key_id_model)));
        interest.setInterest_info(interest_info);
        intent.putExtra(RESTO_ID,interest);
        if(interest_info.getId()!=null) {
            presenter.parseIntent(intent);
            presenter.uploadPhoto(file, new Callback<Integer>() {
                @Override
                public void onResult(Integer result) {
                    switch (result) {
                        case RESULT_OK:
                            showSnackbar(getString(R.string.text_photo_sended_to_moderation));
                            break;
                        case RESULT_CANCELED:
                            showSnackbarRed(getString(android.R.string.cancel));
                            break;
                    }
                }
            });
        }
    }

    //endregion

    public class ItemPhoto extends AdapterItem<Photo, GalleryItemPhoto> {

        public ItemPhoto(Photo photo) {
            setModel(photo);
        }

        @Override
        public int getLayoutRes() {
            return R.layout.view_gallery_item;
        }
    }


}
