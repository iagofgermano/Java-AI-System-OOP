public class QVerdadeiroFalso extends Questao {
    private boolean correto;

    public QVerdadeiroFalso(String enunciado, double peso, boolean correto) {
        super(enunciado, peso);
        this.correto = correto;
    }

    @Override
    public double pontuar(Resposta resposta) {
        String valor = resposta.getValor().trim().toLowerCase();
        boolean respostaBool = valor.equals("true") || valor.equals("verdadeiro") || valor.equals("v");
        return (respostaBool == correto) ? peso : 0.0;
    }

    public boolean isCorreto() {
        return correto;
    }
}
