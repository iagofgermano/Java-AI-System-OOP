import java.util.*;

public class Quiz {
    private UUID id;
    private List<Questao> questoes;
    private int notaMinima;
    private Map<Aluno, List<TentativaQuiz>> tentativasPorAluno;

    public Quiz(int notaMinima) {
        this.id = UUID.randomUUID();
        this.questoes = new ArrayList<>();
        this.notaMinima = notaMinima;
        this.tentativasPorAluno = new HashMap<>();
    }

    public void adicionarQuestao(Questao q) {
        questoes.add(q);
    }

    public TentativaQuiz submeter(Aluno aluno, RespostaSheet respostas) {
        TentativaQuiz tentativa = new TentativaQuiz(aluno, this, respostas);
        tentativasPorAluno.computeIfAbsent(aluno, k -> new ArrayList<>()).add(tentativa);
        return tentativa;
    }

    public double calcularNota(RespostaSheet respostas) {
        double nota = 0.0;
        for (int i = 0; i < questoes.size(); i++) {
            Questao q = questoes.get(i);
            Resposta r = respostas.getResposta(i);
            if (r != null) {
                nota += q.pontuar(r);
            }
        }
        return nota;
    }

    public int getTentativasRestantes(Aluno aluno) {
        List<TentativaQuiz> tentativas = tentativasPorAluno.getOrDefault(aluno, new ArrayList<>());
        // exemplo: limite fixo de 3 tentativas
        return Math.max(0, 3 - tentativas.size());
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public List<Questao> getQuestoes() {
        return Collections.unmodifiableList(questoes);
    }

    public int getNotaMinima() {
        return notaMinima;
    }
}
