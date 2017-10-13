package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import ru.frosteye.ovsa.tool.TextTools;

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
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        like.setOnClickListener(v -> {
            listener.onModelAction(Actions.ACTION_LIKE_SALE, model);
        });
        text.setOnClickListener(v -> {
            listener.onModelAction(Actions.ACTION_SELECT_SALE, model);
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
        text.setText(model.getText() != null ? TextTools.cut(Html.fromHtml(model.getText()).toString(), 250) : null);
        date.setText(DateTools.formatDottedDate(model.getDateStart()));
        more.setOnClickListener(v->listener.onModelAction(Actions.ACTION_SALE_SHARE,model));

        if(model.getPhotos() != null && !model.getPhotos().isEmpty()) {
            Photo photo = model.getPhotos().get(0);
            if(photo.getSize() == null) {
                preview.setVisibility(GONE);
                return;
            }
            preview.setVisibility(VISIBLE);
            float ratio = (float)photo.getSize().getWidth() / photo.getSize().getHeight();
            preview.post(() -> {
                LayoutParams params = ((LayoutParams) preview.getLayoutParams());
                params.height = (int) (preview.getMeasuredWidth()/ratio);
                preview.setLayoutParams(params);
                Picasso.with(getContext()).load(photo.getUrl()).fit().centerCrop().into(preview);
            });

        } else {
            preview.setVisibility(GONE);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
