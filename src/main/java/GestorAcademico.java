import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;
public class GestorAcademico {

    private ArmazenamentoEmMemoria store;

    public GestorAcademico(ArmazenamentoEmMemoria store) {
        this.store = store;
    }

    public Inscricao inscrever(Aluno aluno, Curso curso) {
        if (aluno == null || curso == null) {
            throw new IllegalArgumentException("Aluno e Curso não podem ser nulos.");
        }

        if (store.obterAluno(aluno.getId()) == null) {
            throw new IllegalArgumentException("Aluno não registrado no sistema.");
        }
        if (store.obterCurso(curso.getId()) == null) {
            throw new IllegalArgumentException("Curso não registrado no sistema.");
        }
        Inscricao inscricao = new Inscricao(aluno, curso);
        store.salvarInscricao(inscricao);
        return inscricao;
    }

    public Inscricao inscrever(UUID alunoId, UUID cursoId) {
        Aluno aluno = store.obterAluno(alunoId);
        Curso curso = store.obterCurso(cursoId);

        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId);
        }
        if (curso == null) {
            throw new IllegalArgumentException("Curso não encontrado com ID: " + cursoId);
        }
        return new Inscricao(aluno, curso);
    }

    public TentativaQuiz submeter(Aluno aluno, Quiz quiz, RespostaSheet respostas) {
        TentativaQuiz tentativa = quiz.submeter(aluno, respostas);
        return tentativa;
    }

    public Progresso consultarProgresso(Aluno aluno, Aula aula) {
        return aluno.obterProgresso(aula);
    }

    public void resetarProgresso(Aluno aluno, Curso curso) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (curso == null) {
            throw new IllegalArgumentException("Curso não pode ser nulo");
        }

        List<Aula> aulas = curso.listarAulas();

        for (Aula aula : aulas) {
            aluno.progressoPorAula.remove(aula.getId());
        }

        for (Aula aula : aulas) {
            if (aula.getQuiz() != null) {
                Quiz quiz = aula.getQuiz();
                quiz.limparTentativasDoAluno(aluno);
            }
        }

        aluno.insignias.removeIf(insigniaDoUsuario ->
                insigniaDoUsuario.getInsignia().getId().equals(curso.getId()));
    }

    public List<InsigniaDoUsuario> concederInsigniasSeElegivel(Aluno aluno, Curso curso) {
        List<InsigniaDoUsuario> concedidas = new ArrayList<>();

        boolean cursoCompleto = true;
        for (Aula aula : curso.listarAulas()) {
            if (!aula.concluidaPor(aluno)) {
                cursoCompleto = false;
                break;
            }
        }

        if (cursoCompleto) {
            for (Insignia insignia : curso.listarInsignias()) {
                if (insignia.conceder(aluno, curso)) {
                    InsigniaDoUsuario insigniaDoUsuario = new InsigniaDoUsuario(aluno, insignia, LocalDateTime.now());
                    aluno.adicionarInsignia(insigniaDoUsuario);
                    concedidas.add(insigniaDoUsuario);
                }
            }
        }

        return concedidas;
    }
}