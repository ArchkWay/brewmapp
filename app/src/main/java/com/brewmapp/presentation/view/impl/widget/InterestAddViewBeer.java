package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.contract.UiCustomControl;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 21.10.2017.
 */

public class InterestAddViewBeer extends BaseLinearLayout implements InteractiveModelView<Beer>,UiCustomControl {
    @BindView(R.id.view_interest_avatar)    ImageView avatar;
    @BindView(R.id.view_interest_title)    TextView title;
    @BindView(R.id.view_interest_arrow_right)    ImageView arrow_right;

    private Beer beer;
    private Listener listener;

    public InterestAddViewBeer(Context context) {
        super(context);
    }

    public InterestAddViewBeer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestAddViewBeer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InterestAddViewBeer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Beer beer) {
        this.beer =beer;

        String imgUrl="";
        imgUrl=beer.getGetThumb();

        if(TextUtils.isEmpty(imgUrl)||imgUrl.length()==0)
            Picasso.with(getContext()).load(R.drawable.ic_default_beer).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(imgUrl).fit().centerInside().into(avatar);

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(TextUtils.isEmpty(beer.getTitle())?"":beer.getTitle());
        if(!TextUtils.isEmpty(beer.getTitle_ru())) {
            if (stringBuilder.length() > 0) {
                if (!stringBuilder.toString().equals(beer.getTitle_ru()))
                    stringBuilder.append(TextUtils.isEmpty(beer.getTitle_ru()) ? "" : " (" + beer.getTitle_ru() + ")");
            }else {
                stringBuilder.append(TextUtils.isEmpty(beer.getTitle_ru()) ? "" : beer.getTitle_ru());
            }
        }
        title.setText(stringBuilder.toString());

        if(getContext() instanceof MultiListActivity) {
            setOnClickListener(v -> listener.onModelAction(VIEW_MODEL, beer));
            arrow_right.setOnClickListener(v -> listener.onModelAction(SELECT_MODEL, beer));
        }else {
            setOnClickListener(v -> listener.onModelAction(FilterActions.BEER, beer));
        }
    }

    @Override
    public Beer getModel() {
        return beer;
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
