package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.PhotoDetails;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.widget.BaseFrameLayout;

/**
 * Created by oleg on 26.09.17.
 */

public class LikeView extends BaseFrameLayout implements ViewUserItem.OnClickListener{

    @BindView(R.id.view_like_counter) TextView counter;

    private PhotoDetails photoDetails;

    @Inject    LikeTask likeTask;

    public LikeView(Context context) {
        super(context);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LikeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_like;
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        BeerMap.getAppComponent().plus(new PresenterModule((BaseActivity) getContext())).inject(this);
    }

    public void setCount(int likes) {
        this.counter.setText(String.valueOf(likes));
    }

    public void increase() {
        try {
            counter.setText(String.valueOf(Integer.parseInt(counter.getText().toString()) + 1));
        } catch (Exception e) {

        }
    }

    public void setPhotoDetails(PhotoDetails photoDetails){
        this.photoDetails = photoDetails;
        setCount(Integer.valueOf(photoDetails.getLike()));
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(Keys.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_PHOTO, Integer.valueOf(photoDetails.getId()));
        likeTask.execute(likeDislikePackage, new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                ((BaseActivity)getContext()).showSnackbarRed(e.getMessage());
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                increase();
            }
        });

    }
}
