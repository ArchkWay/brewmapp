package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.BuildConfig;
import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.execution.tool.HashTagHelper2;
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
        container.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
        text.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
        avatar.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
        preview.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
    }

    @Override
    public Sale getModel() {
        return model;
    }

    @Override
    public void setModel(Sale model) {
        shareLikeView.setiLikeable(model);
        this.model = model;
        author.setText(model.getParent().getName());
        new HashTagHelper2(text,model.getText());
        //text.setText(model.getText() != null ? TextTools.cut(Html.fromHtml(model.getText()).toString(), 250) : null);
        date.setText(DateTools.formatDottedDate(model.getDateStart()));

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
