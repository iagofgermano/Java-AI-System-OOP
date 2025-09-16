public class TarefaBase {
    protected boolean ativo;

    public boolean isAtivo() { return ativo; }

    public abstract Resultado executar(Parametros parametros);
}
