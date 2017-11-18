package com.brewmapp.utils.events;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;

/**
 * Created by nixus on 14.11.2017.
 */

public class SelectedFilterEvent {
    private List<IFlexible> filteredList;

    public SelectedFilterEvent(List<IFlexible> filteredList) {
        this.filteredList = filteredList;
    }

    public List<IFlexible> getFilteredList() {
        return filteredList;
    }
}
