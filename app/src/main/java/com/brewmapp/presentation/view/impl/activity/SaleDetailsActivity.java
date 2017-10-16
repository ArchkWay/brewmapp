package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.contract.SaleDetailsView;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

import com.brewmapp.R;
import com.brewmapp.presentation.view.contract.ResultDialog;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;

public class SaleDetailsActivity extends BaseActivity implements SaleDetailsView, RefreshableView {
    @BindView(R.id.activity_sale_details_avatar)    ImageView avatar;
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_sale_details_title)    TextView titile;
    @BindView(R.id.activity_sale_details_resto_name)    TextView resto_name;
    @BindView(R.id.activity_sale_details_web_view)    WebView web_view;
    @BindView(R.id.activity_sale_details_sale_more)    ImageView sale_more;
    @BindView(R.id.activity_sale_details_like_counter)    TextView like_counter;
    @BindView(R.id.activity_sale_details_like)    LinearLayout like;

    private Sale sale;

    @Inject SaleDetailsPresenter presenter;
    @Inject EventsPresenter eventsPresenter;

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
        sale_more.setOnClickListener((v)->new DialogShare(SaleDetailsActivity.this,sale,null));
        like.setOnClickListener((v)->eventsPresenter.onLike(sale,this));
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
    public void showSaleDetails(Sale sale) {
        this.sale = sale;
        setTitle(sale.getParent().getName());
        titile.setText(sale.getName());
        resto_name.setText(sale.getParent().getName());
        like_counter.setText(String.valueOf(sale.getLike()));
        String img="";
        try {
            img=String.valueOf(sale.getPhotos().get(0).getUrl());
            img="<p><a href=\""+img+"\" target=\"_blank\"><img src=\""+img+"\" width=\"100%\" ></img></a></p>";
        }catch (Exception e){

        }
        web_view.loadData(img+sale.getText(), "text/html; charset=utf-8", "utf-8");


    }

    @Override
    public void refreshState() {
        like_counter.setText(String.valueOf(sale.getLike()));
    }
}
