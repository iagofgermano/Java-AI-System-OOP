import java.util.*;

public class Aula {
    private UUID id;
    private int ordem;
    private int duracaoMin;
    private List<BlocoConteudo> blocos;
    private Quiz quiz;

    public Aula(int ordem, int duracaoMin) {
        this.id = UUID.randomUUID();
        this.ordem = ordem;
        this.duracaoMin = duracaoMin;
        this.blocos = new ArrayList<>();
        this.quiz = null;
    }

    public Aula(UUID id, int ordem, int duracaoMin) {
        this.id = id;
        this.ordem = ordem;
        this.duracaoMin = duracaoMin;
        this.blocos = new ArrayList<>();
        this.quiz = null;
    }

    public void adicionarBloco(BlocoConteudo b) {
        blocos.add(b);
    }

    public void adicionarBloco(String texto) {
        blocos.add(new BlocoTexto(1, texto));
    }

    public void definirQuiz(Quiz q) {
        this.quiz = q;
    }

    public boolean concluidaPor(Aluno a) {
        return a.concluiuAula(this);
    }

    // Getters
    public UUID getId() { return id; }
    public int getOrdem() { return ordem; }
    public int getDuracaoMin() { return duracaoMin; }
    public List<BlocoConteudo> getBlocos() { return new ArrayList<>(blocos); }
    public Quiz getQuiz() { return quiz; }
}
