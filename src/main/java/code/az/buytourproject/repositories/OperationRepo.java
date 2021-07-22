package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepo extends JpaRepository<Operation, Long> {

    List<Operation> findOperationsByQuestion(Question question);

    @Query("select o from Operation o where o.question.isFirst=true")
    Operation findFirstOperation();

}