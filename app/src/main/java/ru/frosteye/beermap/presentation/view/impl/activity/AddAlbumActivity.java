package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.pojo.NewAlbumPackage;
import ru.frosteye.beermap.presentation.presenter.contract.AddAlbumPresenter;
import ru.frosteye.beermap.presentation.view.contract.AddAlbumView;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;
import ru.frosteye.ovsa.tool.TextTools;

public class AddAlbumActivity extends BaseActivity implements AddAlbumView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_addAlbum_description) TextView description;
    @BindView(R.id.activity_addAlbum_name) TextView name;

    @Inject AddAlbumPresenter presenter;

    private NewAlbumPackage newAlbumPackage = new NewAlbumPackage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
        description.setEnabled(enabled);
        name.setEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        menu.findItem(R.id.action_done).setEnabled(newAlbumPackage.validate());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done) {
            presenter.onNewAlbumRequestReady(newAlbumPackage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        enableBackButton();
        registerTextChangeListeners(s -> {
            newAlbumPackage.setDescription(TextTools.extractTrimmed(description));
            newAlbumPackage.setName(TextTools.extractTrimmed(name));
            invalidateOptionsMenu();
        }, description, name);
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

    @Override
    public void completeCreation() {
        showMessage(getString(R.string.album_created));
        setResult(RESULT_OK);
        finish();
    }
}
