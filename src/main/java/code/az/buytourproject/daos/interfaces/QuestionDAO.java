package code.az.buytourproject.daos.interfaces;

import code.az.buytourproject.models.Question;
import org.springframework.data.jpa.repository.Query;

public interface QuestionDAO {

    Question findQuestionById(Long id);

    Question findFirstQuestion();
}
