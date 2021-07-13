package code.az.buytourproject.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "languages")
public class Language {
    @Id
    private Long id;
    private String lang;
    @OneToMany(mappedBy = "language")
    private List<Operation> operations;
    @OneToMany(mappedBy = "language")
    private List<Question> questions;
}
