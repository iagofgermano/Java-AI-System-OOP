public class TarefaClassificacaoBinaria extends TarefaBase implements TarefaIA{

    @Override
    public void validar(Parametros parametros) {
        if (!parametros.get("texto").isPresent()) {
            throw new IllegalArgumentException("Parâmetro 'texto' é obrigatório.");
        }
    }

    @Override
    public Resultado executar(Parametros parametros) {
        validar(parametros);

        String texto = parametros.get("texto").orElse("");
        boolean resultado = classificarTexto(texto);

        Resultado resultadoObj = new Resultado(
                "Classificação binária concluída: " + (resultado ? "Positivo" : "Negativo")
        );

        resultadoObj.adicionarMetrica("texto_analisado", texto);
        resultadoObj.adicionarMetrica("classificacao", resultado ? "positivo" : "negativo");

        return resultadoObj;
    }

    private boolean classificarTexto(String texto) {
        String textoLower = texto.toLowerCase();
        return textoLower.contains("ótimo") ||
                textoLower.contains("bom") ||
                textoLower.contains("excelente") ||
                textoLower.contains("maravilhoso");
    }
}