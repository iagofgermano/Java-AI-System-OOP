import java.util.*;

public class RespostaSheet {
    private Map<Questao, Resposta> respostas;

    public RespostaSheet() {
        this.respostas = new HashMap<>();
    }

    public void adicionar(Questao questao, Resposta resposta) {
        respostas.put(questao, resposta);
    }

    public Resposta obter(Questao questao) {
        return respostas.get(questao);
    }

    public int tamanho() {
        return respostas.size();
    }

    public Set<Map.Entry<Questao, Resposta>> getEntradas() {
        return respostas.entrySet();
    }
}