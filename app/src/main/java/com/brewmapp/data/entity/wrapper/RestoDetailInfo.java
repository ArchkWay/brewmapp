package com.brewmapp.data.entity.wrapper;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;

import java.sql.SQLException;
import java.sql.Wrapper;

import ru.frosteye.ovsa.presentation.view.ModelView;

/**
 * Created by xpusher on 10/27/2017.
 */

public class RestoDetailInfo<RestoDetail> implements ModelView<RestoDetail> {

    private RestoDetail model;

    @Override
    public void setModel(RestoDetail model) {
        this.model = model;
    }

    @Override
    public RestoDetail getModel() {
        return model;
    }
}
