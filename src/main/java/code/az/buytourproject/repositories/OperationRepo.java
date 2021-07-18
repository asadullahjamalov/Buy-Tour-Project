package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {

    List<Operation> findOperationByQuestion(Question question);

    @Query("select a.nextQuestion from Operation a where a.id =:id")
    Question findNextQuestionById(Long id);

    @Query("select a from Operation a where a.question.isFirst=true")
    Operation findFirstOperation();

    @Query("select a from Operation a where a.question=:question")
    List<Operation> getOperationsByQuestion(Question question);

    @Query("select a from Operation a where a.nextQuestion.id=:id")
    List<Operation> getOperationsByNextQuestion(Long id);


}