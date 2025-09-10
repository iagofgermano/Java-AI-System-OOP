import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String titulo;
    private String descricao;
    private List<Questao> questoes;

    // Construtor
    public Quiz(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.questoes = new ArrayList<>();
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
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