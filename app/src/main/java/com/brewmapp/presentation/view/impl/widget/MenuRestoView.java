package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.Entity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.MenuResto;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

public class MenuRestoView extends BaseLinearLayout implements ModelView<MenuResto>, View.OnClickListener {

    private MenuResto menuResto;

    @BindView(R.id.view_menu_avatar)    com.makeramen.roundedimageview.RoundedImageView avatar;
    @BindView(R.id.view_menu_title)    TextView menu_title;
    @BindView(R.id.view_menu_container_price)    LinearLayout container_price;

    public MenuRestoView(Context context) {
        super(context);
    }

    public MenuRestoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuRestoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuRestoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(MenuResto menuResto) {
        this.menuResto = menuResto;
        String pathImage=menuResto.getGetThumb_beerFormated();
        if(pathImage==null)
            Picasso.with(getContext()).load(R.drawable.ic_default_beer).fit().centerCrop().into(avatar);
        else
            Picasso.with(getContext()).load(pathImage).fit().centerInside().into(avatar);

        menu_title.setText(menuResto.getBeer_name());

        HashMap<String,String> hashMap=menuResto.getCapacity_price();
        Iterator<Map.Entry<String,String>> iterator=hashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            TextView textView= new TextView(getContext());
            textView.setText(String.format("%s / %s %s", entry.getKey(),entry.getValue(),getContext().getString(R.string.rubl)));
            container_price.addView(textView);
        }

        setOnClickListener(this);

    }

    @Override
    public MenuResto getModel() {
        return null;
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        Starter.BeerDetailActivity(getContext(),menuResto.getBeer_id());
    }
}
