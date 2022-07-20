package com.mvassoler.oauht.mvassoleroauth.domain;

import java.time.LocalDateTime;

public interface BaseEntity<ID> {

    ID getId();

    void setId(ID var1);

    LocalDateTime getCreated();

    void setCreated(LocalDateTime var1);

    LocalDateTime getUpdated();

    void setUpdated(LocalDateTime var1);

    LocalDateTime getDeleted();

    void setDeleted(LocalDateTime var1);
}
