package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.execution.exchange.request.base.Keys;
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
    @BindView(R.id.view_post_container) View container;
    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;
    @BindView(R.id.view_post_container_post_photo)    ImageView post_photo;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;

    private Listener listener;
    private Post model;
    private int originalHeight=0;
    //private FillContent fillContent = new FillContent();

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
        new FillContent().fill();



    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    class FillContent {

        String photoUrl=null;
        int photoWidth=0;
        int photoHeight=0;

        private void setImage() {
            post_photo.setImageBitmap(null);
            try {photoUrl=model.getPhoto().get(0).getUrl();photoHeight=model.getPhoto().get(0).getSize().getHeight();photoWidth=model.getPhoto().get(0).getSize().getWidth();}catch (Exception e){photoUrl=null;}
            if(photoUrl==null) {post_photo.setVisibility(GONE);return;}else {post_photo.setVisibility(VISIBLE);}
            post_photo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    post_photo.getViewTreeObserver().removeOnPreDrawListener(this);
                    //resize
                    float ratio=(float) post_photo.getWidth()/(float) photoWidth;
                    //post_photo.setMinimumHeight((int) (photoHeight*ratio));
                    LayoutParams p= (LayoutParams) post_photo.getLayoutParams();
                    p.height=(int) (photoHeight*ratio);
                    post_photo.setLayoutParams(p);
                    //load
                    post_photo.postDelayed(() -> Picasso.with(getContext()).load(photoUrl).fit().centerCrop().into(post_photo),10);
                    return true;
                }
            });
        }

        private void setTexts() {
            text.setText(model.getText() != null ? Html.fromHtml(model.getText()) : null);
            date.setText(DateTools.formatDottedDateWithTime(model.getDate()));
            try {author.setText(model.getRelated_model_data().getName());}catch (Exception e){}
        }

        private void setAvatar() {
            String avatarLoad=null;
            try {avatarLoad=model.getRelated_model_data().getGetThumb();}catch (Exception e){}
            int avatarError;
            switch (model.getRelated_model()){
                case Keys.CAP_RESTO:
                    avatarError=R.drawable.ic_default_resto;
                    break;
                default:
                    avatarError=R.drawable.ic_default_resto;
            }
            if(avatarLoad==null)
                Picasso.with(getContext()).load(avatarError).fit().centerCrop().into(avatar);
            else
                Picasso.with(getContext()).load(avatarLoad).fit().centerCrop().into(avatar);

        }

        private void setRepost() {
            if(model.getRepost()==null) {
                repost.setVisibility(GONE);
            }else {
                repost.setVisibility(VISIBLE);
                repost_photo.post(() -> {
                    List<Photo> photos = model.getRepost().getPhoto();
                    if (photos != null && photos.size() > 0 && photos.get(0).getUrlPreview() != null) {
                        float ratio = (float) photos.get(0).getSize().getWidth() / photos.get(0).getSize().getHeight();
                        LayoutParams params = ((LayoutParams) repost_photo.getLayoutParams());
                        params.height = (int) (repost_photo.getMeasuredWidth() / ratio);
                        repost_photo.setLayoutParams(params);
                        repost_photo.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(getContext()).load(photos.get(0).getUrl()).fit().centerCrop().into(repost_photo);
                            }
                        });

                    } else {
                        repost_photo.setImageDrawable(null);
                        LayoutParams params = ((LayoutParams) repost_photo.getLayoutParams());
                        params.height = params.width = 0;
                        repost_photo.setLayoutParams(params);
                    }
                });
                repost_name.setText(model.getRepost().getUser_info()==null?"":model.getRepost().getUser_info().getFormattedName());
                repost_text.setText(new StringBuilder()
                        .append((model.getRepost().getShort_text()==null||model.getRepost().getShort_text().equals(""))?Html.fromHtml(String.valueOf(model.getRepost().getText())):model.getRepost().getShort_text())
                        .toString()
                );
            }
        }

        public void fill() {
            shareLikeView.setiLikeable(model);
            setRepost();
            setAvatar();
            setTexts();
            setImage();
        }

    }

}
