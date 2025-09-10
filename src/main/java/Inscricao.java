import java.time.LocalDateTime;

public class Inscricao {
    private Aluno aluno;
    private Curso curso;
    private LocalDateTime dataInscricao;
    private boolean ativa;

    public Inscricao(Aluno aluno, Curso curso) {
        this.aluno = aluno;
        this.curso = curso;
        this.dataInscricao = LocalDateTime.now();
        this.ativa = true;
    }

    // Getters e Setters
    public Aluno getAluno() { return aluno; }
    public Curso getCurso() { return curso; }
    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}