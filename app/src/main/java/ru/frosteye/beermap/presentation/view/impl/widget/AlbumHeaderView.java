package ru.frosteye.beermap.presentation.view.impl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumHeaderView extends BaseLinearLayout implements ModelView<Album> {

    @BindView(R.id.view_albumHeader_image) ImageView image;
    @BindView(R.id.view_albumHeader_name) TextView title;
    @BindView(R.id.view_albumHeader_description) TextView description;


    private Album model;

    public AlbumHeaderView(Context context) {
        super(context);
    }

    public AlbumHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AlbumHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Album getModel() {
        return model;
    }

    @Override
    public void setModel(Album model) {
        this.model = model;
        this.description.setText(model.getDescription());
        this.title.setText(model.getName());
        if(model.getPhotos() != null && !model.getPhotos().getPhotos().isEmpty()) {
            Picasso.with(getContext()).load(model.getPhotos().getPhotos().get(0).getUrlPreview())
                    .fit().centerCrop()
                    .into(image);
        } else {
            image.setImageDrawable(null);
        }
    }
}
