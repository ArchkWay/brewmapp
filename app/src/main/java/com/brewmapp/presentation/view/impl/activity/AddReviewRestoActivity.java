package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Evaluation;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.view.contract.AddReviewRestoView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TextTools;

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

    private final int ALL_CONTROL =0;
    private final int EVALUATION_CONTROL =1;
    private final int TEXT_EDIT_CONTROL =2;

    private Post post = new Post();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_resto);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false, ALL_CONTROL);

        setTitle(R.string.title_activity_add_review);
        registerTextChangeListeners(s -> {
                post.setText(TextTools.extractTrimmed(review_edit_text)); invalidateOptionsMenu();},
                review_edit_text
            );
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
            if(code == EVALUATION_CONTROL && view instanceof RatingBar) {
                view.setEnabled(((RatingBar)view).getOnRatingBarChangeListener()!=null);
                view.setClickable(((RatingBar)view).getOnRatingBarChangeListener()!=null);
            }else if(TEXT_EDIT_CONTROL ==code && view instanceof EditText){
                view.setEnabled(enabled);
                view.setClickable(enabled);
            } else if(code == ALL_CONTROL){
                view.setEnabled(enabled);
                view.setClickable(enabled);
            }
        });
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setEvaluation(List<Evaluation> evaluationList) {

        quality_beer.setOnRatingBarChangeListener(presenter.getRatingListener());
        interior.setOnRatingBarChangeListener(presenter.getRatingListener());
        common_effect.setOnRatingBarChangeListener(presenter.getRatingListener());
        service.setOnRatingBarChangeListener(presenter.getRatingListener());

        for (Evaluation evaluation:evaluationList)
            switch (evaluation.getEvaluation_type()){
                case Keys.EVLUATION_TYPE_BEER:
                    quality_beer.setRating(Float.valueOf(evaluation.getEvaluation_value()));
                    quality_beer.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_EFFECT:
                    common_effect.setRating(Float.valueOf(evaluation.getEvaluation_value()));
                    common_effect.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_INTERIOR:
                    interior.setRating(Float.valueOf(evaluation.getEvaluation_value()));
                    interior.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_SERVICE:
                    service.setRating(Float.valueOf(evaluation.getEvaluation_value()));
                    service.setOnRatingBarChangeListener(null);
                    break;
            }
        enableControls(true, EVALUATION_CONTROL);

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
    public void setUser(User user) {
        post.setName(user.getFormattedName());
        enableControls(true,TEXT_EDIT_CONTROL);
    }

    @Override
    public void reviewSent() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        menu.findItem(R.id.action_send).setEnabled(post.validate());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:{
                presenter.sendReview(post);
            }return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
