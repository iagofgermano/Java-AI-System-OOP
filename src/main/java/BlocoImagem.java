public class BlocoImagem extends BlocoConteudo {
    private String caminho;
    private String descricaoAlt;

    public BlocoImagem(int ordem, String caminho, String descricaoAlt) {
        super(ordem);
        this.caminho = caminho;
        this.descricaoAlt = descricaoAlt;
    }

    @Override
    public String render() {
        return "<img src='" + caminho + "' alt='" + descricaoAlt + "' />";
    }
}
