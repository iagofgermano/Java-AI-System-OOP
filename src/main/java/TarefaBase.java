public abstract class TarefaBase implements TarefaIA {
    protected boolean ativa;

    public TarefaBase() {
        this.ativa = true;
    }

    public abstract void validar(Parametros parametros);

    @Override
    public abstract Resultado executar(Parametros parametros);
}