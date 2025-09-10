import java.util.*;

public class GestorAcademico {
    private List<Curso> cursos;
    private List<Aluno> alunos;
    private List<Insignia> insignias;
    private Map<Aluno, Map<Aula, Progresso>> progressoPorAluno;
    private List<TentativaQuiz> tentativas;

    public GestorAcademico() {
        this.cursos = new ArrayList<>();
        this.alunos = new ArrayList<>();
        this.insignias = new ArrayList<>();
        this.progressoPorAluno = new HashMap<>();
        this.tentativas = new ArrayList<>();
    }

    // Métodos de gerenciamento de cursos
    public void adicionarCurso(Curso curso) {
        cursos.add(curso);
    }

    public List<Curso> getCursos() {
        return new ArrayList<>(cursos);
    }

    // Métodos de gerenciamento de alunos
    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
        progressoPorAluno.put(aluno, new HashMap<>());
    }

    public List<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    // Métodos de inscrição
    public void inscreverAluno(Aluno aluno, Curso curso) {
        Inscricao inscricao = new Inscricao(aluno, curso);
        aluno.adicionarInscricao(inscricao);

        // Inicializar progresso para todas as aulas do curso
        for (Modulo modulo : curso.getModulos()) {
            for (Aula aula : modulo.getAulas()) {
                Progresso progresso = new Progresso(aluno, aula);
                progressoPorAluno.get(aluno).put(aula, progresso);
            }
        }
    }

    // Métodos de progresso
    public void atualizarProgresso(Aluno aluno, Aula aula, double percentual) {
        Map<Aula, Progresso> progressos = progressoPorAluno.get(aluno);
        if (progressos != null) {
            Progresso progresso = progressos.get(aula);
            if (progresso != null) {
                progresso.setPercentualConclusao(percentual);
            }
        }
    }

    public Progresso getProgresso(Aluno aluno, Aula aula) {
        Map<Aula, Progresso> progressos = progressoPorAluno.get(aluno);
        if (progressos != null) {
            return progressos.get(aula);
        }
        return null;
    }

    // Métodos de avaliação
    public void registrarTentativa(TentativaQuiz tentativa) {
        tentativas.add(tentativa);
    }

    public List<TentativaQuiz> getTentativasPorAluno(Aluno aluno) {
        List<TentativaQuiz> tentativasAluno = new ArrayList<>();
        for (TentativaQuiz tentativa : tentativas) {
            if (tentativa.getAluno().equals(aluno)) {
                tentativasAluno.add(tentativa);
            }
        }
        return tentativasAluno;
    }

    // Métodos de insignias
    public void adicionarInsignia(Insignia insignia) {
        insignias.add(insignia);
    }

    public void concederInsignia(Aluno aluno, Insignia insignia) {
        InsigniaDoUsuario insigniaDoUsuario = new InsigniaDoUsuario(insignia, aluno);
        aluno.adicionarInsignia(insigniaDoUsuario);
    }

    public List<Insignia> getInsignias() {
        return new ArrayList<>(insignias);
    }
}