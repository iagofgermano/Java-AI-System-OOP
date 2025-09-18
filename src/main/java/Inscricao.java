import java.util.UUID;

public class Inscricao {

    private UUID id;
    private Aluno aluno;
    private Curso curso;
    private StatusInscricao status;

    public Inscricao(Aluno aluno, Curso curso) {
        this.id = UUID.randomUUID();
        this.aluno = aluno;
        this.curso = curso;
        this.status = StatusInscricao.ACTIVE;
    }

    public Inscricao(UUID id, Aluno aluno, Curso curso, StatusInscricao status) {
        this.id = id;
        this.aluno = aluno;
        this.curso = curso;
        this.status = status;
    }

    public void marcarConcluida() {
        if (this.status == StatusInscricao.ACTIVE) {
            this.status = StatusInscricao.COMPLETED;
        }
    }

    public void arquivar() {
        this.status = StatusInscricao.ARCHIVED;
    }

    public UUID getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public StatusInscricao getStatus() {
        return status;
    }
}