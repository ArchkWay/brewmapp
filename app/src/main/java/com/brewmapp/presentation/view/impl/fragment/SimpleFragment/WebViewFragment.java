package com.brewmapp.presentation.view.impl.fragment.SimpleFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.brewmapp.BuildConfig;
import com.brewmapp.R;

public class WebViewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_help, container, false);
        view.post(() -> fillContent());
        return view;
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
        setHasOptionsMenu(true);
        mListener.setTitleActionBar(R.string.help);

    }

    private void fillContent() {

        class myWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        }

        WebView webView= (WebView) getView().findViewById(R.id.fragment_help_web_view);
        webView.setWebViewClient(new myWebViewClient());
        try {
            webView.loadUrl(getActivity().getIntent().getData().toString());
        }catch (Exception e){
            mListener.commonError(e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void setTitleActionBar(int title);

        void commonError(String... message);
    }
}
