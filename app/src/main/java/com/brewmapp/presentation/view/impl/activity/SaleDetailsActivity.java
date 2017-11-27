package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.tool.HashTagHelper2;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.view.contract.SaleDetailsView;
import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

public class SaleDetailsActivity extends BaseActivity implements SaleDetailsView
                    {
    @BindView(R.id.activity_sale_details_avatar)    ImageView avatar;
    @BindView(R.id.activity_sale_details_container_avatar)    LinearLayout container_avatar;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_sale_details_date)    TextView date;
    @BindView(R.id.activity_sale_details_text)    TextView text;
    @BindView(R.id.activity_sale_details_photo)    ImageView photo;
    @BindView(R.id.activity_sale_details_resto_name)    TextView resto_name;
    @BindView(R.id.root_view_share_like) ShareLikeView shareLikeView;
    private Sale sale;

    @Inject SaleDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();
        text.setMovementMethod(LinkMovementMethod.getInstance());
        container_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleDetailsActivity.this, RestoDetailActivity.class);
                Interest interest=null;
                if(interest==null)
                    try {
                        interest=new Interest(new Resto(sale.getRelated_id(),""));
                    }catch (Exception e){}
                if(interest!=null) {
                    intent.putExtra(Keys.RESTO_ID, interest);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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
    public void setModel(Sale sale) {
        this.sale = sale;

        class FillContent{
            String photoUrl=null;
            int photoWidth=0;
            int photoHeight=0;


            public void fill(){
                shareLikeView.setiLikeable(sale);
                texts();

                String urlAvatar=null;
                try {urlAvatar=sale.getParent().getThumb();}catch (Exception e){}
                avatar(avatar,urlAvatar,R.drawable.ic_sale);

                Photo new_photo=null;try {new_photo=sale.getPhotos().get(0);}catch (Exception e){}
                photo(photo,new_photo,R.drawable.ic_default_image);

            }
            private void photo(ImageView imageView, Photo new_photo, int default_photo) {
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

            private void avatar(ImageView imageView, String urlAvatar, int ic_default_resto) {
                if(urlAvatar==null)
                    Picasso.with(imageView.getContext()).load(ic_default_resto).fit().centerCrop().into(imageView);
                else
                    Picasso.with(imageView.getContext()).load(urlAvatar).fit().centerCrop().into(imageView);
            }
            private void texts() {
                setTitle(R.string.text_view_sale);
                try {resto_name.setText(sale.getParent().getName());}catch (Exception e){}
                new HashTagHelper2(text,sale.getText());
                date.setText(sale.getDateStartFormated());
            }
        }
        new FillContent().fill();
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
