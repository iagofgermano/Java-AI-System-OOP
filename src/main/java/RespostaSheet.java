import java.util.*;

public class RespostaSheet {
    // Atributo que mapeia Questões para suas respectivas Respostas
    private Map<Questao, Resposta> respostas;

    // Construtor
    public RespostaSheet() {
        this.respostas = new HashMap<>();
    }

    // Adiciona uma resposta para uma determinada questão
    public void adicionar(Questao questao, Resposta resposta) {
        respostas.put(questao, resposta);
    }

    // Obtém a resposta associada a uma determinada questão
    public Resposta obter(Questao questao) {
        return respostas.get(questao);
    }

    // Retorna o número total de respostas registradas
    public int tamanho() {
        return respostas.size();
    }

    // Método auxiliar para obter todas as entradas (opcional)
    public Set<Map.Entry<Questao, Resposta>> getEntradas() {
        return respostas.entrySet();
    }
}