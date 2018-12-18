package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.tool.Text2TextWithHashTag;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.view.contract.PostDetailsView;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

public class PostDetailsActivity extends BaseActivity implements PostDetailsView
{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_post_details_title)    TextView title;
    @BindView(R.id.activity_post_details_text)    TextView text;
    @BindView(R.id.activity_post_details_date)    TextView date;
    @BindView(R.id.activity_post_details_photo)    ImageView photo;
    @BindView(R.id.activity_post_details_avatar)    ImageView avatar;
    @BindView(R.id.activity_post_details_container_avatar)    LinearLayout container_avatar;

    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;

    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;


    @Inject    PostDetailsPresenter presenter;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);

        enableBackButton();


//        String imgUrl = post.getUser_getThumb().getUrl_preview();
//        if (TextUtils.isEmpty(imgUrl)) {
//            try {
//                Picasso.with(this).load(post.getUser_info().getGender().equals("1") ? R.drawable.ic_user_man : R.drawable.ic_user_woman).fit().centerCrop().into(avatar);
//            }
//            catch (Exception e) {
//            }
//        }

        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setLinksClickable(true);
        setTitle(R.string.title_activity_news_detail);
        container_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailsActivity.this, RestoDetailActivity.class);
                Interest interest=null;
                try {
                    interest=new Interest(new Resto(post.getRelated_model_data().getId(),post.getRelated_model_data().getName()));
                }catch (Exception e){}
                if(interest==null)
                    try {
                        interest=new Interest(new Resto(post.getRelated_id(),""));
                    }catch (Exception e){}
                if(interest!=null) {
                    intent.putExtra(Keys.RESTO_ID, interest);
                    startActivity(intent);
                }

            }
        });
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
    public void fillContent(Post post) {

        this.post=post;

        class FillContent{
            String photoUrl;
            int photoHeight;
            int photoWidth;

            public void fill(){
                shareLikeView.setiLikeable(post);
                repost();
                texts();
                Photo new_photo=null;try {new_photo=post.getPhoto().get(0);}catch (Exception e){}
                photo(photo,new_photo,R.drawable.ic_default_resto);

                String urlAvatar=null;
                try {urlAvatar=post.getRelated_model_data().getGetThumb();}catch (Exception e){}
                avatar(avatar,urlAvatar,R.drawable.ic_default_resto);
            }

            private void avatar(ImageView imageView, String urlAvatar, int ic_default_resto) {
                if(urlAvatar==null)
                    Picasso.with(imageView.getContext()).load(ic_default_resto).fit().centerCrop().into(imageView);
                else
                    Picasso.with(imageView.getContext()).load(urlAvatar).fit().centerCrop().into(imageView);
            }

            private void photo(ImageView imageView,Photo new_photo,int default_photo) {
                imageView.setImageBitmap(null);
                try {
                    photoUrl=new_photo.getUrl();
                    photoHeight=new_photo.getSize().getHeight();
                    photoWidth=new_photo.getSize().getWidth();}catch (Exception e){photoUrl=null;}

                if(photoUrl==null) {imageView.setVisibility(GONE);return;}
                else {imageView.setVisibility(VISIBLE);}

                imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        //resize
                        float ratio=(float) imageView.getWidth()/(float) photoWidth;
                        LinearLayout.LayoutParams p= (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        p.height=(int) (photoHeight*ratio);
                        imageView.setLayoutParams(p);
                        //load
                        imageView.post(() -> Picasso.with(imageView.getContext()).load(photoUrl).error(default_photo).into(imageView));

                        return true;
                    }
                });


            }

            private void texts() {

                title.setText(post.getRelated_model_data().getName());
                date.setText(post.getDateFormated());
                new Text2TextWithHashTag(text,post.getText());

            }

            private void repost() {
//                if(post.getRepost()==null) {
                    repost.setVisibility(GONE);
//                    repost_photo.setImageDrawable(null);
//                }else {
//                    repost.setVisibility(VISIBLE);
//
//                    List<Photo> photos=post.getRepost().getPhoto();
//                    if(photos!=null&&photos.size()>0&&photos.get(0).getUrlPreview()!=null)
//                        repost_photo.post(() -> {
//                            float ratio = (float)photos.get(0).getSize().getWidth() / photos.get(0).getSize().getHeight();
//                            LinearLayout.LayoutParams params = ((LinearLayout.LayoutParams) repost_photo.getLayoutParams());
//                            params.height = (int) (repost_photo.getMeasuredWidth()/ratio);
//                            repost_photo.setLayoutParams(params);
//                            Picasso.with(PostDetailsActivity.this).load(photos.get(0).getUrl()).fit().centerCrop().into(repost_photo);
//                        });
//
//                    repost_name.setText(post.getRepost().getUser_info()==null?"":post.getRepost().getUser_info().getFormattedName());
//                    repost_text.setText(new StringBuilder()
//                            .append((post.getRepost().getShort_text()==null||post.getRepost().getShort_text().equals(""))?Html.fromHtml(String.valueOf(post.getRepost().getText())):post.getRepost().getShort_text())
//                            .toString()
//                    );
//                }

            }


        }
        new FillContent().fill();



    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbarTitle.setText(getTitle());
    }

    @Override
    public void setTitle(int title) {
        super.setTitle(title);
        toolbarTitle.setText(getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK){
                    setResult(RESULT_OK);
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
