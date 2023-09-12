package com.db.voting.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Votes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "associate_id", referencedColumnName = "id")
    private Associate associate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

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
