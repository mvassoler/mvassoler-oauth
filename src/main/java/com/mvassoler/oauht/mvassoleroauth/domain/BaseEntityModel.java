package com.mvassoler.oauht.mvassoleroauth.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public abstract class BaseEntityModel implements BaseEntity<UUID> {
    @Id
    @GenericGenerator(
            name = "uuid2",
            strategy = "uuid2"
    )
    @GeneratedValue(
            generator = "uuid2"
    )
    @Column(
            name = "id",
            updatable = false,
            unique = true,
            nullable = false
    )
    private UUID id;
    @Column(
            name = "created",
            nullable = false
    )
    private LocalDateTime created;
    @Column(
            name = "updated",
            nullable = false
    )
    private LocalDateTime updated;
    @Column(
            name = "deleted"
    )
    private LocalDateTime deleted;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    public BaseEntityModel() {
    }

    public UUID getId() {
        return this.id;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public LocalDateTime getUpdated() {
        return this.updated;
    }

    public LocalDateTime getDeleted() {
        return this.deleted;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public void setDeleted(LocalDateTime deleted) {
        this.deleted = deleted;
    }


    protected boolean canEqual(Object other) {
        return other instanceof BaseEntityModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntityModel that = (BaseEntityModel) o;

        if (!id.equals(that.id)) return false;
        if (!created.equals(that.created)) return false;
        if (!updated.equals(that.updated)) return false;
        return deleted.equals(that.deleted);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + updated.hashCode();
        result = 31 * result + deleted.hashCode();
        return result;
    }

    public String toString() {
        return "BaseEntityModel(id=" + this.getId() + ", created=" + this.getCreated() + ", updated=" + this.getUpdated() + ", deleted=" + this.getDeleted() + ")";
    }
}