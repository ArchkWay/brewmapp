package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.presentation.presenter.contract.ShareLikeViewPresenter;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.BasicView;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;

/**
 * Created by Kras on 17.10.2017.
 */

public class ShareLikeView extends RelativeLayout implements BasicView {

    @BindView(R.id.view_share_like)    View like;
    @BindView(R.id.view_share_like_counter)    TextView likeCounter;
    @BindView(R.id.view_share_more)    ImageView more;

    @BindView(R.id.view_share_dislike)    View dislike;
    @BindView(R.id.view_share_dislike_counter)    TextView dislikeCounter;

    @BindView(R.id.left_name)    TextView leftText;
    @BindView(R.id.middle_text)     TextView middleText;


    @Inject    ShareLikeViewPresenter shareLikeViewPresenter;

    private ILikeable iLikeable;
    private InteractiveModelView.Listener listener;

    public ShareLikeView(Context context) {
        super(context);
    }

    public ShareLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareLikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShareLikeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(isInEditMode()) return;
        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        ButterKnife.bind(this);
        shareLikeViewPresenter.onAttach(this);

        dislike.setOnClickListener(view -> {shareLikeViewPresenter.onDislike(iLikeable,()->{
            //iLikeable.increaseDisLikes();
            setiLikeable(iLikeable);
        });});

        like.setOnClickListener(view -> {shareLikeViewPresenter.onLike(iLikeable,()->{
            //iLikeable.increaseLikes();
            setiLikeable(iLikeable);
        });});
        more.setOnClickListener(view -> {new DialogShare((BaseActivity) getContext(),iLikeable, () -> refreshItems());});
    }


    private void refreshItems() {
        listener.onModelAction(Actions.ACTION_REFRESH_FRAGMENT_CONTENT,null);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void showMessage(CharSequence message, int code) {

    }



    public ILikeable getiLikeable() {
        return iLikeable;
    }

    public void setiLikeable(ILikeable iLikeable) {
        this.iLikeable = iLikeable;
        likeCounter.setText(String.valueOf(iLikeable.getLike()));
        dislikeCounter.setText(String.valueOf(iLikeable.getDislike()));
    }

    public void setLeftText(String val) {
        leftText.setText(val);
        leftText.setVisibility(VISIBLE);
    }
    public void setMiddleText(String val) {
        middleText.setText(val);
        middleText.setVisibility(VISIBLE);
    }


    public void setListener(InteractiveModelView.Listener listener) {
        this.listener = listener;
    }
}
