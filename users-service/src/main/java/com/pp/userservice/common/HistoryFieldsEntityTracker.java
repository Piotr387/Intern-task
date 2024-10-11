package com.pp.userservice.common;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

@Getter
@Setter
@MappedSuperclass
public class HistoryFieldsEntityTracker<CloneableEntity> implements Serializable {

    @Transient
    public CloneableEntity previousState;

    // start L1 Prototype

    /**
     * Each Entity class which inherit this class have possbility of
     * tracking entity changes by making deep copy after fetching entity from database
     */
    @SuppressWarnings("all")
    public void cloneEntity() {
        this.previousState = (CloneableEntity) SerializationUtils.clone(this);
    }
}
