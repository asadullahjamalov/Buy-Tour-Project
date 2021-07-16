package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.id =:id")
    Question findQuestionById(Long id);

    @Query("select q from Question q where q.isFirst=true")
    Question findFirstQuestion();

}