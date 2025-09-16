import java.util.*;
import java.util.UUID;
public class GestorAcademico {

    private ArmazenamentoEmMemoria store;

    public GestorAcademico(ArmazenamentoEmMemoria store) {
        this.store = store;
    }

    // Inscreve um aluno em um curso usando objetos
    public Inscricao inscrever(Aluno aluno, Curso curso) {
        // Verifica se já existe inscrição ativa
        for (Inscricao i : aluno.getInscricoes()) {
            if (i.getCurso().getId().equals(curso.getId()) &&
                    (i.getStatus() == StatusInscricao.ACTIVE || i.getStatus() == StatusInscricao.COMPLETED)) {
                return i; // Já inscrito
            }
        }

        Inscricao novaInscricao = new Inscricao(aluno, curso);
        aluno.adicionarInscricao(novaInscricao); // Método que deve existir em Aluno
        return novaInscricao;
    }

    // Inscreve um aluno em um curso usando UUIDs
    public Inscricao inscrever(UUID alunoId, UUID cursoId) {
        Aluno aluno = store.obterAluno(alunoId);
        Curso curso = store.obterCurso(cursoId);

        if (aluno == null || curso == null) {
            throw new IllegalArgumentException("Aluno ou Curso não encontrado.");
        }

        return inscrever(aluno, curso);
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

    // Reseta o progresso do aluno em todas as aulas de um curso
    public void resetarProgresso(Aluno aluno, Curso curso) {
        for (Aula aula : curso.listarAulas()) {
            Progresso progresso = aluno.obterProgresso(aula);
            if (progresso != null) {
                progresso.reset(); // Método a ser implementado em Progresso
            }
        }
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
            for (Insignia insignia : curso.getInsignias()) { // Supondo método getInsignias() em Curso
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