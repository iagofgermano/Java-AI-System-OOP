import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ArmazenamentoEmMemoria {

    // Mapas para armazenar os dados em memória
    private final Map<UUID, Curso> cursos = new ConcurrentHashMap<>();
    private final Map<UUID, Aluno> alunos = new ConcurrentHashMap<>();
    private final Map<UUID, Quiz> quizzes = new ConcurrentHashMap<>();

    // Construtor
    public ArmazenamentoEmMemoria() {
        // Inicialização, se necessário
    }

    // Métodos para Curso
    public Curso obterCurso(UUID id) {
        return cursos.get(id);
    }

    public void salvarCurso(Curso curso) {
        cursos.put(curso.getId(), curso);
    }

    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos.values());
    }

    // Métodos para Aluno
    public Aluno obterAluno(UUID id) {
        return alunos.get(id);
    }

    public void salvarAluno(Aluno aluno) {
        alunos.put(aluno.getId(), aluno);
    }

    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunos.values());
    }

    // Métodos para Quiz
    public Quiz obterQuiz(UUID id) {
        return quizzes.get(id);
    }

    public void salvarQuiz(Quiz quiz) {
        quizzes.put(quiz.getId(), quiz);
    }

    public List<Quiz> listarQuizzes() {
        return new ArrayList<>(quizzes.values());
    }
}