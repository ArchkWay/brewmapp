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
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogInput;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectKitchen;
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
    @BindView(R.id.fragment_edit_resto_place)    TextView place;
    @BindView(R.id.fragment_edit_resto_edit_place)    ImageView edit_place;
    @BindView(R.id.fragment_edit_resto_edit_kitchen)    ImageView edit_kitchen;
    @BindView(R.id.fragment_edit_resto_kitchen)    TextView kitchen;


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
        edit_name.setOnClickListener(v->
                new DialogInput()
                        .show(getActivity()
                                .getSupportFragmentManager(),
                                R.string.new_value,
                                name.getText().toString(),
                                string -> {
                                    try {
                                        presenter.getRestoDetail_new_data().getResto().setName(string);
                                        name.setText(string);
                                        mListener.invalidateOptionsMenu();
                                    }catch (Exception e){ showMessage(e.getMessage());}
                                }
                        )
        );
        edit_avg_account.setOnClickListener(v ->
                new DialogInput()
                        .show(getActivity()
                                .getSupportFragmentManager(),
                                R.string.new_value,
                                Integer.valueOf(avg_account.getText().toString()),
                                string -> {
                                        try {
                                            presenter.getRestoDetail_new_data().getResto().setAvgCost(Integer.valueOf(string));
                                            avg_account.setText(string);
                                            mListener.invalidateOptionsMenu();
                                        }catch (Exception e){ showMessage(e.getMessage());}
                                            }
                                )
        );
        edit_place.setOnClickListener(v ->
                new DialogSelectAddress().setLocation(presenter.getRestoDetail_new_data().getResto().getLocation())
                        .showDialog(
                                getActivity().getSupportFragmentManager(),
                                location -> {
                                    presenter.getRestoDetail_new_data().getResto().setLocation(location);
                                    place.setText(location.getFormatLocation());
                                    mListener.invalidateOptionsMenu();
                                })
        );

        edit_kitchen.setOnClickListener(v ->
                new DialogSelectKitchen()
                        .setKitchen(presenter.getRestoDetail_new_data().getResto_kitchen())
                        .showDialog(
                                getActivity().getSupportFragmentManager(),
                                kitchenArrayList -> {
                                    presenter.getRestoDetail_new_data().setResto_kitchen(kitchenArrayList);
                                    kitchen.setText(presenter.getRestoDetail_new_data().getFormattedKitchen());
                                    mListener.invalidateOptionsMenu();
                                })
        );

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
    public void setContent(RestoDetail restoDetail) {
        new FillContent(restoDetail);
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

    @Override
    public void commonError(String... strings) {
        mListener.commonError(strings);
    }

    //****************************************************


    public interface OnFragmentInteractionListener {
        void commonError(String... message);

        void invalidateOptionsMenu();
    }
    class FillContent {

        public FillContent(RestoDetail restoDetail) {
            getActivity().setTitle(restoDetail.getResto().getName());
            initSlider(restoDetail.getResto());
            fillTexts(restoDetail);
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

        private void fillTexts(RestoDetail restoDetail) {
            name.setText(restoDetail.getResto().getName());
            try {place.setText(restoDetail.getResto().getLocation().getFormatLocation());}catch (Exception e){}
            avg_account.setText(String.valueOf(restoDetail.getResto().getAvgCost()));
            kitchen.setText(restoDetail.getFormattedKitchen());
        }
    }
}
