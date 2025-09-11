import java.time.LocalDateTime;
import java.util.UUID;

public class TentativaQuiz {
    private UUID id;
    private Quiz quiz;
    private Aluno aluno;
    private RespostaSheet respostaSheet;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private double nota;

    public TentativaQuiz(Aluno aluno, Quiz quiz) {
        this.id = quiz.getId();
        this.quiz = quiz;
        this.aluno = aluno;
        this.respostaSheet = new RespostaSheet();
        this.dataInicio = LocalDateTime.now();
    }

    public void finalizarTentativa() {
        this.dataFim = LocalDateTime.now();
        this.nota = quiz.pontuar(respostaSheet);
    }

    // Getters e Setters
    public Quiz getQuiz() { return quiz; }
    public Aluno getAluno() { return aluno; }
    public RespostaSheet getRespostaSheet() { return respostaSheet; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public double getNota() { return nota; }

    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public void setNota(double nota) { this.nota = nota; }
}