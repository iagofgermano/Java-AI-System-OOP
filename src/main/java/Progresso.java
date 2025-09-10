import java.time.LocalDateTime;

public class Progresso {
    private Aluno aluno;
    private Aula aula;
    private double percentualConclusao;
    private LocalDateTime dataUltimaAtividade;
    private boolean concluido;

    public Progresso(Aluno aluno, Aula aula) {
        this.aluno = aluno;
        this.aula = aula;
        this.percentualConclusao = 0.0;
        this.dataUltimaAtividade = LocalDateTime.now();
        this.concluido = false;
    }

    // Getters e Setters
    public Aluno getAluno() { return aluno; }
    public Aula getAula() { return aula; }
    public double getPercentualConclusao() { return percentualConclusao; }
    public LocalDateTime getDataUltimaAtividade() { return dataUltimaAtividade; }
    public boolean isConcluido() { return concluido; }

    public void setPercentualConclusao(double percentualConclusao) {
        this.percentualConclusao = percentualConclusao;
        this.dataUltimaAtividade = LocalDateTime.now();
        if (percentualConclusao >= 100.0) {
            this.concluido = true;
        }
    }
}