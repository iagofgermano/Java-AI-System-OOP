public class QUmaEscolha extends Questao {
    private String respostaCorreta;

    public QUmaEscolha(String enunciado, String respostaCorreta) {
        super(enunciado);
        this.respostaCorreta = respostaCorreta.toLowerCase().trim();
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    @Override
    public double pontuar(Resposta resposta) {
        String respostaAluno = resposta.getValor().toLowerCase().trim();
        return respostaAluno.equals(respostaCorreta) ? 1.0 : 0.0;
    }
}