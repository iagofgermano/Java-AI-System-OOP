import java.time.LocalDateTime;
import java.util.UUID;

public class InsigniaDoUsuario {
    private UUID id;
    private Aluno aluno;
    private Insignia insignia;
    private LocalDateTime data;

    // Construtor
    public InsigniaDoUsuario(Aluno aluno, Insignia insignia, LocalDateTime data) {
        this.id = UUID.randomUUID();
        this.aluno = aluno;
        this.insignia = insignia;
        this.data = data;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Insignia getInsignia() {
        return insignia;
    }

    public LocalDateTime getData() {
        return data;
    }

    // (Opcional) Setters, se necessário
    // public void setAluno(Aluno aluno) { this.aluno = aluno; }
    // public void setInsignia(Insignia insignia) { this.insignia = insignia; }
    // public void setData(LocalDateTime data) { this.data = data; }
}