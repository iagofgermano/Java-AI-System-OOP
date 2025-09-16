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

        // Verifica se aluno e curso estão registrados no sistema (opcional)
        if (store.obterAluno(aluno.getId()) == null) {
            throw new IllegalArgumentException("Aluno não registrado no sistema.");
        }
        if (store.obterCurso(curso.getId()) == null) {
            throw new IllegalArgumentException("Curso não registrado no sistema.");
        }

        // Cria a inscrição
        Inscricao inscricao = new Inscricao(aluno, curso);

        // Retorna a inscrição criada
        return inscricao;
    }

    public Inscricao inscrever(UUID alunoId, UUID cursoId) {
        // Recupera aluno e curso do armazenamento
        Aluno aluno = store.obterAluno(alunoId);
        Curso curso = store.obterCurso(cursoId);

        // Valida se aluno e curso existem
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não encontrado com ID: " + alunoId);
        }
        if (curso == null) {
            throw new IllegalArgumentException("Curso não encontrado com ID: " + cursoId);
        }
        return new Inscricao(aluno, curso);
    }

    // Submete respostas de um quiz por um aluno
    public TentativaQuiz submeter(Aluno aluno, Quiz quiz, RespostaSheet respostas) {
        TentativaQuiz tentativa = quiz.submeter(aluno, respostas);
        return tentativa;
    }

    // Consulta o progresso do aluno em uma aula específica
    public Progresso consultarProgresso(Aluno aluno, Aula aula) {
        return aluno.obterProgresso(aula);
    }

    public void resetarProgresso(Aluno aluno, Curso curso) {
        // Validações
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

        // Reseta as tentativas de quiz para cada quiz nas aulas
        for (Aula aula : aulas) {
            if (aula.getQuiz() != null) {
                Quiz quiz = aula.getQuiz();
                quiz.limparTentativasDoAluno(aluno);
            }
        }

        // Remove as insígnias do usuário relacionadas a este curso
        // (isso depende da implementação específica)
        aluno.insignias.removeIf(insigniaDoUsuario ->
                insigniaDoUsuario.getInsignia().getId().equals(curso.getId()));
    }

    // Concede insígnias ao aluno se ele tiver concluído o curso
    public List<InsigniaDoUsuario> concederInsigniasSeElegivel(Aluno aluno, Curso curso) {
        List<InsigniaDoUsuario> concedidas = new ArrayList<>();

        // Verifica se o aluno completou o curso
        boolean cursoCompleto = true;
        for (Aula aula : curso.listarAulas()) {
            if (!aula.concluidaPor(aluno)) {
                cursoCompleto = false;
                break;
            }
        }

        if (cursoCompleto) {
            // Exemplo: concede todas as insígnias associadas ao curso
            for (Insignia insignia : curso.listarInsignias()) { // Supondo método getInsignias() em Curso
                if (insignia.conceder(aluno, curso)) {
                    InsigniaDoUsuario insigniaDoUsuario = new InsigniaDoUsuario(aluno, insignia, LocalDateTime.now());
                    aluno.adicionarInsignia(insigniaDoUsuario); // Método a ser adicionado em Aluno
                    concedidas.add(insigniaDoUsuario);
                }
            }
        }

        return concedidas;
    }
}