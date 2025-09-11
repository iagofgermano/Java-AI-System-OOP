import java.util.ArrayList;
import java.util.List;

public class QMultiplaSelecao extends Questao {
    private final List<String> opcoes;
    private int indiceRespostaCorreta;

    public QMultiplaSelecao(String enunciado, double peso) {
        super(enunciado, peso);
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
            int indiceResposta = Integer.parseInt(resposta.getValor()) - 1;
            if (indiceResposta == indiceRespostaCorreta) {
                return super.peso; // Ponto completo por acertar
            }
        } catch (NumberFormatException e) {
            System.err.println("Alternativa inválida.");
            return 0.0;
        }
        return 0.0;
    }
}