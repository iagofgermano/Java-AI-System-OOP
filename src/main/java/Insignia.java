public class Insignia {
    private String nome;
    private String descricao;
    private String criterio;

    public Insignia(String nome, String descricao, String criterio) {
        this.nome = nome;
        this.descricao = descricao;
        this.criterio = criterio;
    }

    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCriterio() { return criterio; }
}