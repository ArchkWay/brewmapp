package com.brewmapp.presentation.presenter.impl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.execution.task.LoadUsersTask;
import com.brewmapp.execution.task.ProfileChangeTask;
import com.brewmapp.execution.task.UploadAvatarTask;
import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileEditFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private Context context;
    private UserRepo userRepo;
    private LoadProfileTask loadProfileTask;
    private LoadUsersTask loadUsersTask;
    String[] checkListAll={"getCountryId","getCityId","getGender","getGames","getSite","getSkype","getAdditionalPhone","getPhone","getInterests","getMusic","getBooks","getFilms","getFamilyStatus","getCountryId","getCityId","getBirthday","getFirstname","getStatus","getLastname","getThumbnail"};
    String[] checkListWithoutPthoto={"getCountryId","getCityId","getGender","getGames","getSite","getSkype","getAdditionalPhone","getPhone","getInterests","getMusic","getBooks","getFilms","getFamilyStatus","getCountryId","getCityId","getBirthday","getFirstname","getStatus","getLastname"};
    String[] checkListPhoto={"getThumbnail"};


    @Inject
    public ProfileEditFragmentPresenterImpl(Context context,UserRepo userRepo, ProfileChangeTask profileChangeTask,UploadAvatarTask uploadAvatarTask,LoadProfileTask loadProfileTask,LoadUsersTask loadUsersTask){
        this.context=context;
        user_old_data=userRepo.load();

        this.profileChangeTask = profileChangeTask;
        this.uploadAvatarTask = uploadAvatarTask;
        this.loadProfileTask = loadProfileTask;
        this.userRepo = userRepo;
        this.loadUsersTask = loadUsersTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(ProfileEditFragmentView profileEditFragmentView) {
        super.onAttach(profileEditFragmentView);

        loadUsersTask.execute(user_old_data.getId(),new SimpleSubscriber<ArrayList<User>>(){
            @Override
            public void onNext(ArrayList<User> users) {
                super.onNext(users);
                try {
                    user_old_data=users.get(0);
                    user_new_data.setGender(user_old_data.getGender());
                    user_new_data.setBirthday(user_old_data.getBirthday());
                    user_new_data.setCityId(user_old_data.getCityId());
                    user_new_data.setCountryId(user_old_data.getCountryId());
                    user_new_data.setFamilyStatus(user_old_data.getFamilyStatus());

                    view.refreshProfile(user_old_data);
                }catch (Exception e){
                    view.commonError(e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.commonError(e.getMessage());
            }
        });

    }

    @Override
    public CharSequence getTitle() {
        return user_old_data.getFormattedName();
    }

    @Override
    public void save(ProfileEditFragment.OnFragmentInteractionListener mListener) {
        class save {
            boolean needRefreshRepo=false;

            public save() {
                saveUser();
            }

            public void saveUser() {
                if (isNeedSaveUser(checkListWithoutPthoto)) {
                    profileChangeTask.execute(user_new_data, new SimpleSubscriber<ListResponse<User>>() {
                        @Override
                        public void onNext(ListResponse<User> userListResponse) {
                            super.onNext(userListResponse);
                            needRefreshRepo=true;
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
                if (isNeedSaveUser(checkListPhoto)) {
                    uploadAvatarTask.execute(new File(user_new_data.getThumbnail()), new SimpleSubscriber<String>() {
                        @Override
                        public void onNext(String string) {
                            super.onNext(string);
                            needRefreshRepo=true;
                            refreshUserRepo();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    refreshUserRepo();
                }
            }

            public void refreshUserRepo() {

                if(needRefreshRepo) {
                    loadProfileTask.execute(null, new SimpleSubscriber<UserProfile>() {
                        @Override
                        public void onNext(UserProfile userProfile) {
                            super.onNext(userProfile);
                            userRepo.save(userProfile.getUser());
                            Picasso.with(context).invalidate(userProfile.getUser().getThumbnail());
                            mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.USER_SAVED)));
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            view.commonError(e.getMessage());
                        }
                    });
                }else {
                    mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.USER_SAVED)));
                }
            }

        }
        new save();
    }

    @Override
    public boolean isNeedSaveUser(String... checkListCustom) {
        if (checkListCustom.length==0)
            checkListCustom=checkListAll;

        for (String s:checkListCustom) if(isNeedSaveField(s)) return true;

        return false;
    }

    @Override
    public void setNewPhoto(File file) {
        user_new_data.setThumbnail(file.getAbsolutePath());
    }

    @Override
    public View.OnClickListener getOnClickBirthday(FragmentActivity activity, User user, TextView text_birthday) {
        return v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    activity,
                    (view, year, month, dayOfMonth) -> {
                        Date date=new GregorianCalendar(year, month, dayOfMonth).getTime();
                        user_new_data.setBirthday(date);
                        text_birthday.setText(user_new_data.getFormatedBirthday());
                        activity.invalidateOptionsMenu();},
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();};
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
