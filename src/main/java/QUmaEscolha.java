import java.util.List;

public class QUmaEscolha extends Questao {
    private final String respostaCorreta;

    public QUmaEscolha(String enunciado, double peso, String respostaCorreta) {
        super(enunciado, peso);
        this.respostaCorreta = respostaCorreta.toLowerCase().trim();
    }

    public String getEnunciado() {
        return super.enunciado;
    }

    @Override
    public double pontuar(Resposta resposta) {
        String respostaAluno = resposta.getValor().toLowerCase().trim();
        return respostaAluno.equals(respostaCorreta) ? super.peso : 0.0;
    }
}