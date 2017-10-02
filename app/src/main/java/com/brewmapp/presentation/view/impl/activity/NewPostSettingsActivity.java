package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Post;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.NewPostSettingsPresenter;
import com.brewmapp.presentation.view.contract.NewPostSettingsView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.tool.DateTools;

public class NewPostSettingsActivity extends BaseActivity implements NewPostSettingsView {

    @BindView(R.id.activity_newPost_settings_date) TextView date;
    @BindView(R.id.activity_newPost_settings_dateSelector) View dateSelector;
    @BindView(R.id.activity_newPost_settings_friendsOnly) SwitchCompat friendsOnlySwitch;
    @BindView(R.id.common_toolbar) Toolbar toolbar;

    @Inject NewPostSettingsPresenter presenter;

    private boolean friendsOnly;
    private Date delayedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_settings);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();
        Post post = ((Post) getIntent().getSerializableExtra(Keys.POST));
        delayedDate = post.getDelayedDate();
        friendsOnly = post.isFriendsOnly();
        friendsOnlySwitch.setChecked(friendsOnly);
        date.setText(delayedDate == null ? getString(R.string.today) : DateTools.formatDottedDate(delayedDate));
        dateSelector.setOnClickListener(v -> {
            if(delayedDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(delayedDate);
                DateTools.showDateDialog(this, datePickerCallback, calendar, new Date());
            } else {
                DateTools.showDateDialog(this, datePickerCallback, new Date());
            }
        });
        friendsOnlySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.friendsOnly = isChecked;
        });
    }

    private DateTools.DatePickerCallback datePickerCallback = selected -> {
        DateTools.showTimeDialog(this, new DateTools.TimePickerCallback() {
            @Override
            public void onTimeSelected(String time) {

            }

            @Override
            public void onRaw(int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selected);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                delayedDate = calendar.getTime();
                date.setText(DateTools.formatDottedDateWithTime(calendar.getTime()));
            }
        });
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_allDone) {
            Intent intent = new Intent();
            intent.putExtra(Keys.FRIENDS_ONLY, friendsOnly);
            intent.putExtra(Keys.DELAYED_DATE, delayedDate);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }
}
