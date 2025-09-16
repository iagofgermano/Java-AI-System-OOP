import java.time.LocalDateTime;
import java.util.UUID;

public class TentativaQuiz {

    private UUID id;
    private Aluno aluno;
    private Quiz quiz;
    private double nota;
    private boolean aprovado;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private int numeroTentativa;

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

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public int getNumeroTentativa() {
        return numeroTentativa;
    }

    public void setNumeroTentativa(int numeroTentativa) {
        this.numeroTentativa = numeroTentativa;
    }
}