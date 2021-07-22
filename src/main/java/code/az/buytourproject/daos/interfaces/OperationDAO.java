package code.az.buytourproject.daos.interfaces;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;

import java.util.List;

public interface OperationDAO {

    List<Operation> findOperationsByQuestion(Question question);

    Operation findFirstOperation();

}
