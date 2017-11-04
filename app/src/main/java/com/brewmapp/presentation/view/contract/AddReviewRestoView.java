package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Evaluation;
import com.brewmapp.data.entity.User;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 03.11.2017.
 */

public interface AddReviewRestoView  extends BasicView {

    void setEvaluation(List<Evaluation> restoDetail);
    void commonError(String... strings);

    void setUser(User restoDetail);

    void reviewSent();
}
