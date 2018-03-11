package com.brewmapp.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.AddReviewBeerPresenter;
import com.brewmapp.presentation.view.contract.AddReviewBeerView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TextTools;

public class AddReviewBeerActivity extends BaseActivity implements AddReviewBeerView , View.OnFocusChangeListener{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.addreviewactivity_review_edit_text)    EditText review_edit_text;

    @BindView(R.id.addreviewactivitybeer_rating_aftertaste)    RatingBar aftertaste;
    @BindView(R.id.addreviewactivitybeer_rating_color)    RatingBar color;
    @BindView(R.id.addreviewactivitybeer_rating_flavor)    RatingBar flavor;
    @BindView(R.id.addreviewactivitybeer_rating_taste)    RatingBar taste;

    @BindView(R.id.addreviewactivity_rating_negative)    View rating_negative;
    @BindView(R.id.addreviewactivity_rating_positive)    View rating_positive;
    @BindView(R.id.addreviewactivity_rating_positive_select)    View positive_select;
    @BindView(R.id.addreviewactivity_rating_negative_select)    View negative_select;

    @Inject    AddReviewBeerPresenter presenter;

    private Post post = new Post();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_beer);
    }

    @Override
    protected void initView() {
        enableBackButton();
        setTitle(R.string.title_activity_add_review);
        enableControls(false, 0);
        registerTextChangeListeners(s -> {post.setText(TextTools.extractTrimmed(review_edit_text)); invalidateOptionsMenu();},review_edit_text);
        aftertaste.setOnRatingBarChangeListener(presenter.getRatingBarChangeListener());
        color.setOnRatingBarChangeListener(presenter.getRatingBarChangeListener());
        flavor.setOnRatingBarChangeListener(presenter.getRatingBarChangeListener());
        taste.setOnRatingBarChangeListener(presenter.getRatingBarChangeListener());

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

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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
    }

    @Override
    public void reviewSent() {
        setResult(RESULT_OK);
        finish();
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
