public class TarefaPrompting extends TarefaBase implements TarefaIA {

    public TarefaPrompting() {
        this.ativa = true;
    }

    @Override
    public void validar(Parametros parametros) {
        // Valida se os parâmetros necessários para prompting estão presentes
        if (!parametros.get("prompt").isPresent()) {
            throw new IllegalArgumentException("Parâmetro 'prompt' é obrigatório.");
        }
    }

    @Override
    public Resultado executar(Parametros parametros) {
        validar(parametros);

        String prompt = parametros.get("prompt").orElse("");
        // Simulação de execução de prompting
        String respostaSimulada = "[Resposta simulada para o prompt: " + prompt + "]";

        Resultado resultado = new Resultado("Execução de prompting concluída com sucesso.");
        resultado.adicionarMetrica("prompt", prompt);
        resultado.adicionarMetrica("resposta", respostaSimulada);
        resultado.adicionarMetrica("tempo_execucao_ms", "125");

        return resultado;
    }
}