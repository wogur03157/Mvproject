package com.mever.api.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Table(name = "reservation_mail")
public class ReservationMail {
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
    String period;
    @Column()
    String sendDate;
}