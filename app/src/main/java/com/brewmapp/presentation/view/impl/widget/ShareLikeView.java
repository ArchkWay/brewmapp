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

/**
 * Created by Kras on 17.10.2017.
 */

public class ShareLikeView extends RelativeLayout implements BasicView {

    @BindView(R.id.view_share_like)    View like;
    @BindView(R.id.view_share_like_counter)    TextView likeCounter;
    @BindView(R.id.view_share_more)    ImageView more;


    @Inject    ShareLikeViewPresenter shareLikeViewPresenter;

    private ILikeable iLikeable;

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

        like.setOnClickListener(view -> {shareLikeViewPresenter.onLike(iLikeable,()->{refreshState();});});
        more.setOnClickListener(view -> {new DialogShare((BaseActivity) getContext(),iLikeable, () -> refreshItems());});
    }

    private void refreshState() {
        if(getContext() instanceof MainActivity)
            ((MainActivity)getContext()).refreshState();
        else{
            ((BaseActivity)getContext()).setResult(Activity.RESULT_OK);
            setiLikeable(iLikeable);
        }
    }

    private void refreshItems() {
        if(getContext() instanceof MainActivity)
            ((MainActivity)getContext()).refreshItems();
        else{
            ((BaseActivity)getContext()).setResult(Activity.RESULT_OK);
            ((BaseActivity)getContext()).finish();
        }

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
    }


}
