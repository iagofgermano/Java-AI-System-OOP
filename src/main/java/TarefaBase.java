public abstract class TarefaBase implements TarefaIA {
    protected boolean ativa;

    public TarefaBase() {
        this.ativa = true;
    }

    public void validar(Parametros parametros) {
        if (!ativa) {
            throw new IllegalStateException("Tarefa não está ativa.");
        }
    }

    @Override
    public abstract Resultado executar(Parametros parametros);
}