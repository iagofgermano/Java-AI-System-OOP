public class BlocoCodigo extends BlocoConteudo {
    private String linguagem;
    private String codigo;

    public BlocoCodigo(int ordem, String linguagem, String codigo) {
        super(ordem);
        this.linguagem = linguagem;
        this.codigo = codigo;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String render() {
        return "```" + linguagem + "\n" + codigo + "\n```";
    }
}
