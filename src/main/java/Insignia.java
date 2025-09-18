import java.time.LocalDateTime;
import java.util.UUID;

public class Insignia {
    private UUID id;
    private String nome;
    private String descricao;

    public Insignia(String nome, String descricao) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean conceder(Aluno aluno, Curso curso) {
        for (Aula aula : curso.listarAulas()) {
            if (!aula.concluidaPor(aluno)) {
                return false;
            }
        }

        LocalDateTime data = LocalDateTime.now();
        InsigniaDoUsuario insigniaDoUsuario = new InsigniaDoUsuario(aluno, this, data);
        aluno.adicionarInsignia(insigniaDoUsuario);
        return true;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Insignia insignia = (Insignia) o;
        return id.equals(insignia.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Insignia{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}