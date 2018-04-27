package com.brewmapp.presentation.view.impl.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;


import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by xpusher on 11/28/2017.
 */

public class ViewUserItem extends BaseLinearLayout implements InteractiveModelView<User> {
    @BindView(R.id.view_user_avatar)    ImageView avatar;
    @BindView(R.id.view_user_chevron_right)    ImageView chevron_right;
    @BindView(R.id.view_user_username)    TextView user_name;
    @BindView(R.id.view_user_city)    TextView city;

    private Listener listener;
    private User user;


    public ViewUserItem(Context context) {
        super(context);
    }

    public ViewUserItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewUserItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewUserItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(User model) {
        this.user=model;
        class FillContent{
            public FillContent(){
                texts();

                images(avatar,model.getThumbnail(),model.getGender()==1?R.drawable.ic_user_man:R.drawable.ic_user_woman);
            }

            private void images(ImageView imageView, String urlAvatar, int ic_default_resto) {
                if(urlAvatar==null)
                    Picasso.with(imageView.getContext()).load(ic_default_resto).fit().centerCrop().into(imageView);
                else
                    Picasso.with(imageView.getContext()).load(urlAvatar).fit().centerCrop().into(imageView);
            }

            private void texts() {
                user_name.setText(model.getFormattedName());
                city.setText(model.getCountryCityFormated());
            }
        }

        new FillContent();

        if(getContext() instanceof MultiListActivity) {
            chevron_right.setOnClickListener(view -> {
                ((Activity) getContext()).setResult(Activity.RESULT_OK, new Intent(null, Uri.parse(String.valueOf(model.getId()))));
                ((Activity) getContext()).finish();
            });
            setOnClickListener(view -> {
                ((Activity) getContext()).startActivityForResult(
                        new Intent(String.valueOf(ProfileActivity_view.SHOW_PROFILE_FRAGMENT_VIEW_SHOT), Uri.parse(String.valueOf(user.getId())), getContext(), ProfileActivity.class),
                        RequestCodes.REQUEST_PROFILE_FRIEND
                );
            });
        }else if(getContext() instanceof MainActivity){
            Contact contact=new Contact();
            contact.setFriend_info(user);
            setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_CLICK,contact));
        }

    }

    @Override
    public User getModel() {
        return null;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener=listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);

    }
}
