package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;
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
 * Created by Kras on 10.12.2017.
 */

public class RestoEditFragment extends BaseFragment  implements RestoEditFragmentView {
    @BindView(R.id.fragment_edit_resto_swipe)    RefreshableSwipeRefreshLayout refreshableSwipeRefreshLayout;
    @BindView(R.id.fragment_edit_resto_scroll)    ScrollView scrollView;
    @BindView(R.id.fragment_edit_resto_slider)    SliderLayout sliderLayout;
    @BindView(R.id.fragment_edit_resto_indicator)    PagerIndicator pagerIndicator;
    @BindView(R.id.fragment_edit_resto_photosCounter)    TextView photosCounter;
    @BindView(R.id.fragment_edit_resto_name)    TextView name;
    @BindView(R.id.fragment_edit_resto_edit_name)    ImageView edit_name;
    @BindView(R.id.fragment_edit_resto_avg_account)    TextView avg_account;
    @BindView(R.id.fragment_edit_resto_edit_avg_account)    ImageView edit_avg_account;


    @Inject RestoEditFragmentPresenter presenter;

    private OnFragmentInteractionListener mListener;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_resto_edit;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        sliderLayout.stopAutoCycle();

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        try {
            String id_resto=getActivity().getIntent().getData().toString();
            presenter.requestContent(id_resto);
        }catch (Exception e){
            mListener.commonError(e.getMessage());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
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
    public void setContent(Resto resto) {
        new FillContent(resto);
    }

    @Override
    public void commonError(String... strings) {
        mListener.commonError(strings);
    }

    public interface OnFragmentInteractionListener {
        void commonError(String... message);

        void invalidateOptionsMenu();
    }

    class FillContent {

        public FillContent(Resto resto) {
            getActivity().setTitle(resto.getName());
            initSlider(resto);
            fillTexts(resto);
        }

        private void initSlider(Resto resto) {
            ArrayList<String> photos=new ArrayList<>();
            if(resto.getThumb()!=null&&resto.getThumb().length()>0)
                photos.add(resto.getThumb());
            for(String s:photos){
                sliderLayout.addSlider(new DefaultSliderView(getActivity())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .image(s)
                        .setOnSliderClickListener(slider1 -> {showMessage(getActivity().getString(R.string.message_develop));}));

            }
            presenter.loadAllPhoto(sliderLayout);
        }

        private void fillTexts(Resto resto) {
            name.setText(resto.getName());
        }
    }
}
