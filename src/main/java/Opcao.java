public class Opcao {
    private String texto;
    private boolean correta;


    public Opcao(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
    }

    // Getters
    public String getTexto() {
        return texto;
    }

    public boolean isCorreta() {
        return correta;
    }

    // Setters (opcionais - só se você quiser alterar depois de criar)
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    @Override
    public String toString() {
        return "Opcao{" +
                "texto='" + texto + '\'' +
                ", correta=" + correta +
                '}';
    }
}
