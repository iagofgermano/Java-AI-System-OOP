public class TarefaClassificacaoBinaria extends TarefaBase {

    @Override
    public Resultado executar(Parametros parametros) {
        validar(parametros);

        // Simulação de lógica de classificação binária
        String resumo = "Executada classificação binária com os parâmetros fornecidos.";
        Resultado resultado = new Resultado(resumo);
        resultado.adicionarMetrica("acuracia", "0.92");
        resultado.adicionarMetrica("tempo_execucao_ms", "120");

        return resultado;
    }
}