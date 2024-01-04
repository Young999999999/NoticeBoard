package com.codingrecipe.BootNoticeBoard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //생성시 시간 정보를 불러옴
    @Column(updatable = false) //update시에는 관여 못하게
    private LocalDateTime createdTime;

    @UpdateTimestamp //업데이트시 시간 정보를 불러옴
    @Column(insertable = false) //생성시에는 관여 못하게
    private LocalDateTime updatedTime;

}
