package com.mever.api.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Getter
@Setter
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "send_history")
public class SendHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long seq;
    @Column()
    String title;
    @Column()
    String email;
    @Column()
    String content;
    @Column()
    String phone;
    @Column()
    String type;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date date;
}