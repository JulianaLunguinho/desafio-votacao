package com.db.voting.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table
@Entity
@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Votes votes = (Votes) o;
        return id != null && Objects.equals(id, votes.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
