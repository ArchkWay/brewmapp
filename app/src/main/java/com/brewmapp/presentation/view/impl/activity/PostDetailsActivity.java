package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.view.contract.PostDetailsView;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.contract.ResultTask;
import com.brewmapp.presentation.view.contract.ResultDialog;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class PostDetailsActivity extends BaseActivity implements PostDetailsView , RefreshableView{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_post_details_title)    TextView title;
    @BindView(R.id.activity_post_details_text)    TextView text;
    @BindView(R.id.activity_post_details_like)    View like;
    @BindView(R.id.activity_post_details_more)    ImageView more;
    @BindView(R.id.activity_post_details_like_counter)    TextView counter;


    @Inject    PostDetailsPresenter presenter;
    @Inject    EventsPresenter eventsPresenter;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
    }

    @Override
    protected void initView() {
        enableBackButton();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
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
    public void refreshState() {
        counter.setText(String.valueOf(post.getLike()));
    }

    @Override
    public void showPostDetails(Post post) {
        this.post=post;
        setTitle(post.getUser().getFullName());
        title.setText(post.getName());
        text.setText(post.getText() != null ? Html.fromHtml(post.getText()) : null);
        like.setOnClickListener((v)->eventsPresenter.onLike(post,this));
        more.setOnClickListener((v)->new DialogShare(PostDetailsActivity.this, post, () -> {setResult(RESULT_OK); finish();}));
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

}
