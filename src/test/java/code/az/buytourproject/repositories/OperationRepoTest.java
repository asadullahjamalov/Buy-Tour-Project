package code.az.buytourproject.repositories;

import code.az.buytourproject.models.Operation;
import code.az.buytourproject.models.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OperationRepoTest {

    @Autowired
    private OperationRepo underTest;

    @Autowired
    private  QuestionRepo questionRepo;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }


    @Test
    void findOperationsByQuestion() {
        Question question1 = new Question();
        questionRepo.save(question1);
        Operation operation1 = Operation.builder()
                .question(question1)
                .build();
        underTest.save(operation1);
        Operation operation2 = Operation.builder()
                .question(question1)
                .build();
        underTest.save(operation2);
        Operation operation3 = Operation.builder()
                .question(question1)
                .build();
        underTest.save(operation3);

        Question question2 = new Question();
        questionRepo.save(question2);
        Operation operation4 = Operation.builder()
                .question(question2)
                .build();
        underTest.save(operation4);
        Operation operation5 = Operation.builder()
                .question(question2)
                .build();
        underTest.save(operation5);

        List<Operation> operationList1 = underTest.findOperationsByQuestion(question1);
        List<Operation> operationList2 = underTest.findOperationsByQuestion(question2);

        assertThat(operationList1).isEqualTo(Arrays.asList(operation1, operation2, operation3));
        assertThat(operationList2).isEqualTo(Arrays.asList(operation4, operation5));

    }

    @Test
    void findFirstOperation() {
        Question question1 = Question.builder().isFirst(true).build();
        questionRepo.save(question1);
        Operation operation1 = Operation.builder()
                .question(question1)
                .build();
        underTest.save(operation1);

        Question question2 = Question.builder().isFirst(false).build();
        questionRepo.save(question2);
        Operation operation2 = Operation.builder()
                .question(question2)
                .build();
        underTest.save(operation2);


        Operation operation = underTest.findFirstOperation();

        assertThat(operation).isEqualTo(operation1);
    }
}