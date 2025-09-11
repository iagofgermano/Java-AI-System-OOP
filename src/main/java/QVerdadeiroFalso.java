public class QVerdadeiroFalso extends Questao {
    private final boolean respostaCorreta;

    public QVerdadeiroFalso(String enunciado, double peso, boolean respostaCorreta) {
        super(enunciado, peso);
        this.respostaCorreta = respostaCorreta;
    }

    public boolean getRespostaCorreta() {
        return respostaCorreta;
    }

    @Override
    public double pontuar(Resposta resposta) {
        try {
            String valor = resposta.getValor().toLowerCase().trim();
            boolean respostaAluno;

            if (valor.equals("verdadeiro") || valor.equals("true") || valor.equals("v")) {
                respostaAluno = true;
            } else if (valor.equals("falso") || valor.equals("false") || valor.equals("f")) {
                respostaAluno = false;
            } else {
                throw new Exception("Valor inválido passado. Resposta deve ser V ou F"); // Formato inválido
            }

            return respostaAluno == respostaCorreta ? super.peso : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

}