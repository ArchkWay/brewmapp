package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by oleg on 16.08.17.
 */

public class PostView extends BaseLinearLayout implements InteractiveModelView<Post> {

    @BindView(R.id.view_post_author) TextView author;
    @BindView(R.id.view_post_text) TextView text;
    @BindView(R.id.view_post_avatar) ImageView avatar;
    @BindView(R.id.view_post_date) TextView date;
    @BindView(R.id.view_post_like) View like;
    @BindView(R.id.view_post_like_counter) TextView likeCounter;
    @BindView(R.id.view_post_more) ImageView more;
    @BindView(R.id.view_post_container) View container;
    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;

    private Listener listener;
    private Post model;

    public PostView(Context context) {
        super(context);
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        like.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_LIKE_POST, model));
        container.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_POST, model));
        text.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_POST, model));
        repost.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_POST, model));
    }

    @Override
    public Post getModel() {
        return model;
    }

    @Override
    public void setModel(Post model) {
        this.model = model;
        if(model.getRepost()==null)
            repost.setVisibility(GONE);
        else {
            repost.setVisibility(VISIBLE);
            repost_photo.post(() -> {
                List<Photo> photos = model.getRepost().getPhoto();
                if (photos != null && photos.size() > 0 && photos.get(0).getUrlPreview() != null) {
                    float ratio = (float) photos.get(0).getSize().getWidth() / photos.get(0).getSize().getHeight();
                    LayoutParams params = ((LayoutParams) repost_photo.getLayoutParams());
                    params.height = (int) (repost_photo.getMeasuredWidth() / ratio);
                    repost_photo.setLayoutParams(params);
                    Picasso.with(getContext()).load(photos.get(0).getUrl()).fit().centerCrop().into(repost_photo);
                } else {
                    repost_photo.setImageDrawable(null);
                    LayoutParams params = ((LayoutParams) repost_photo.getLayoutParams());
                    params.height = params.width = 0;
                    repost_photo.setLayoutParams(params);
                }
            });
            //repost_name.setText(model.getRepost().getUser_resto_admin()==null?model.getRepost().getUser_info().getFormattedName():model.getRepost().getUser_resto_admin().getName());
            repost_name.setText(model.getRepost().getUser_info()==null?"":model.getRepost().getUser_info().getFormattedName());
            repost_text.setText(new StringBuilder()
                    .append((model.getRepost().getShort_text()==null||model.getRepost().getShort_text().equals(""))?Html.fromHtml(String.valueOf(model.getRepost().getText())):model.getRepost().getShort_text())
                    .toString()
            );
        }

        author.setText(model.getUser().getFormattedName());
        likeCounter.setText(String.valueOf(model.getLikes()));
        text.setText(model.getText() != null ? Html.fromHtml(model.getText()) : null);
        date.setText(DateTools.formatDottedDateWithTime(model.getDate()));
        if(model.getUser().getThumbnail() != null) {
            Picasso.with(getContext()).load(model.getUser().getThumbnail()).fit().centerCrop().into(avatar);
        } else {
            if(model.getUser().getGender() == 1) {
                avatar.setImageResource(R.drawable.ic_user_man);
            } else {
                avatar.setImageResource(R.drawable.ic_user_woman);
            }
        }
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onModelAction(Actions.ACTION_SHARE_POST,model);
            }
        });
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
