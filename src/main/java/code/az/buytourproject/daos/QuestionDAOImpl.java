package code.az.buytourproject.daos;

import code.az.buytourproject.daos.interfaces.QuestionDAO;
import code.az.buytourproject.models.Question;
import code.az.buytourproject.repositories.QuestionRepo;
import org.springframework.stereotype.Component;

@Component
public class QuestionDAOImpl implements QuestionDAO {

    QuestionRepo questionRepo;

    public QuestionDAOImpl(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Override
    public Question findFirstQuestion() {
        return questionRepo.findFirstQuestion();
    }
}
