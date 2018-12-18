package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Review;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 04.11.2017.
 */

public class WhoIsInterestedView extends BaseLinearLayout implements InteractiveModelView<Interest> {

    @BindView(R.id.view_review_author)    TextView author;
    @BindView(R.id.view_review_avatar)    ImageView avatar;
    @BindView(R.id.view_review_date) TextView date;



    private Interest model;

    public WhoIsInterestedView(Context context) {
        super(context);
    }

    public WhoIsInterestedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WhoIsInterestedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WhoIsInterestedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Interest model) {
        this.model=model;
        try {author.setText(model.getUser_info().getFormattedName());}catch (Exception e){}

        String imgUrl=null;
        try {
                imgUrl = model.getUser_getThumb();
                Picasso.with(getContext()).load(imgUrl).fit().centerCrop().into(avatar);
        }
        catch (Exception e){}

        try {
                date.setText(model.getCreated_at());
        }
        catch (Exception e){}

        if(TextUtils.isEmpty(imgUrl))
            try {
                    Picasso.with(getContext()).load(model.getUser_info().getGender().equals("1") ?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().centerCrop().into(avatar);
            }
            catch (Exception e){}

    }

    @Override
    public Interest getModel() {
        return model;
    }

    @Override
    public void setListener(Listener listener) {
        setOnClickListener(view -> listener.onModelAction(Actions.ACTION_CLICK_ON_ITEM_USER,model));
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);

    }
}
