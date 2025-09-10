public abstract class BlocoConteudo {
    protected String titulo;
    protected String conteudo;

    public BlocoConteudo(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getConteudo() { return conteudo; }
}