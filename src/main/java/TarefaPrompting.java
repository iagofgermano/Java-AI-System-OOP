public class TarefaPrompting extends TarefaBase {

    @Override
    public Resultado executar(Parametros parametros) {
        validar(parametros);

        String prompt = parametros.get("prompt").orElse("Nenhum prompt fornecido.");
        String resposta = "Resposta simulada para o prompt: " + prompt;

        Resultado resultado = new Resultado("Prompting executado com sucesso.");
        resultado.adicionarMetrica("prompt", prompt);
        resultado.adicionarMetrica("resposta", resposta);

        return resultado;
    }
}