package com.pp.userservice.common;

import javax.persistence.PostLoad;

public class HistoryEntityListener {

    @PostLoad
    public void saveState(HistoryFieldsEntityTracker<?> target){
        target.cloneEntity();
    }
}