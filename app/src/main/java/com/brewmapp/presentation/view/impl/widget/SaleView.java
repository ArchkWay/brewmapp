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

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by oleg on 16.08.17.
 */

public class SaleView extends BaseLinearLayout implements InteractiveModelView<Sale> {

    @BindView(R.id.view_sale_author) TextView author;
    @BindView(R.id.view_sale_text) TextView text;
    @BindView(R.id.view_sale_avatar) ImageView avatar;
    @BindView(R.id.view_sale_date) TextView date;
    @BindView(R.id.view_sale_like) View like;
    @BindView(R.id.view_sale_like_counter) TextView likeCounter;
    @BindView(R.id.view_sale_more) ImageView more;
    @BindView(R.id.view_sale_preview) ImageView preview;

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
        ButterKnife.bind(this);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        like.setOnClickListener(v -> {
            listener.onModelAction(Actions.ACTION_LIKE_SALE, model);
        });
    }

    @Override
    public Sale getModel() {
        return model;
    }

    @Override
    public void setModel(Sale model) {
        this.model = model;
        author.setText(model.getParent().getName());
        likeCounter.setText(String.valueOf(model.getLike()));
        text.setText(model.getText() != null ? Html.fromHtml(model.getText()) : null);
        date.setText(DateTools.formatDottedDate(model.getDateStart()));
        if(model.getPhotos() != null && !model.getPhotos().isEmpty()) {
            Photo photo = model.getPhotos().get(0);
            if(photo.getSize() == null) {
                preview.setVisibility(GONE);
                return;
            }
            preview.setVisibility(VISIBLE);
            float ratio = photo.getSize().getWidth() / photo.getSize().getHeight();
            LinearLayout.LayoutParams params = ((LayoutParams) preview.getLayoutParams());
            params.height = preview.getMeasuredWidth();
            Picasso.with(getContext()).load(photo.getUrl()).fit().centerCrop().into(preview);
        } else {
            preview.setVisibility(GONE);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
