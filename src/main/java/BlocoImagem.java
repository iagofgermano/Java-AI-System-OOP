public class BlocoImagem extends BlocoConteudo {
    private String urlImagem;

    public BlocoImagem(String titulo, String conteudo, String urlImagem) {
        super(titulo, conteudo);
        this.urlImagem = urlImagem;
    }

    public String getUrlImagem() {
        return urlImagem;
    }
}