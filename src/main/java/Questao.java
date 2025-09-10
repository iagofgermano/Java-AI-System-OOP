public abstract class Questao {
    protected String enunciado;

    public Questao(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getEnunciado() {
        return enunciado;
    }

    // Método abstrato para pontuação
    public abstract double pontuar(Resposta resposta);
}