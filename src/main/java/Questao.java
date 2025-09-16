public abstract class Questao {
    protected String enunciado;
    protected double peso;

    public Questao(String enunciado, double peso) {
        this.enunciado = enunciado;
        this.peso = peso;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public double getPeso() {
        return peso;
    }

    public abstract double pontuar(Resposta resposta);
}
