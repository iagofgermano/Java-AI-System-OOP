import java.util.Optional;

public class SessaoPlayground implements AutoCloseable {
    private Aluno aluno;
    private TarefaIA tarefa;
    private Resultado ultimoResultado;
    private boolean ativa;

    // Construtor
    public SessaoPlayground(Aluno aluno, TarefaIA tarefa) {
        this.aluno = aluno;
        this.tarefa = tarefa;
        this.ultimoResultado = null;
        this.ativa = false;
    }

    // Método para iniciar a sessão
    public void iniciar() {
        if (!ativa) {
            this.ativa = true;
            System.out.println("Sessão iniciada para o aluno: " + aluno.getNome());
        } else {
            System.out.println("A sessão já está ativa.");
        }
    }

    // Método para executar a tarefa com os parâmetros fornecidos
    public Resultado executar(Parametros parametros) {
        if (!ativa) {
            throw new IllegalStateException("A sessão não foi iniciada.");
        }

        this.ultimoResultado = tarefa.executar(parametros);
        System.out.println("Execução concluída. Resultado: " + ultimoResultado.toString());
        return this.ultimoResultado;
    }

    // Método da interface AutoCloseable para fechar a sessão
    @Override
    public void close() {
        if (ativa) {
            this.ativa = false;
            System.out.println("Sessão encerrada para o aluno: " + aluno.getNome());
        }
    }

    // Getters (opcionais, dependendo da necessidade)
    public Aluno getAluno() {
        return aluno;
    }

    public TarefaIA getTarefa() {
        return tarefa;
    }

    public Optional<Resultado> getUltimoResultado() {
        return Optional.ofNullable(ultimoResultado);
    }

    public boolean isAtiva() {
        return ativa;
    }
}