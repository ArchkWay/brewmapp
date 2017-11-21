package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import java.io.File;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 09.11.2017.
 */

public interface ProfileEditFragmentView extends BasicView {
    void refreshProfile(User user);
    void commonError(String... strings);

    void selectedPhoto(File file);
}
