package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.tool.Text2TextWithHashTag;
import com.brewmapp.presentation.view.impl.activity.PhotoSliderActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 16.08.17.
 */

public class SaleView extends BaseLinearLayout implements InteractiveModelView<Sale> {

    @BindView(R.id.view_sale_author) TextView author;
    @BindView(R.id.view_sale_text) TextView text;
    @BindView(R.id.view_sale_avatar) ImageView avatar;
    @BindView(R.id.view_sale_date) TextView date;
    @BindView(R.id.view_sale_preview) ImageView preview;
    @BindView(R.id.view_sale_container) View container;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;

    private Listener listener;
    private Sale model;

    public SaleView(Context context) {
        super(context);
    }

    public SaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SaleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        container.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RestoDetailActivity.class);
            Interest interest=null;
            if(interest==null)
                try {
                    interest=new Interest(new Resto(model.getRelated_id(),""));
                }catch (Exception e){}
            if(interest!=null) {
                intent.putExtra(Keys.RESTO_ID, interest);
                getContext().startActivity(intent);
            }

        });
        text.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
        preview.setOnClickListener(v -> PhotoSliderActivity.startPhotoSliderActivity(model.getPhotos(),getContext()));
    }

    @Override
    public Sale getModel() {
        return model;
    }

    @Override
    public void setModel(Sale model) {
        this.model=model;
        class FillContent{
            String photoUrl=null;
            int photoWidth=0;
            int photoHeight=0;
            public void fill() {
                shareLikeView.setiLikeable(model);

                texts();

                String urlAvatar=null;
                try {urlAvatar=model.getParent().getThumb();}catch (Exception e){}
                avatar(avatar,urlAvatar,R.drawable.ic_default_resto);

                Photo new_photo=null;try {new_photo=model.getPhotos().get(0);}catch (Exception e){}
                photo(preview,new_photo,R.drawable.ic_default_image);


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
                try {author.setText(model.getParent().getName());}catch (Exception e){};
                date.setText(model.getDateStartFormated());
                new Text2TextWithHashTag(text,model.getText());
            }

            private void avatar(ImageView imageView, String urlAvatar, int ic_default_resto) {
                if(urlAvatar==null)
                    Picasso.with(imageView.getContext()).load(ic_default_resto).fit().centerCrop().into(imageView);
                else
                    Picasso.with(imageView.getContext()).load(urlAvatar).fit().centerCrop().into(imageView);
            }
        }

        new FillContent().fill();
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
