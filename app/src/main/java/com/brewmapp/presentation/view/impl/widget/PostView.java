package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.Intent;
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
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Related_model_data;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.execution.tool.HashTagHelper2;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.PhotoSliderActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 16.08.17.
 */

public class PostView extends BaseLinearLayout implements InteractiveModelView<Post> {

    @BindView(R.id.view_post_author) TextView author;
    @BindView(R.id.view_post_text) TextView text;
    @BindView(R.id.view_post_avatar) ImageView avatar;
    @BindView(R.id.view_post_date) TextView date;
    @BindView(R.id.view_post_container) View container;
    @BindView(R.id.view_post_container_subcontainer) View subcontainer;
    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;
    @BindView(R.id.view_post_container_post_photo)    ImageView post_photo;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;

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
        subcontainer.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), RestoDetailActivity.class);
            Interest interest=null;
            try {
                interest=new Interest(new Resto(model.getRelated_model_data().getId(),model.getRelated_model_data().getName()));
            }catch (Exception e){}
            if(interest==null)
                try {
                    interest=new Interest(new Resto(model.getRelated_id(),""));
                }catch (Exception e){}
            if(interest!=null) {
                intent.putExtra(Keys.RESTO_ID, interest);
                getContext().startActivity(intent);
            }
        } );
        text.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_POST, model));
        repost.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_POST, model));
        post_photo.setOnClickListener(v -> {
            try {
                String[] urls=new String[model.getPhoto().size()];
               for(int i=0;i<model.getPhoto().size();i++)
                   urls[i]=model.getPhoto().get(i).getUrl();
               if(urls.length>0){
                   Intent intent = new Intent(getContext(), PhotoSliderActivity.class);
                    intent.putExtra(Keys.PHOTOS, urls);
                    getContext().startActivity(intent);
               }

            }catch (Exception e){}

        });

    }

    @Override
    public Post getModel() {
        return model;
    }

    @Override
    public void setModel(Post model) {
        this.model = model;

        class FillContent {
            SimpleSubscriber subscriber=new SimpleSubscriber<RestoDetail>() {
                @Override public void onNext(RestoDetail restoDetail) {
                    super.onNext(restoDetail);
                    try {
                        Related_model_data related_model_data=new Related_model_data();
                        related_model_data.setGetThumb(restoDetail.getResto().getThumb());
                        set_avatar(related_model_data);
                    } catch (Exception e){}
                }
                @Override public void onError(Throwable e) {
                    super.onError(e);
                }
            };

            String photoUrl=null;
            int photoWidth=0;
            int photoHeight=0;

            private void image() {
                post_photo.setImageBitmap(null);
                try {photoUrl=model.getPhoto().get(0).getUrl();photoHeight=model.getPhoto().get(0).getSize().getHeight();photoWidth=model.getPhoto().get(0).getSize().getWidth();}catch (Exception e){photoUrl=null;}
                if(photoUrl==null) {post_photo.setVisibility(GONE);return;}else {post_photo.setVisibility(VISIBLE);}
                post_photo.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        post_photo.getViewTreeObserver().removeOnPreDrawListener(this);
                        //resize
                        float ratio=(float) post_photo.getWidth()/(float) photoWidth;
                        LayoutParams p= (LayoutParams) post_photo.getLayoutParams();
                        p.height=(int) (photoHeight*ratio);
                        post_photo.setLayoutParams(p);
                        //load
                        post(() -> Picasso.with(getContext()).load(photoUrl).error(R.drawable.ic_news).into(post_photo));

                        return true;
                    }
                });
            }

            private void texts() {
                new HashTagHelper2(text,model.getText());
                date.setText(model.getDateFormated());
                try {author.setText(model.getRelated_model_data().getName());}catch (Exception e){}
            }

            private void avatar() {
                if(model.getRelated_model_data()==null){
                    if(getContext() instanceof MultiListActivity&&Keys.CAP_RESTO.equals(model.getRelated_model())){
                        LoadRestoDetailTask loadRestoDetailTask=((MultiListActivity)getContext()).getLoadRestoTask();
                        LoadRestoDetailPackage loadRestoDetailPackage =new LoadRestoDetailPackage();
                        loadRestoDetailPackage.setId(String.valueOf(model.getRelated_id()));
                        loadRestoDetailTask.execute(loadRestoDetailPackage, subscriber);

                    }
                }else {
                    set_avatar(model.getRelated_model_data());
                }

            }

            private void set_avatar(Related_model_data related_model_data) {
                String avatarLoad = null;
                try {
                    avatarLoad = related_model_data.getGetThumb();
                } catch (Exception e) {
                }

                if (avatarLoad == null)
                    Picasso.with(getContext()).load(R.drawable.ic_default_resto).fit().centerCrop().into(avatar);
                else
                    Picasso.with(getContext()).load(avatarLoad).fit().centerCrop().into(avatar);

            }

            private void repost() {
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
                repost();
                avatar();
                texts();
                image();
            }

        }

        new FillContent().fill();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }


}
