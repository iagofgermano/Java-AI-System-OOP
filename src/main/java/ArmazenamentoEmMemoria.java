public class ArmazenamentoEmMemoria {
    private Map<UUID, Curso> cursos;
    private Map<UUID, Aluno> alunos;
    private Map<UUID, Quiz> quizzes;

    public ArmazenamentoEmMemoria() {
        this.cursos = new ConcurrentHashMap<>();
        this.alunos = new ConcurrentHashMap<>();
        this.quizzes = new ConcurrentHashMap<>();
    }

    public void obterCurso(UUID id) {
    }

    public void obterAluno(UUID id) {
    }

    public void obterQuiz(UUID id) {
    }

    public void salvarCurso(Curso curso) {
        cursos.put(curso.getId(), curso);
    }

    public void salvarAluno(Aluno aluno) {
        alunos.put(aluno.getId(), aluno);
    }

    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos.values());
    }
}
