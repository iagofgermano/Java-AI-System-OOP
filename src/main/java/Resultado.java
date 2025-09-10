import java.time.LocalDateTime;

public class Resultado {
    private TentativaQuiz tentativa;
    private double nota;
    private LocalDateTime dataAvaliacao;

    public Resultado(TentativaQuiz tentativa) {
        this.tentativa = tentativa;
        this.nota = tentativa.getNota();
        this.dataAvaliacao = LocalDateTime.now();
    }

    // Getters
    public TentativaQuiz getTentativa() { return tentativa; }
    public double getNota() { return nota; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }

    @Override
    public String toString() {
        return String.format("Resultado: Nota=%.2f, Data=%s", nota, dataAvaliacao);
    }
}