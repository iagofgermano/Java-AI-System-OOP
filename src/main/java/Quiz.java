import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private UUID id;
    private double notaMinima;
    private List<Questao> questoes;

    // Construtor
    public Quiz(double notaMinima) {
        this.notaMinima = notaMinima;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public List<Questao> getQuestoes() {
        return new ArrayList<>(questoes); // Retorna cópia para encapsulamento
    }

    // Método para adicionar uma questão
    public void adicionarQuestao(Questao questao) {
        if (questao != null) {
            questoes.add(questao);
        }
    }

    // Método para calcular a pontuação com base nas respostas fornecidas
    public double pontuar(RespostaSheet respostaSheet) {
        if (respostaSheet == null) {
            return 0.0;
        }

        double notaTotal = 0.0;

        for (Questao questao : questoes) {
            // Busca a resposta associada a essa questão
            Resposta resposta = respostaSheet.getResposta(questao);

            if (resposta != null) {
                // Chama o método pontuar da própria questão (polimorfismo)
                notaTotal += questao.pontuar(resposta);
            }
        }

        return notaTotal;
    }
}