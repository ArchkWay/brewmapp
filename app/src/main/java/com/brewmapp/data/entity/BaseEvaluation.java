package com.brewmapp.data.entity;

import com.brewmapp.presentation.view.impl.widget.ItemEvaluationView;

import java.io.Serializable;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 20.01.2018.
 */

public class BaseEvaluation  implements Serializable{
    protected String id;
    protected String user_id;
    protected String evaluation_type;
    protected String evaluation_value;
    protected String created_at;

}
