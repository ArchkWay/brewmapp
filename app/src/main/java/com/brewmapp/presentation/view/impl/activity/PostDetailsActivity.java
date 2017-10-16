package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.view.contract.PostDetailsView;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.contract.ResultTask;
import com.brewmapp.presentation.view.contract.ResultDialog;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;
import com.brewmapp.utils.Cons;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.DateTools;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PostDetailsActivity extends BaseActivity implements PostDetailsView , RefreshableView{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_post_details_title)    TextView title;
    @BindView(R.id.activity_post_details_text)    TextView text;
    @BindView(R.id.activity_post_details_date)    TextView date;
    @BindView(R.id.activity_post_details_like)    View like;
    @BindView(R.id.activity_post_details_more)    ImageView more;
    @BindView(R.id.activity_post_details_like_counter)    TextView counter;
    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;


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

        if(post.getRepost()==null) {
            repost.setVisibility(GONE);
            repost_photo.setImageDrawable(null);
        }else {
            repost.setVisibility(VISIBLE);

            List<Photo> photos=post.getRepost().getPhoto();
            if(photos!=null&&photos.size()>0&&photos.get(0).getUrlPreview()!=null)
                repost_photo.post(() -> {
                    float ratio = (float)photos.get(0).getSize().getWidth() / photos.get(0).getSize().getHeight();
                    LinearLayout.LayoutParams params = ((LinearLayout.LayoutParams) repost_photo.getLayoutParams());
                    params.height = (int) (repost_photo.getMeasuredWidth()/ratio);
                    repost_photo.setLayoutParams(params);
                    Picasso.with(PostDetailsActivity.this).load(photos.get(0).getUrl()).fit().centerCrop().into(repost_photo);
                });

            repost_name.setText(post.getRepost().getUser_resto_admin()==null?post.getRepost().getUser_info().getFormattedName():post.getRepost().getUser_resto_admin().getName());
            repost_text.setText(new StringBuilder()
                    .append(post.getRepost().getShort_text()==null?Html.fromHtml(String.valueOf(post.getRepost().getText())):post.getRepost().getShort_text())
                    .toString()
            );
        }


        setTitle(post.getUser().getFormattedName());
        title.setText(post.getUser().getFormattedName());
        text.setText(post.getText() != null ? Html.fromHtml(post.getText()) : null);
        like.setOnClickListener((v)->eventsPresenter.onLike(post,this));
        more.setOnClickListener((v)->new DialogShare(PostDetailsActivity.this, post, () -> {setResult(RESULT_OK); finish();}));
        date.setText(DateTools.formatDottedDateWithTime(post.getDate()));
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Cons.REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
