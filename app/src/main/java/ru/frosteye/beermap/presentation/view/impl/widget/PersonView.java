package ru.frosteye.beermap.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.model.IPerson;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by oleg on 06.08.17.
 */

public class PersonView extends BaseRelativeLayout implements ModelView<IPerson> {

    @BindView(R.id.view_personRow_image) RoundedImageView image;
    @BindView(R.id.view_personRow_imageOverlay) RoundedImageView imageOverlay;
    @BindView(R.id.view_personRow_name) TextView title;

    private IPerson model;

    public PersonView(Context context) {
        super(context);
    }

    public PersonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PersonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public IPerson getModel() {
        return model;
    }

    @Override
    public void setModel(IPerson model) {
        this.model = model;
    }
}
