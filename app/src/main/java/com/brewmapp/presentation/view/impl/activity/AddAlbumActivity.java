package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.pojo.EditAlbumPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.AddAlbumPresenter;
import com.brewmapp.presentation.view.contract.AddAlbumView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.tool.TextTools;

public class AddAlbumActivity extends BaseActivity implements AddAlbumView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_addAlbum_description) EditText description;
    @BindView(R.id.activity_addAlbum_name) EditText name;

    @Inject AddAlbumPresenter presenter;

    private EditAlbumPackage editAlbumPackage = new EditAlbumPackage();

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
        menu.findItem(R.id.action_done).setEnabled(editAlbumPackage.validate());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done) {
            presenter.onNewAlbumRequestReady(editAlbumPackage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        Album.SimpleAlbum album = ((Album.SimpleAlbum) getIntent().getSerializableExtra(Keys.ALBUM));
        if(album != null) {
            editAlbumPackage.setAlbumId(album.getAlbumId());
            editAlbumPackage.setDescription(album.getDescription());
            editAlbumPackage.setName(album.getTitle());
            name.setText(editAlbumPackage.getName());
            description.setText(editAlbumPackage.getDescription());
            name.setSelection(editAlbumPackage.getName().length());
        }
        enableBackButton();
        registerTextChangeListeners(s -> {
            editAlbumPackage.setDescription(TextTools.extractTrimmed(description));
            editAlbumPackage.setName(TextTools.extractTrimmed(name));
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
