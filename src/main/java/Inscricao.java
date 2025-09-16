import java.time.LocalDateTime;
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
        this.status = StatusInscricao.ACTIVE; // Status inicial padrão
    }

    // Métodos

    /**
     * Marca a inscrição como concluída.
     */
    public void marcarConcluida() {
        if (this.status == StatusInscricao.ACTIVE) {
            this.status = StatusInscricao.COMPLETED;
        }
    }

    /**
     * Arquiva a inscrição.
     */
    public void arquivar() {
        this.status = StatusInscricao.ARCHIVED;
    }

    // Getters e Setters

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

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }
}