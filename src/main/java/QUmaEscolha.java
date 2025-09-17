import java.util.ArrayList;
import java.util.List;

public class QUmaEscolha extends Questao {
    private List<Opcao> opcoes;
    private int indiceCorreto;

    public QUmaEscolha(String enunciado, double peso) {
        super(enunciado, peso);
        this.opcoes = new ArrayList<>();
    }

    public void adicionarOpcao(String texto, boolean correta) {
        Opcao opcao = new Opcao(texto, correta);
        opcoes.add(opcao);
        if (correta) {
            indiceCorreto = opcoes.size() - 1;
        }
    }

    @Override
    public double pontuar(Resposta resposta) {
        int respostaIndice = resposta.indiceSelecionado();
        return (respostaIndice == indiceCorreto) ? peso : 0.0;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public int getIndiceCorreto() {
        return indiceCorreto;
    }
}
