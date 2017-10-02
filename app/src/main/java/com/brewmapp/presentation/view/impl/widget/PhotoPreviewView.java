package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;

import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.stub.impl.SimpleTextWatcher;

/**
 * Created by oleg on 16.08.17.
 */

public class PhotoPreviewView extends BaseLinearLayout implements ModelView<UploadPhotoResponse> {

    private static final int aspectRatioHeight = 3;
    private static final int aspectRatioWidth = 5;

    @BindView(R.id.view_photo_image) ImageView image;
    @BindView(R.id.view_photo_description) TextView description;


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
    protected void prepareView() {
        ButterKnife.bind(this);
        description.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                model.setTitle(editable.toString());
            }
        });
    }

    @Override
    public UploadPhotoResponse getModel() {
        return model;
    }

    @Override
    public void setModel(UploadPhotoResponse model) {
        this.model = model;
        this.description.setText(model.getTitle());
        Picasso.with(getContext())
                .load(model.getFile())
                .fit().centerCrop().into(image);
    }
}
