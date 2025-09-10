public class QVerdadeiroFalso extends Questao {
    private boolean respostaCorreta;

    public QVerdadeiroFalso(String enunciado, boolean respostaCorreta) {
        super(enunciado);
        this.respostaCorreta = respostaCorreta;
    }

    public boolean getRespostaCorreta() {
        return respostaCorreta;
    }

    @Override
    public double pontuar(Resposta resposta) {
        String valor = resposta.getValor().toLowerCase().trim();
        boolean respostaAluno;

        if (valor.equals("verdadeiro") || valor.equals("true") || valor.equals("v")) {
            respostaAluno = true;
        } else if (valor.equals("falso") || valor.equals("false") || valor.equals("f")) {
            respostaAluno = false;
        } else {
            return 0.0; // Formato inválido
        }

        return respostaAluno == respostaCorreta ? 1.0 : 0.0;
    }
}