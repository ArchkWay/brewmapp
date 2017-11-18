package com.brewmapp.presentation.presenter.impl;

import android.net.Uri;
import android.text.TextUtils;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.execution.task.UploadAvatarTask;
import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 09.11.2017.
 */

public class ProfileEditFragmentPresenterImpl extends BasePresenter<ProfileEditFragmentView> implements ProfileEditFragmentPresenter {

    private User user_old_data;
    private User user_new_data=new User();
    private ProfileChangeTask profileChangeTask;
    private UploadAvatarTask uploadAvatarTask;


    @Inject
    public ProfileEditFragmentPresenterImpl(UserRepo userRepo, ProfileChangeTask profileChangeTask,UploadAvatarTask uploadAvatarTask){
        user_old_data=userRepo.load();
        user_new_data.setGender(user_old_data.getGender());
        user_new_data.setBirthday(user_old_data.getBirthday());
        this.profileChangeTask = profileChangeTask;
        this.uploadAvatarTask = uploadAvatarTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileEditFragmentView profileEditFragmentView) {
        super.onAttach(profileEditFragmentView);
        view.refreshProfile(user_old_data);
    }

    @Override
    public CharSequence getTitle() {
        return user_old_data.getFormattedName();
    }

    @Override
    public void save(ProfileEditFragment.OnFragmentInteractionListener mListener) {
        class save {

            public save() {
                saveUser();
            }

            public void saveUser() {
                if (isNeedSaveUser(  "getFirstname", "getStatus", "getLastname")) {
                    profileChangeTask.execute(user_new_data, new SimpleSubscriber<ListResponse<User>>() {
                        @Override
                        public void onNext(ListResponse<User> userListResponse) {
                            super.onNext(userListResponse);
                            uploadPhoto();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                } else {
                    uploadPhoto();
                }
            }

            public void uploadPhoto() {
                if (isNeedSaveUser("getThumbnail")) {
                    uploadAvatarTask.execute(new File(user_new_data.getThumbnail()), new SimpleSubscriber<String>() {
                        @Override
                        public void onNext(String string) {
                            super.onNext(string);
                            mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.FRAGMENT_USER_SAVED)));
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.FRAGMENT_USER_SAVED)));
                }
            }
        }
        new save();
    }

    @Override
    public boolean isNeedSaveUser(String... checkListCustom) {
        //user_new_data.getLastname()
        String[] checkList={"getThumbnail","getFirstname","getStatus","getLastname"};
        if(checkListCustom.length>0)
            checkList=checkListCustom;

        for (String s:checkList) if(isNeedSaveField(s)) return true;

        return false;
    }

    private boolean isNeedSaveField(String getMethod_name) {
        try {
            String type=user_new_data.getClass().getMethod(getMethod_name).getGenericReturnType().toString();
            if(type.contains("String")){
                Method m_get = user_old_data.getClass().getMethod(getMethod_name);
                Method m_set = user_new_data.getClass().getMethod(new StringBuilder().append(getMethod_name).replace(0, 1, "s").toString(), String.class);

                String val_old = (String) m_get.invoke(user_old_data);
                String val_new = (String) m_get.invoke(user_new_data);

                if (!TextUtils.isEmpty(val_new) && !val_new.equals(val_old))
                    return true;
                else
                    m_set.invoke(user_new_data, (String) null);

            }else if(type.contains("int")){
                Method m_get = user_old_data.getClass().getMethod(getMethod_name);

                int val_old = (int) m_get.invoke(user_old_data);
                int val_new = (int ) m_get.invoke(user_new_data);

                return val_old!=val_new;

            }else if(type.contains("Date")){
                Method m_get = user_old_data.getClass().getMethod(getMethod_name);
                Date val_old = (Date) m_get.invoke(user_old_data);
                Date val_new = (Date) m_get.invoke(user_new_data);

                return val_old!=val_new;
            }
            } catch(NoSuchMethodException e){
                e.printStackTrace();
            } catch(IllegalAccessException e){
                e.printStackTrace();
            } catch(InvocationTargetException e){
                e.printStackTrace();
            }


        return false;
    }

    @Override
    public User getUserWithNewData() {
        return user_new_data;
    }
}
