import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QMultiplaSelecao extends Questao {
    private List<Opcao> opcoes;
    private Set<Integer> corretas;

    public QMultiplaSelecao(String enunciado, double peso) {
        super(enunciado, peso);
        this.opcoes = new ArrayList<>();
        this.corretas = new HashSet<>();
    }

    public void adicionarOpcao(String texto, boolean correta) {
        Opcao opcao = new Opcao(texto, correta);
        opcoes.add(opcao);
        if (correta) {
            corretas.add(opcoes.size() - 1);
        }
    }

    @Override
    public double pontuar(Resposta resposta) {
        Set<Integer> respostaIndices = resposta.getIndicesResposta();
        return respostaIndices.equals(corretas) ? peso : 0.0;
    }
}
