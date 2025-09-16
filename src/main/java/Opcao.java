public class Opcao {
    private String texto;
    private boolean correta;

    public Opcao(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
    }

    public String getTexto() { return texto; }
    public boolean isCorreta() { return correta; }

    public void opcoesTexto(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
    }
}
