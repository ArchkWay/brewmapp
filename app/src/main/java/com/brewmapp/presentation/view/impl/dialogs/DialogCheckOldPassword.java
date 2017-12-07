package com.brewmapp.presentation.view.impl.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by Kras on 02.12.2017.
 */
@SuppressLint("ValidFragment")
public class DialogCheckOldPassword extends DialogFragment implements DialogInterface.OnClickListener{

    private OnValidConfirmPassword onValidConfirmPassword;
    private EditText input;

    @SuppressLint("ValidFragment")
    public DialogCheckOldPassword(FragmentManager supportManagerFragment, OnValidConfirmPassword onValidConfirmPassword){
        this.onValidConfirmPassword = onValidConfirmPassword;
        show(supportManagerFragment,"ssss");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        input = new EditText(getActivity());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);    //edit text added to alert
        alert.setTitle(R.string.enter_old_password);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.ok, (dialog, which) -> requestCheckUser());
        alert.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return alert.create();
    }

    private void requestCheckUser() {

        new checkUser(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        ).execute(input.getText().toString(),new SimpleSubscriber<User>(){
            @Override
            public void onNext(User user) {
                super.onNext(user);
                onValidConfirmPassword.onValidPass();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onValidConfirmPassword.onErrorPass();
            }
        });
    }

    public interface OnValidConfirmPassword {
        public void onValidPass();
        public void onErrorPass();
    }

    class checkUser extends BaseNetworkTask<String, User>{

        public checkUser(MainThread mainThread, Executor executor, Api api) {
            super(mainThread, executor, api);
        }

        @Override
        protected Observable<User> prepareObservable(String string) {
            return Observable.create(subscriber -> {
                try {
                    RequestParams params = new RequestParams();
                    params.addParam(Keys.PASSWORD,string);
                    SingleResponse<User> response = executeCall(getApi().chkUser(params));
                    subscriber.onNext(response.getData());
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }
    }
}
