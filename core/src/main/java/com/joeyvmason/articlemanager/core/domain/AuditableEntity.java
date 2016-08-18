package com.joeyvmason.articlemanager.core.domain;

import com.google.common.collect.ComparisonChain;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public abstract class AuditableEntity extends Identifiable {

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    public static Comparator<AuditableEntity> timeCreatedAscComparator() {
        return (thisEntity, thatEntity) -> ComparisonChain.start().compare(thisEntity.getCreated(), thatEntity.getCreated()).result();
    }

    public static Comparator<AuditableEntity> timeCreatedDescComparator() {
        return Collections.reverseOrder(timeCreatedAscComparator());
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void updateTimestamps(Date now) {
        if (created == null) {
            created = now;
        }
        updated = now;
    }
}