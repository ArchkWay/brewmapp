package com.brewmapp.presentation.view.impl.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.presenter.impl.AddReviewRestoPresenterImpl;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class AddReviewRestoActivity extends BaseActivity implements AddReviewRestoView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.addreviewactivity_rating_common_effect)    RatingBar common_effect;
    @BindView(R.id.addreviewactivity_rating_interior)    RatingBar interior;
    @BindView(R.id.addreviewactivity_rating_quality_beer)    RatingBar quality_beer;
    @BindView(R.id.addreviewactivity_rating_service)    RatingBar service;
    @BindView(R.id.addreviewactivity_review_edit_text)    EditText review_edit_text;

    @BindViews({
            R.id.addreviewactivity_rating_common_effect,
            R.id.addreviewactivity_rating_interior,
            R.id.addreviewactivity_rating_quality_beer,
            R.id.addreviewactivity_rating_service,
            R.id.addreviewactivity_review_edit_text
    })    List<View> viewList;

    @Inject    AddReviewRestoPresenter presenter;

    private Post post = new Post();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_resto);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false,0);
        setTitle(R.string.title_activity_add_review);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        ButterKnife.apply(viewList, (ButterKnife.Action<View>) (view, index) -> {
            view.setEnabled(enabled);
            view.setClickable(enabled);
            if(view instanceof RatingBar) {
                ((RatingBar) view)
                        .getProgressDrawable()
                        .setColorFilter(
                                getResources().getColor(enabled ? R.color.colorButtonRed : R.color.colorGrayControls),
                                PorterDuff.Mode.SRC_ATOP
                        );
                ((RatingBar) view).setOnRatingBarChangeListener(enabled?presenter.getRatingListener():null);
            }
        });
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setModel(RestoDetail restoDetail) {
        enableControls(true,0);

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        menu.findItem(R.id.action_send).setEnabled(post.validate());
        return super.onCreateOptionsMenu(menu);
    }

}
