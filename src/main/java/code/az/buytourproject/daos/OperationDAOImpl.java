package code.az.buytourproject.daos;

import code.az.buytourproject.daos.interfaces.OperationDAO;
import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.repositories.OperationRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationDAOImpl implements OperationDAO {

    OperationRepo operationRepo;

    public OperationDAOImpl(OperationRepo operationRepo) {
        this.operationRepo = operationRepo;
    }

    @Override
    public List<Operation> findOperationsByQuestion(Question question) {
        return operationRepo.findOperationsByQuestion(question);
    }

    @Override
    public Operation findFirstOperation() {
        return operationRepo.findFirstOperation();
    }

}
