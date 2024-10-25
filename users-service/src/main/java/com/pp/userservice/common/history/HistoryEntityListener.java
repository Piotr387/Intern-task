package com.pp.userservice.common.history;

import javax.persistence.PostLoad;

public class HistoryEntityListener {

    @PostLoad
    public void saveState(HistoryFieldsEntityTracker<?> target){
        target.cloneEntity();
    }
}