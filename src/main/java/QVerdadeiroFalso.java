public class QVerdadeiroFalso extends Questao {
    private boolean correto;

    public QVerdadeiroFalso(String enunciado, double peso, boolean correto) {
        super(enunciado, peso);
        this.correto = correto;
    }

    @Override
    public double pontuar(Resposta resposta) {
        return (resposta.isVerdadeiroFalso() == correto) ? peso : 0.0;
    }
}
