package code.az.buytourproject.daos.interfaces;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationDAO {
    List<Operation> findOperationByQuestion(Question question);

    Question findNextQuestionById(Long id);

    Operation findFirstOperation();

    List<Operation> getOperationsByQuestion(Question question);

    List<Operation> getOperationsByNextQuestion(Long id);
}
