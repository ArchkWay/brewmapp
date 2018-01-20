package com.brewmapp.data.entity.wrapper;

import com.brewmapp.data.entity.BaseEvaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kras on 20.01.2018.
 */

public class EvaluationData extends ArrayList<BaseEvaluation> {
    private String id_of_object_evaluation;
    private String type_of_object_evaluation;
    public EvaluationData(String id_of_object_evaluation, String type_of_object_evaluation) {
        this.id_of_object_evaluation=id_of_object_evaluation;
        this.type_of_object_evaluation=type_of_object_evaluation;

    }

    public String getId_of_object_evaluation() {
        return id_of_object_evaluation;
    }

    public void setId_of_object_evaluation(String id_of_object_evaluation) {
        this.id_of_object_evaluation = id_of_object_evaluation;
    }

    public String getType_of_object_evaluation() {
        return type_of_object_evaluation;
    }

    public void setType_of_object_evaluation(String type_of_object_evaluation) {
        this.type_of_object_evaluation = type_of_object_evaluation;
    }
}
