package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.view.contract.PostDetailsView;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;
import com.squareup.picasso.Picasso;

import org.xml.sax.XMLReader;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.DateTools;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

public class PostDetailsActivity extends BaseActivity implements PostDetailsView
{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_post_details_title)    TextView title;
    @BindView(R.id.activity_post_details_text)    TextView text;
    @BindView(R.id.activity_post_details_date)    TextView date;
    @BindView(R.id.view_post_container_repost)    LinearLayout repost;
    @BindView(R.id.view_post_container_repost_name)    TextView repost_name;
    @BindView(R.id.view_post_container_repost_text)    TextView repost_text;
    @BindView(R.id.view_post_container_repost_photo)    ImageView repost_photo;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;


    @Inject    PostDetailsPresenter presenter;

    private Post post;
    private Html.TagHandler htmlTagHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
    }

    @Override
    protected void initView() {
        enableBackButton();
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setLinksClickable(true);
        htmlTagHandler= new Html.TagHandler() {
            int cntOpen=0;
            int cntClose=0;
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                if(tag.startsWith("mybr")){
                    if(opening) {
                        if (cntOpen == 0)
                            processSpan(opening, output, new AppearanceSpan(cntOpen++));
                    }else{
                        if (cntClose == 0)
                            processSpan(opening, output, new AppearanceSpan(cntClose++));

                    }
                }
            }
        };

    }
    void processSpan(boolean opening, Editable output, Object span) {
        int len = output.length();
        if (opening) {
            output.setSpan(span, len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object[] objs = output.getSpans(0, len, span.getClass());
            int where = len;
            if (objs.length > 0) {
                for(int i = objs.length - 1; i >= 0; --i) {
                    if (output.getSpanFlags(objs[i]) == Spannable.SPAN_MARK_MARK) {
                        where = output.getSpanStart(objs[i]);
                        output.removeSpan(objs[i]);
                        break;
                    }
                }
            }

            if (where != len) {
                output.setSpan(span, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    class AppearanceSpan extends CharacterStyle {
        int color;
        public AppearanceSpan(int cntColor){
            switch (cntColor){
                case 0:
                    color=Color.RED;
                    break;
                case 1:
                    color=Color.BLUE;
                    break;
                default:
                    color=Color.BLACK;

            }
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(color);
        }
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
    public void showPostDetails(Post post) {
        post.setText(post.getText().replaceAll("<br>","<mybr>"));
        this.post=post;

        class FillContent{
            public void fill(){
                shareLikeView.setiLikeable(post);
                repost();
                texts();
            }

            private void texts() {
                setTitle(post.getUser().getFormattedName());

                title.setText(post.getUser().getFormattedName());
                date.setText(DateTools.formatDottedDateWithTime(post.getDate()));

                try {
                    Spanned spannedText=Html.fromHtml(post.getText(),null,htmlTagHandler);
                    Spannable reversedText = revertSpanned(spannedText);
                    text.setText(reversedText);
                }catch (Exception e){}

            }
            Spannable revertSpanned(Spanned stext) {
                Object[] spans = stext.getSpans(0, stext.length(), Object.class);
                Spannable ret = Spannable.Factory.getInstance().newSpannable(stext.toString());
                if (spans != null && spans.length > 0) {
                    for(int i = spans.length - 1; i >= 0; --i) {
                        ret.setSpan(spans[i], stext.getSpanStart(spans[i]), stext.getSpanEnd(spans[i]), stext.getSpanFlags(spans[i]));
                    }
                }

                return ret;
            }

            private void repost() {
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

                    repost_name.setText(post.getRepost().getUser_info()==null?"":post.getRepost().getUser_info().getFormattedName());
                    repost_text.setText(new StringBuilder()
                            .append((post.getRepost().getShort_text()==null||post.getRepost().getShort_text().equals(""))?Html.fromHtml(String.valueOf(post.getRepost().getText())):post.getRepost().getShort_text())
                            .toString()
                    );
                }

            }
        }
        new FillContent().fill();



    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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
