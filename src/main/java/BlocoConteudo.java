public abstract class BlocoConteudo {
    protected int ordem;

    public BlocoConteudo(int ordem) {
        this.ordem = ordem;
    }

    public int getOrdem() {
        return ordem;
    }

    public abstract String render();
}
