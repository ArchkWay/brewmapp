package com.brewmapp.presentation.view.impl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.SearchBeer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 07.12.2017.
 */

public class SearchBeerView extends BaseLinearLayout implements InteractiveModelView<SearchBeer> {

    @BindView(R.id.beer_title)
    TextView title;
    @BindView(R.id.beer_price)
    TextView beerPrice;
    @BindView(R.id.beer_craft)
    TextView beerCraft;
    @BindView(R.id.beer_short_text)
    TextView shortDescription;
    @BindView(R.id.ic_beer)
    com.makeramen.roundedimageview.RoundedImageView logo;
    private Listener listener;
    private SearchBeer model;

    public SearchBeerView(Context context) {
        super(context);
    }

    public SearchBeerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchBeerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchBeerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public SearchBeer getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(SearchBeer model) {
        this.model = model;

        //String titleRu = (model.getTitleRu() == null || TextUtils.isEmpty(model.getTitle()) ? "" : " (" + model.getTitleRu() + ")");
        title.setText(model.getFormatedTitle());
        shortDescription.setVisibility((model.getShortText() != null && model.getShortText().isEmpty()) ? GONE : VISIBLE);
        shortDescription.setText((model.getShortText() != null && model.getShortText().isEmpty()) ? "" : model.getShortText());
        if(model.getGetThumb() != null && !model.getGetThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerInside().into(logo);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_default_beer).fit().centerCrop().into(logo);
        }

        beerPrice.setText(getResources().getString(R.string.beer_avg_price, model.getAvgPrices500()));
        beerCraft.setVisibility(model.getCraft() == 1 ? VISIBLE : GONE);

        setOnClickListener(v -> listener.onModelAction(FilterRestoField.BEER, model));

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
