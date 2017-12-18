package com.brewmapp.presentation.view.impl.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.fragment.BeerEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.AboutFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.WebViewFragment;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class MultiFragmentActivity extends BaseActivity implements MultiFragmentActivityView,
        AboutFragment.OnFragmentInteractionListener,
        WebViewFragment.OnFragmentInteractionListener ,
        BeerEditFragment.OnFragmentInteractionListener,
        RestoEditFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener

{
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.multiactivity_root)    ViewGroup root;



    @Inject    MultiFragmentActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_view);
    }

    @Override
    protected void initView() {
        enableBackButton();
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
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
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.multiactivity_сontainer, fragment)
                .commit();

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setTitleActionBar(int title) {
        setTitle(title);
//// Get the ActionBar
//        ActionBar ab = getSupportActionBar();
//
//        // Create a TextView programmatically.
//        TextView tv = new TextView(getApplicationContext());
//
//        // Create a LayoutParams for TextView
//        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, // Width of TextView
//                ViewGroup.LayoutParams.WRAP_CONTENT); // Height of TextView
//
//        // Apply the layout parameters to TextView widget
//        tv.setLayoutParams(lp);
//
//        // Set text to display in TextView
//        tv.setText(ab.getTitle());
//
//        // Set the text color of TextView
//        tv.setTextColor(Color.BLACK);
//
//        // Set TextView text alignment to center
//        tv.setGravity(Gravity.CENTER);
//
//        // Set the ActionBar display option
//        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//
//        // Finally, set the newly created TextView as ActionBar custom view
//        ab.setCustomView(tv);
    }
}
