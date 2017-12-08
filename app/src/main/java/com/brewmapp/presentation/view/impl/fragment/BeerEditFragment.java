package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.BeerEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.BeerEditFragmentView;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.WebViewFragment;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 12/8/2017.
 */

public class BeerEditFragment extends BaseFragment  implements BeerEditFragmentView {

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


    public interface OnFragmentInteractionListener {
        void commonError(String... message);
    }
}
