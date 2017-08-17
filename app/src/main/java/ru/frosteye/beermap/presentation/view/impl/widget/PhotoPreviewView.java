package ru.frosteye.beermap.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.Photo;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 16.08.17.
 */

public class PhotoPreviewView extends BaseLinearLayout implements ModelView<UploadPhotoResponse> {

    @BindView(R.id.view_photo_image) ImageView image;


    private UploadPhotoResponse model;

    public PhotoPreviewView(Context context) {
        super(context);
    }

    public PhotoPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhotoPreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public UploadPhotoResponse getModel() {
        return model;
    }

    @Override
    public void setModel(UploadPhotoResponse model) {
        this.model = model;
        Picasso.with(getContext())
                .load(model.getFile())
                .fit().centerCrop().into(image);
    }
}
