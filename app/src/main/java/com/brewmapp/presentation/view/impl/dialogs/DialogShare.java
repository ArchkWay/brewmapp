package com.brewmapp.presentation.view.impl.dialogs;

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

import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.OvsaActivity;

/**
 * Created by Kras on 13.10.2017.
 */

public class DialogShare extends AlertDialog.Builder {
    public DialogShare(@NonNull Context context, String[] items, LivePresenter livePresenter,Object o) {
        super(context);
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
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                    break;
                case 1:
                    Intent intent = new Intent(context, NewPostActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    if (context instanceof OvsaActivity)
                        ((OvsaActivity) context).showMessage(" разработке");
                    break;
                case 3:
                    if(livePresenter instanceof EventsPresenter)
                        ((EventsPresenter)livePresenter).onDeleteNewsTask((Post) o);
                    break;
            }
        });
        show();
    }
}
