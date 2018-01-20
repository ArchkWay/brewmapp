package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.BaseEvaluation;
import com.brewmapp.presentation.view.impl.widget.ItemEvaluationView;

import java.util.List;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 20.01.2018.
 */

public class EvaluationItem extends AdapterItem<EvaluationData, ItemEvaluationView> {

    public EvaluationItem(EvaluationData evaluations) {
        setModel(evaluations);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_evaluation_resto;
    }
}
