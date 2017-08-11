package ru.frosteye.beermap.presentation.view.impl.fragment;

import android.content.Intent;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.app.environment.RequestCodes;
import ru.frosteye.beermap.presentation.presenter.contract.FriendsPresenter;
import ru.frosteye.beermap.presentation.view.contract.FriendsView;
import ru.frosteye.beermap.presentation.view.impl.activity.InviteActivity;
import ru.frosteye.beermap.presentation.view.impl.widget.FinderView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.beermap.R;

public class FriendsFragment extends BaseFragment implements FriendsView {

    @BindView(R.id.fragment_friends_addFriend) View addFriend;
    @BindView(R.id.fragment_friends_search) FinderView search;

    @Inject FriendsPresenter presenter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {
        addFriend.setOnClickListener(v -> {
            startActivityForResult(new Intent(getActivity(), InviteActivity.class), RequestCodes.REQUEST_INVITE_FRIEND);
        });
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadFriends();
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
        return ResourceHelper.getString(R.string.friends);
    }
}
