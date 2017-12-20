package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.presentation.presenter.contract.BeerEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.BeerEditFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogInput;
import com.brewmapp.presentation.view.impl.dialogs.DialogSearchBrand;
import com.brewmapp.presentation.view.impl.fragment.Simple.WebViewFragment;
import com.brewmapp.presentation.view.impl.widget.AddPhotoSliderView;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

/**
 * Created by xpusher on 12/8/2017.
 */

public class BeerEditFragment extends BaseFragment  implements BeerEditFragmentView {
    @BindView(R.id.fragment_edit_beer_swipe)    RefreshableSwipeRefreshLayout refreshableSwipeRefreshLayout;
    @BindView(R.id.fragment_edit_beer_scroll)   ScrollView scrollView;
    @BindView(R.id.fragment_edit_beer_slider)    SliderLayout sliderLayout;
    @BindView(R.id.fragment_edit_beer_indicator)    PagerIndicator pagerIndicator;
    @BindView(R.id.fragment_edit_beer_photosCounter)    TextView photosCounter;
    @BindView(R.id.fragment_edit_beer_name)    TextView name;
    @BindView(R.id.fragment_edit_beer_edit_name)    ImageView edit_name;
    @BindView(R.id.fragment_edit_beer_brand)    TextView brand;
    @BindView(R.id.fragment_edit_beer_edit_brand)    ImageView edit_brand;

    @Inject    BeerEditFragmentPresenter presenter;

    private OnFragmentInteractionListener mListener;

    public BeerEditFragment(){

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_beer_edit;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        sliderLayout.stopAutoCycle();
        registerTextChangeListeners(s -> {presenter.getBeer_new_data().setTitle(name.getText().toString());mListener.invalidateOptionsMenu();},name);
        edit_name.setOnClickListener(v -> new DialogInput().show(getActivity().getSupportFragmentManager(),R.string.new_value,name.getText().toString(), string -> name.setText(string)));
        edit_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogSearchBrand().show(getActivity().getSupportFragmentManager(), new DialogSearchBrand.OnSelectBrand() {
                    @Override
                    public void onSelect(BeerBrand beerBrand) {
                        brand.setText(beerBrand.getName());
                        presenter.getBeer_new_data().setBrand_id(beerBrand.getId());
                        mListener.invalidateOptionsMenu();
                    }
                });
            }
        });
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        try {
            String id_beer=getActivity().getIntent().getData().toString();
            presenter.requestContent(id_beer);
        }catch (Exception e){
            mListener.commonError(e.getMessage());
        }

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WebViewFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void commonError(String... strings) {
        mListener.commonError(strings);
    }

    @Override
    public void setContent(Beer beer) {
        new FillContent(beer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save,menu);
        menu.findItem(R.id.action_save).setEnabled(presenter.isNeedSave());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                presenter.storeChanges();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        void commonError(String... message);

        void invalidateOptionsMenu();
    }

    class FillContent{

        public FillContent(Beer beer) {
            getActivity().setTitle(beer.getTitle());
            fillSlider(beer);
            fillTexts(beer);
        }

        private void fillTexts(Beer beer) {
            name.setText(beer.getTitle());
            try {brand.setText(beer.getRelations().getBeerBrand().getName());}catch (Exception e){}


        }

        private void fillSlider(Beer beer) {
            ArrayList<String> photos=new ArrayList<>();
            if(beer.getGetThumb()!=null&&beer.getGetThumb().length()>0)
                photos.add(beer.getGetThumb());
            for(String s:photos){
                sliderLayout.addSlider(new DefaultSliderView(getActivity())
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .image(s)
                        .setOnSliderClickListener(slider1 -> {showMessage(getActivity().getString(R.string.message_develop));}));

            }
            sliderLayout.addSlider(new AddPhotoSliderView(getActivity(), v -> {showMessage(getActivity().getString(R.string.message_develop));}));

        }
    }
}
