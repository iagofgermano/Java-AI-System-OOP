/**
 * Interface que representa algo que pode ser avaliado.
 */
public interface Avaliavel {
    /**
     * Calcula a nota com base em uma RespostaSheet.
     *
     * @param respostaSheet Objeto contendo as respostas do usuário
     * @return Nota calculada (0.0 até 10.0, por exemplo)
     */
    double calcularNota(RespostaSheet respostaSheet);
}
