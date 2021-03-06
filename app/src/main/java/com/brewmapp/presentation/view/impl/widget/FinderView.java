package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.widget.BaseFrameLayout;
import ru.frosteye.ovsa.stub.impl.SimpleTextWatcher;

/**
 * Created by oleg on 06.08.17.
 */

public class FinderView extends BaseFrameLayout {

    @BindView(R.id.finder_cancel) View cancel;

    public AutoCompleteTextView getInput() {
        return input;
    }

    @BindView(R.id.view_finder_input)  AutoCompleteTextView input;

    private String hintString;



    public interface Listener {
        void onTextChanged(String string);
    }

    private Listener listener;
    private OnClickListener cancelListener;

    public FinderView(Context context) {
        super(context);
    }

    public FinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FinderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] getStyleableResource() {
        return R.styleable.FinderView;
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_search;
    }

    @Override
    protected void onTypedArrayReady(TypedArray array) {
        hintString = array.getString(R.styleable.FinderView_finderHint);
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        input.setHint(hintString);
        input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(listener != null) {
                    listener.onTextChanged(charSequence.toString());
                }
            }
        });
        input.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() ==       KeyEvent.KEYCODE_ENTER) {
                    hideInputKeyboard();
                    return true;
                }
                else  if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    cancel.performClick();
                    clearFocus();
                    return true;
                }
                return false;
            }
        });
        input.requestFocus();
        cancel.setOnClickListener(v -> {
            if(input.getText().toString().length() > 0) {
                input.setText(null);
                cancelListener.onClick(v);
            }
        });
    }

    public void hideInputKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCancelClickListener(OnClickListener listener) {
        cancelListener = listener;
    }

    public void setHintString(String hintString) {
        this.hintString = hintString;
        input.setHint(hintString);
    }

    @Override
    public void clearFocus() {
        super.clearFocus();
        //input.clearFocus();
        try {
            ((View)getParent()).requestFocus();
        }catch (Exception e){}
        hideInputKeyboard();
    }


    public boolean isSearchFocused(){
        return input.isFocused();
    }


}
