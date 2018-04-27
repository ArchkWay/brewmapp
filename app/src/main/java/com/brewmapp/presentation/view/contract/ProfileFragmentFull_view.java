package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import java.util.ArrayList;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface ProfileFragmentFull_view extends BasicView {
    void commonError(String... message);

    void showProfile(ArrayList<User> users);
}
