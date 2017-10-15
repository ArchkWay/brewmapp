package com.brewmapp.presentation.view.impl.dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.ClaimTask;
import com.brewmapp.execution.task.DeleteNewsTask;
import com.brewmapp.execution.task.LoadClaimTypesTask;
import com.brewmapp.presentation.view.contract.ShareDialog;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.IPrompt;
import ru.frosteye.ovsa.presentation.view.SimplePrompt;
import ru.frosteye.ovsa.tool.UITools;

/**
 * Created by Kras on 13.10.2017.
 */

public class DialogShare extends AlertDialog.Builder {

    @Inject    LoadClaimTypesTask loadClaimTypesTask;
    @Inject    ClaimTask claimTask;


    public DialogShare(@NonNull BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        super(context);

        BeerMap.getAppComponent().plus(new PresenterModule(context)).inject(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(position == 3 ? Color.RED : Color.BLACK);
                        return view;
                    }
                };
        for (String s : items) arrayAdapter.add(s);
        setAdapter(arrayAdapter, (dialog, which) -> {
            switch (which) {
                case 0:
                    selectSend(context,items,iLikeable,shareDialog);
                    break;
                case 1:
                    selectRepost(context,items,iLikeable,shareDialog);
                    break;
                case 2:
                    selectClaim(context,items,iLikeable,shareDialog);
                    break;
                case 3:
                    selectDelete(context,items,iLikeable,shareDialog);
                    break;
            }
        });
        show();
    }

    private void selectDelete(BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        shareDialog.onDelete();
    }

    private void selectClaim(BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        loadClaimTypesTask.execute(null, new SimpleSubscriber<String[]>() {
            @Override
            public void onError(Throwable e) {
                context.showMessage(e.getMessage());
            }

            @Override
            public void onNext(String[] variants) {
                context.showSelect(context, variants, (text1, position) -> {
                    UITools.showPrompt(context, new SimplePrompt()
                            .setTitle(context.getString(R.string.alert))
                            .setHint(context.getString(R.string.enter_message))
                            .setPositiveButton(context.getString(R.string.send)), new IPrompt.Listener() {
                        @Override
                        public void onResult(String text) {
                            ClaimPackage claimPackage;
                            if(iLikeable instanceof Post)
                                claimPackage = new ClaimPackage(position, ((Post)iLikeable).getId(), Keys.CAP_NEWS);
                            else if(iLikeable instanceof Event)
                                claimPackage = new ClaimPackage(position, ((Event)iLikeable).getId(), Keys.CAP_EVENT);
                            else if(iLikeable instanceof Sale)
                                claimPackage = new ClaimPackage(position, ((Sale)iLikeable).getId(), Keys.CAP_SHARE);
                            else
                                return;
                            claimPackage.setText(text);
                            claimTask.execute(claimPackage,new SimpleSubscriber<Boolean>(){
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    super.onNext(aBoolean);
                                    if(aBoolean)context.showMessage(context.getString(R.string.claim_recieve));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    context.showMessage(e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }, false);

            }
        });

    }

    private void selectRepost(BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        Intent intent = new Intent(context, NewPostActivity.class);
        context.startActivity(intent);
    }

    private void selectSend(BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        String text="";
        if(iLikeable instanceof Post)
            text=((Post)iLikeable).getText();
        else if(iLikeable instanceof Event)
            text=((Event)iLikeable).getText();
        else if(iLikeable instanceof Sale)
            text=((Sale)iLikeable).getText();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);

    }
}
