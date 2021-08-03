package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class QuestionRepoTest {

    @Autowired
    private QuestionRepo underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findFirstQuestion() {
        Question question1 = Question.builder().isFirst(true).build();
        underTest.save(question1);

        Question question2 = Question.builder().isFirst(false).build();
        underTest.save(question2);

        Question question = underTest.findFirstQuestion();

        assertThat(question).isEqualTo(question1);

    }

}