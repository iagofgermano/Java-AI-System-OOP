import java.util.ArrayList;
import java.util.List;

public class QMultiplaSelecao extends Questao {
    private List<String> opcoes;
    private int indiceRespostaCorreta;

    public QMultiplaSelecao(String enunciado) {
        super(enunciado);
        this.opcoes = new ArrayList<>();
    }

    public void adicionarOpcao(String opcao) {
        opcoes.add(opcao);
    }

    public List<String> getOpcoes() {
        return new ArrayList<>(opcoes);
    }

    public void setRespostaCorreta(int indice) {
        if (indice >= 0 && indice < opcoes.size()) {
            this.indiceRespostaCorreta = indice;
        }
    }

    @Override
    public double pontuar(Resposta resposta) {
        try {
            int indiceResposta = Integer.parseInt(resposta.getValor());
            if (indiceResposta == indiceRespostaCorreta) {
                return 1.0; // Ponto completo por acertar
            }
        } catch (NumberFormatException e) {
            // Resposta inválida
        }
        return 0.0;
    }
}