package com.brewmapp.presentation.view.impl.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.execution.task.LoadComplaint;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.ShareDialog;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.OvsaActivity;

/**
 * Created by Kras on 13.10.2017.
 */

public class DialogShare extends AlertDialog.Builder {

    @Inject    LoadComplaint loadComplaint;

    public DialogShare(@NonNull BaseActivity context, String[] items, ILikeable iLikeable, ShareDialog shareDialog) {
        super(context);

        BeerMap.getAppComponent().plus(new PresenterModule(context)).inject(this);
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {
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
                    break;
                case 1:
                    Intent intent = new Intent(context, NewPostActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    //shareDialog.onComplaint();
                    break;
                case 3:
                    shareDialog.onDelete();

                    break;
            }
        });
        show();
    }
}
