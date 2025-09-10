public class BlocoCodigo extends BlocoConteudo {
    private String linguagem;

    public BlocoCodigo(String titulo, String conteudo, String linguagem) {
        super(titulo, conteudo);
        this.linguagem = linguagem;
    }

    public String getLinguagem() {
        return linguagem;
    }
}