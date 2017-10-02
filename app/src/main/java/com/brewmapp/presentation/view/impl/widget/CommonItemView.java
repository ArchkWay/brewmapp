package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.model.ICommonItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 17.08.17.
 */

public class CommonItemView extends BaseLinearLayout implements InteractiveModelView<ICommonItem> {

    @BindView(R.id.view_commonItem_image) ImageView image;
    @BindView(R.id.view_commonItem_title) TextView title;
    @BindView(R.id.view_commonItem_scroll) HorizontalScrollView scroll;

    private ICommonItem model;
    private Listener listener;

    public CommonItemView(Context context) {
        super(context);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        final GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                listener.onModelAction(Actions.ACTION_SELECT, model);
                return true;
            }
        });


        scroll.setOnTouchListener((v, event) -> {
            detector.onTouchEvent(event);
            return false;
        });
    }

    @Override
    public ICommonItem getModel() {
        return model;
    }

    @Override
    public void setModel(ICommonItem model) {
        this.model = model;
        title.setText(model.title());
        if(model.image() != null) {
            try {
                switch (model.image().source()) {
                    case FILE:
                        Picasso.with(getContext()).load(model.image().file()).fit().centerCrop().into(image);
                        break;
                    case URL:
                        Picasso.with(getContext()).load(model.image().url()).fit().centerCrop().into(image);
                        break;
                    case RESOURCE:
                        image.setImageResource(model.image().resource());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            image.setImageDrawable(null);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
