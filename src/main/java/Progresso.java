import java.time.LocalDateTime;
import java.util.UUID;

public class Progresso {
    private UUID id;
    private Aluno aluno;
    private Aula aula;
    private StatusProgresso status;
    private LocalDateTime ultimaVisualizacao;

    public Progresso(Aluno aluno, Aula aula) {
        this.id = UUID.randomUUID();
        this.aluno = aluno;
        this.aula = aula;
        this.status = StatusProgresso.NOT_STARTED;
        this.ultimaVisualizacao = LocalDateTime.now();
    }

    public void marcarEmProgresso() {
        this.status = StatusProgresso.IN_PROGRESS;
        this.ultimaVisualizacao = LocalDateTime.now();
    }

    public void marcarConcluido() {
        this.status = StatusProgresso.COMPLETED;
        this.ultimaVisualizacao = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Aula getAula() {
        return aula;
    }

    public StatusProgresso getStatus() {
        return status;
    }

    public LocalDateTime getUltimaVisualizacao() {
        return ultimaVisualizacao;
    }
}
