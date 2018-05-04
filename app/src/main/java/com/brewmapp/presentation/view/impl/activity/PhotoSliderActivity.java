package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.PhotoSliderPresenter;
import com.brewmapp.presentation.view.contract.PhotoSliderView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.CustomSliderView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class PhotoSliderActivity extends BaseActivity implements PhotoSliderView {

    //region BindView
    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    //@BindView(R.id.activity_photos_indicator) PagerIndicator indicator;
    @BindView(R.id.activity_photos_slider) SliderLayout slider;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    //endregion

    //region Inject
    @Inject PhotoSliderPresenter presenter;
    //endregion

    private int size_photo;

    //region PhotoSliderActivity Impl
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Bitmap bitmap=((CustomSliderView)slider.getCurrentSlider()).getBitmap();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String tempFile=Environment.getExternalStorageDirectory()
                        + File.separator
                        +"temporary_file.jpg"
//                        + new File((slider.getCurrentSlider()).getUrl()).getName()
//                        +".jpg"
                        ;
                File f = new File(tempFile);
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+f.getAbsolutePath()));
                //share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, getString(R.string.send_photo)));


//                slider.getCurrentSlider()
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_STREAM, text);
//                sendIntent.setType("image/jpeg");
//                startActivity(sendIntent);

                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);

        enableBackButton();
        slider.stopAutoCycle();
        String[] urls = getIntent().getStringArrayExtra(Keys.PHOTOS);

        ArrayList<Photo> photoArrayList = (ArrayList<Photo>) getIntent().getExtras().getSerializable(Keys.PHOTO_COUNT);
        try {
            int i=0;
            for(String url: urls) {
                CustomSliderView customSliderView;
                if(photoArrayList==null)
                    customSliderView=new CustomSliderView(this, url);
                else
                    customSliderView = new CustomSliderView(this, url,photoArrayList.get(i++));

                slider.addSlider(customSliderView);
            }
            size_photo = photoArrayList.size();
        }catch (Exception e){
            showMessage(getString(R.string.enter));
            finish();
        }
        slider.getPagerIndicator().setVisibility(View.GONE);
        slider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbarTitle.setText(
                        String.format("%s из %s",String.valueOf(position+1),String.valueOf(size_photo))
                );

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slider.setCurrentPosition(
                getIntent().getIntExtra(getString(R.string.key_photo_selected),0)
        );

        if(getResources().getConfiguration().orientation!=ORIENTATION_PORTRAIT)
            toolbar.setVisibility(View.GONE);

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

    //region Static
    public static void startPhotoSliderActivity(List<Photo> photos, Context context) {
        try {
            int selected=0;
            String[] urls=new String[photos.size()];
            for(int i=0;i<photos.size();i++) {
                urls[i] = photos.get(i).getUrl();
                if(urls[i]==null)
                    urls[i]=photos.get(i).getThumb().getUrl();
                if(photos.get(i).isSelected())
                    selected=i;
            }
            if(urls.length>0){
                Intent intent = new Intent(context, PhotoSliderActivity.class);
                intent.putExtra(Keys.PHOTOS, urls);
                intent.putExtra(Keys.PHOTO_COUNT,  new ArrayList<>(photos));
                intent.putExtra(context.getString(R.string.key_photo_selected),selected);
                context.startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void startPhotoSliderActivity(String path, Context context) {
        ArrayList<Photo> photoArrayList=new ArrayList<>();
        Photo photo=new Photo();
        photo.setUrl(path);
        photoArrayList.add(photo);
        startPhotoSliderActivity(photoArrayList, context);
    }
    //endregion

}
