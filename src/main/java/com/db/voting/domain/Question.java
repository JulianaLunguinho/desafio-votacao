package com.db.voting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "question")
    private String content;

    @Column(name = "initial_datetime")
    private LocalDateTime initialDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "positive_votes")
    private Integer positiveVotes;

    @Column(name = "negative_votes")
    private Integer negativeVotes;

}
