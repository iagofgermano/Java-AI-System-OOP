import java.util.HashMap;
import java.util.Map;

public class RespostaSheet {
    private Map<Questao, Resposta> respostas;

    public RespostaSheet() {
        this.respostas = new HashMap<>();
    }

    public void adicionarResposta(Questao questao, Resposta resposta) {
        respostas.put(questao, resposta);
    }

    public Resposta getResposta(Questao questao) {
        return respostas.get(questao);
    }

    public Map<Questao, Resposta> getTodasRespostas() {
        return new HashMap<>(respostas);
    }
}