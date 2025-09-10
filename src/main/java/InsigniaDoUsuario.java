import java.time.LocalDateTime;

public class InsigniaDoUsuario {
    private Insignia insignia;
    private Aluno aluno;
    private LocalDateTime dataConquista;

    public InsigniaDoUsuario(Insignia insignia, Aluno aluno) {
        this.insignia = insignia;
        this.aluno = aluno;
        this.dataConquista = LocalDateTime.now();
    }

    // Getters
    public Insignia getInsignia() { return insignia; }
    public Aluno getAluno() { return aluno; }
    public LocalDateTime getDataConquista() { return dataConquista; }
}