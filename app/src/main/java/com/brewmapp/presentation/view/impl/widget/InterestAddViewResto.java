package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Resto;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 21.10.2017.
 */

public class InterestAddViewResto extends BaseLinearLayout implements InteractiveModelView<Resto> {
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;
    @BindView(R.id.view_interest_shot_text)    TextView shot_text;
    @BindView(R.id.view_interest_button_select)    ImageView arrow_right;

    private Resto resto;
    private Listener listener;


    public InterestAddViewResto(Context context) {
        super(context);
    }

    public InterestAddViewResto(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestAddViewResto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InterestAddViewResto(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setModel(Resto resto) {
        this.resto =resto;
        String imgUrl="";
        imgUrl= resto.getThumb();

        if(TextUtils.isEmpty(imgUrl)||imgUrl.length()==0)
            Picasso.with(getContext()).load(R.drawable.ic_default_resto).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(imgUrl).fit().centerInside().into(avatar);


        title.setText(resto.getName());
        shot_text.setText(resto.getAdressFormat());

        setOnClickListener(v -> listener.onModelAction(RequestCodes.ACTION_VIEW, resto));
        arrow_right.setOnClickListener(v -> listener.onModelAction(RequestCodes.ACTION_SELECT, resto));
    }

    @Override
    public Resto getModel() {
        return resto;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);


    }


}
