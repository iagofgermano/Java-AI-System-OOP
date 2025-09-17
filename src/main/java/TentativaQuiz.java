import java.time.LocalDateTime;
import java.util.UUID;

public class TentativaQuiz {

    private final UUID id;
    private Aluno aluno;
    private final Quiz quiz;
    private final double nota;
    private final boolean aprovado;
    private final LocalDateTime inicio;
    private final LocalDateTime fim;
    private final int numeroTentativa;

    // Construtor
    public TentativaQuiz(Aluno aluno, Quiz quiz, double nota, boolean aprovado,
                         LocalDateTime inicio, LocalDateTime fim, int numeroTentativa) {
        this.id = UUID.randomUUID();
        this.aluno = aluno;
        this.quiz = quiz;
        this.nota = nota;
        this.aprovado = aprovado;
        this.inicio = inicio;
        this.fim = fim;
        this.numeroTentativa = numeroTentativa;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public double getNota() {
        return nota;
    }
}