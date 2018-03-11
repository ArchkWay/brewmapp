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
import com.brewmapp.data.entity.EvaluationResto;
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


public class AddReviewRestoActivity extends BaseActivity implements AddReviewRestoView, View.OnFocusChangeListener{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.addreviewactivity_rating_common_effect)    RatingBar common_effect;
    @BindView(R.id.addreviewactivity_rating_interior)    RatingBar interior;
    @BindView(R.id.addreviewactivity_rating_quality_beer)    RatingBar quality_beer;
    @BindView(R.id.addreviewactivity_rating_service)    RatingBar service;
    @BindView(R.id.addreviewactivity_review_edit_text)    EditText review_edit_text;

    @BindView(R.id.addreviewactivity_rating_negative)    View rating_negative;
    @BindView(R.id.addreviewactivity_rating_positive)    View rating_positive;
    @BindView(R.id.addreviewactivity_rating_positive_select)    View positive_select;
    @BindView(R.id.addreviewactivity_rating_negative_select)    View negative_select;

    @BindViews({
            R.id.addreviewactivity_rating_common_effect,
            R.id.addreviewactivity_rating_interior,
            R.id.addreviewactivity_rating_quality_beer,
            R.id.addreviewactivity_rating_service,
            R.id.addreviewactivity_review_edit_text
    })    List<View> viewList;

    @Inject    AddReviewRestoPresenter presenter;


    private Post post = new Post();
    private final int CONTROL_ALL=0;
    private final int CONTROL_ONLY_TEXT_EDIT=1;
    private final int CONTROL_ONLY_EVALUATION=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_resto);
    }

    @Override
    protected void initView() {
        enableBackButton();
        enableControls(false, CONTROL_ALL);
        setTitle(R.string.title_activity_add_review);
        registerTextChangeListeners(s -> {
                post.setText(TextTools.extractTrimmed(review_edit_text)); invalidateOptionsMenu();},
                review_edit_text
            );

        rating_negative.setOnFocusChangeListener(this);
        rating_positive.setOnFocusChangeListener(this);
        positive_select.setVisibility(View.VISIBLE);
        negative_select.setVisibility(View.INVISIBLE);
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
            if(code == CONTROL_ONLY_EVALUATION && view instanceof RatingBar) {
                view.setEnabled(((RatingBar)view).getOnRatingBarChangeListener()!=null);
                view.setClickable(((RatingBar)view).getOnRatingBarChangeListener()!=null);
            }else if(CONTROL_ONLY_TEXT_EDIT ==code && view instanceof EditText){
                view.setEnabled(enabled);
                view.setClickable(enabled);
            } else if(code == CONTROL_ALL){
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
    public void setEvaluation(List<EvaluationResto> evaluationRestoList) {

        quality_beer.setOnRatingBarChangeListener(presenter.getRatingListener());
        interior.setOnRatingBarChangeListener(presenter.getRatingListener());
        common_effect.setOnRatingBarChangeListener(presenter.getRatingListener());
        service.setOnRatingBarChangeListener(presenter.getRatingListener());

        for (EvaluationResto evaluationResto : evaluationRestoList)
            switch (evaluationResto.getEvaluation_type()){
                case Keys.EVLUATION_TYPE_BEER:
                    quality_beer.setRating(Float.valueOf(evaluationResto.getEvaluation_value()));
                    quality_beer.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_EFFECT:
                    common_effect.setRating(Float.valueOf(evaluationResto.getEvaluation_value()));
                    common_effect.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_INTERIOR:
                    interior.setRating(Float.valueOf(evaluationResto.getEvaluation_value()));
                    interior.setOnRatingBarChangeListener(null);
                    break;
                case Keys.EVLUATION_TYPE_SERVICE:
                    service.setRating(Float.valueOf(evaluationResto.getEvaluation_value()));
                    service.setOnRatingBarChangeListener(null);
                    break;
            }
        enableControls(true, CONTROL_ONLY_EVALUATION);

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
        enableControls(true, CONTROL_ONLY_TEXT_EDIT);
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
                post.setType(positive_select.getVisibility()==View.VISIBLE?1:0);
                presenter.sendReview(post);
            }return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.addreviewactivity_rating_positive:
                    positive_select.setVisibility(View.VISIBLE);
                    negative_select.setVisibility(View.INVISIBLE);
                    break;
                case R.id.addreviewactivity_rating_negative:
                    positive_select.setVisibility(View.INVISIBLE);
                    negative_select.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
