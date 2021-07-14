package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepo extends JpaRepository<Operation, Long> {

    Operation findOperationByQuestion(Question question);

    @Query("select a.nextQuestion from Operation a where a.id =:id")
    Question findNextQuestionById(Long id);

}