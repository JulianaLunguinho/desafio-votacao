package com.db.voting.repository;

import com.db.voting.domain.Associate;
import com.db.voting.domain.Question;
import com.db.voting.domain.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Long> {

    Integer countByAssociateAndQuestion(Associate associate, Question question);

}
