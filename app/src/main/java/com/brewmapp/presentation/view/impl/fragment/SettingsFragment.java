package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.brewmapp.BuildConfig;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.contract.SettingsView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileActivity;
import com.brewmapp.presentation.view.impl.activity.StartActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SettingsFragment extends BaseFragment implements SettingsView {

    @BindView(R.id.fragment_setting_about) View about;
    @BindView(R.id.fragment_setting_help) View help;
    @BindView(R.id.fragment_setting_write_to_us) View write_to_us;
    @BindView(R.id.fragment_setting_terms_of_use) View terms_of_use;
    @BindView(R.id.fragment_setting_profile) View profile;
    @BindView(R.id.fragment_setting_change_password) View change_password;
    @BindView(R.id.fragment_setting_change_phone) View change_phone;
    @BindView(R.id.fragment_setting_auth_facebook) View auth_facebook;
    @BindView(R.id.fragment_setting_delete_account) View delete_account;
    @BindView(R.id.fragment_setting_simple_exit) View simple_exit;
    @BindView(R.id.fragment_setting_all_devices_exit) View all_devices_exit;

    @Inject SettingsPresenter presenter;
    @Inject    MainPresenter presenterMain;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_settings;
    }


    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        about.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_ABOUT,null,getActivity(), MultiFragmentActivity.class)));
        help.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_WEBVIEW, Uri.parse(BuildConfig.SERVER_ROOT_URL),getActivity(), MultiFragmentActivity.class)));
        write_to_us.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        terms_of_use.setOnClickListener(view1 -> startActivity(new Intent(MultiFragmentActivityView.MODE_WEBVIEW, Uri.parse("https://brewmapp.com/company/terms"),getActivity(), MultiFragmentActivity.class)));
        profile.setOnClickListener(v -> startActivity(new Intent(String.valueOf(ProfileActivity_view.SHOW_FRAGMENT_EDIT),null,getActivity(), ProfileActivity.class)));
        change_password.setOnClickListener(v -> presenter.setPassword(getActivity()));
        change_phone.setOnClickListener(v -> startActivity(new Intent(String.valueOf(ProfileActivity_view.SHOW_FRAGMENT_EDIT),null,getActivity(), ProfileActivity.class)));
        simple_exit.setOnClickListener(v -> new DialogConfirm(getString(R.string.message_quit), getActivity().getSupportFragmentManager(), new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                presenterMain.onLogout();
                startActivity(new Intent(v.getContext(),StartActivity.class));
                getActivity().finish();
            }

            @Override
            public void onCancel() {

            }
        }));
        all_devices_exit.setOnClickListener(v -> new DialogConfirm(getString(R.string.message_quit_all_devices), getActivity().getSupportFragmentManager(), new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                showMessage(getString(R.string.message_develop));
            }

            @Override
            public void onCancel() {

            }
        }));
        //auth_facebook.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));

        delete_account.setOnClickListener(v -> new DialogConfirm(getString(R.string.message_delete_account), getActivity().getSupportFragmentManager(), new DialogConfirm.OnConfirm() {
            @Override
            public void onOk() {
                //showMessage(getString(R.string.message_develop));
                class deleteUser extends BaseNetworkTask<Void, String>{

                    public deleteUser(MainThread mainThread, Executor executor, Api api) {
                        super(mainThread, executor, api);
                    }

                    @Override
                    protected Observable<String> prepareObservable(Void v) {
                        return Observable.create(subscriber -> {
                            try {
                                executeCall(getApi().deleteUser(new RequestParams()));
                                subscriber.onNext("");
                                subscriber.onComplete();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        });
                    }
                }
                new deleteUser(
                        BeerMap.getAppComponent().mainThread(),
                        BeerMap.getAppComponent().executor(),
                        BeerMap.getAppComponent().api()
                ).execute(null,new SimpleSubscriber<String>(){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        presenterMain.onLogout();
                        startActivity(new Intent(v.getContext(),StartActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showMessage(getString(R.string.error),0);
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        }));

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
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
        return ResourceHelper.getString(R.string.settings);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.stub,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
    }

    @Override
    public void onBarAction(int id) {
        super.onBarAction(id);
    }


}
