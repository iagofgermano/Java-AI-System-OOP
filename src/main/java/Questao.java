public abstract class Questao {
    protected String enunciado;
    protected double peso;

    protected Questao(String enunciado, double peso) {
        this.enunciado = enunciado;
        this.peso = peso;
    }

    public abstract double pontuar(Resposta resposta);
}