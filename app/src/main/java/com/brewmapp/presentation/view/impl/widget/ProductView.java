package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Product;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 21.10.2017.
 */

public class ProductView extends BaseLinearLayout implements InteractiveModelView<Product> {
    @BindView(R.id.view_product_avatar)    ImageView avatar;
    @BindView(R.id.view_product_title)    TextView title;

    private Product product;
    private Listener listener;

    public ProductView(Context context) {
        super(context);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProductView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setModel(Product model) {
        product=model;
        Picasso.with(getContext()).load(model.getGetThumb()).fit().centerCrop().into(avatar);
        title.setText(model.getTitle());
        setOnClickListener(v -> listener.onModelAction(0,model));
    }

    @Override
    public Product getModel() {
        return product;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);

    }


}
