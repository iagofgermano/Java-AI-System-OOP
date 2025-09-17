public class BlocoTexto extends BlocoConteudo {
    private String texto;

    public BlocoTexto(int ordem, String texto) {
        super(ordem);
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String render() {
        return texto;
    }
}
