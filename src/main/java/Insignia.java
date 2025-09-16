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

    // Getters
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Concede a insígnia ao aluno se ele atender aos critérios.
     * @param aluno O aluno que pode receber a insígnia
     * @param curso O curso relacionado à concessão
     * @return true se a insígnia foi concedida, false caso contrário
     */
    public boolean conceder(Aluno aluno, Curso curso) {
        // Lógica de verificação de elegibilidade deve ser implementada aqui
        // Por exemplo: verificar se o aluno completou o curso
        // Esta é uma implementação placeholder
        return false;
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