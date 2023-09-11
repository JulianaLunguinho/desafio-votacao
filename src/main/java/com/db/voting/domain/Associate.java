package com.db.voting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Associate {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

}
