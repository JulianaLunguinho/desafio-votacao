package com.db.voting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Votes {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "associate_id")
    private Long associateId;

    @Column(name = "question_id")
    private Long questionId;

}
