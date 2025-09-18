import java.util.Optional;

public class SessaoPlayground implements AutoCloseable {
    private Aluno aluno;
    private TarefaIA tarefa;
    private Resultado ultimoResultado;
    private boolean ativa;

    public SessaoPlayground(Aluno aluno, TarefaIA tarefa) {
        this.aluno = aluno;
        this.tarefa = tarefa;
        this.ultimoResultado = null;
        this.ativa = false;
    }

    public void iniciar() {
        if (!ativa) {
            this.ativa = true;
            System.out.println("Sessão iniciada para o aluno: " + aluno.getNome());
        } else {
            System.out.println("A sessão já está ativa.");
        }
    }

    public Resultado executar(Parametros parametros) {
        if (!ativa) {
            throw new IllegalStateException("A sessão não foi iniciada.");
        }

        this.ultimoResultado = tarefa.executar(parametros);
        System.out.println("Execução concluída. Resultado: " + ultimoResultado.toString());
        return this.ultimoResultado;
    }

    @Override
    public void close() {
        if (ativa) {
            this.ativa = false;
            System.out.println("Sessão encerrada para o aluno: " + aluno.getNome());
        }
    }

    public Aluno getAluno() {
        return aluno;
    }
}