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
    public List<Operation> findOperationByQuestion(Question question) {
        return operationRepo.findOperationByQuestion(question);
    }

    @Override
    public Question findNextQuestionById(Long id) {
        return operationRepo.findNextQuestionById(id);
    }

    @Override
    public Operation findFirstOperation() {
        return operationRepo.findFirstOperation();
    }

    @Override
    public List<Operation> getOperationsByQuestion(Question question) {
        return operationRepo.getOperationsByQuestion(question);
    }

    @Override
    public List<Operation> getOperationsByNextQuestion(Long id) {
        return operationRepo.getOperationsByNextQuestion(id);
    }

    @Override
    public Operation getOperationByText_az(String text) {
        return operationRepo.getOperationByText_az(text);
    }

    @Override
    public Operation getOperationByText_ru(String text) {
        return operationRepo.getOperationByText_ru(text);
    }

    @Override
    public Operation getOperationByText_en(String text) {
        return operationRepo.getOperationByText_en(text);
    }
}
