package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.brewmapp.R;
import com.brewmapp.data.entity.Photo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 21.03.2018.
 */

public class GalleryItemPhoto extends BaseLinearLayout implements ModelView<Photo> , InteractiveModelView<Photo>{

    @BindView(R.id.gallery_image)    ImageView gallery_image;
    @BindView(R.id.gallery_progress)    ProgressBar gallery_progress;

    private Photo model;
    private Target target;
    public GalleryItemPhoto(Context context) {
        super(context);
    }

    public GalleryItemPhoto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryItemPhoto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GalleryItemPhoto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setModel(Photo model) {
        this.model=model;
        target=new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                gallery_progress.setVisibility(INVISIBLE);
                gallery_image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                errorLoad();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                gallery_progress.setVisibility(VISIBLE);
            }
        };
        try {
            String url=model.getThumb().getThumbUrl();
            if(url==null||url.length()==0)
                errorLoad();
            else
                Picasso.with(getContext()).load(url).into(target);
        }catch (Exception e){
            errorLoad();
        }



    }

    private void errorLoad() {
        gallery_progress.setVisibility(INVISIBLE);
    }

    @Override
    public Photo getModel() {
        return null;
    }

    @Override
    public void setListener(InteractiveModelView.Listener listener) {
        setOnClickListener(v->{
            model.setSelected(true);
            listener.onModelAction(0,model);
        });

    }
}
