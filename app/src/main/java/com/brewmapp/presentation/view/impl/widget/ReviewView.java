package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Review;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewView extends BaseLinearLayout implements InteractiveModelView<Review> {

    @BindView(R.id.view_review_author)    TextView author;
    @BindView(R.id.view_review_avatar)    ImageView avatar;
    @BindView(R.id.view_review_date) TextView date;
    @BindView(R.id.view_review_text) TextView text;


    private Review model;

    public ReviewView(Context context) {
        super(context);
    }

    public ReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Review model) {
        this.model = model;
        try {author.setText(model.getUser_info().getFormattedName());}catch (Exception e){}

        String imgUrl=null;
        try {imgUrl=model.getUser_getThumb();Picasso.with(getContext()).load(imgUrl).fit().centerCrop().into(avatar);}catch (Exception e){}
        try {date.setText(model.getTimestamp());}catch (Exception e){}
        try {text.setText(model.getText());}catch (Exception e){}
        if(TextUtils.isEmpty(imgUrl))  try {Picasso.with(getContext()).load(model.getUser_info().getGender().equals("1")?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().centerCrop().into(avatar);}catch (Exception e){}

    }

    @Override
    public Review getModel() {
        return model;
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);

    }
}
