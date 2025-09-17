import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // === INFRAESTRUTURA ===
        ArmazenamentoEmMemoria store = new ArmazenamentoEmMemoria();
        GestorAcademico gestor = new GestorAcademico(store);

        // === USUÁRIOS ===
        Admin admin = new Admin("Admin Master", "admin@plataforma.com");
        Aluno aluno = new Aluno("João Silva", "joao@exemplo.com");

        store.salvarAluno(aluno);

        // === CURSO ===
        Curso curso = new Curso("Introdução à Programação", "Curso básico de lógica de programação.");
        curso.publicar();

        // === MÓDULO ===
        Modulo modulo1 = new Modulo(1, "Fundamentos");
        curso.adicionarModulo(modulo1);

        // === AULA ===
        Aula aula1 = new Aula(1, 30);
        aula1.adicionarBloco(new BlocoTexto(1, "Bem-vindo ao curso!"));
        aula1.adicionarBloco(new BlocoCodigo(2, "java", "System.out.println(\"Olá Mundo\");"));
        modulo1.adicionarAula(aula1);

        // === QUIZ ===
        Quiz quiz = new Quiz(7);
        QUmaEscolha questao1 = new QUmaEscolha("Qual é a saída do código apresentado?", 5.0);
        questao1.adicionarOpcao("Erro", false);
        questao1.adicionarOpcao("Olá Mundo", true);
        questao1.adicionarOpcao("Nada", false);
        quiz.adicionarQuestao(questao1);

        aula1.definirQuiz(quiz);

        store.salvarCurso(curso);

        // === INSCRIÇÃO ===
        Inscricao inscricao = gestor.inscrever(aluno, curso);

        // === SIMULAÇÃO DE RESPOSTA AO QUIZ ===
        RespostaSheet respostaSheet = new RespostaSheet();
        respostaSheet.adicionar(questao1, new Resposta("1")); // índice da opção correta

        TentativaQuiz tentativa = gestor.submeter(aluno, quiz, respostaSheet);
        System.out.println("Nota obtida: " + tentativa.getNota());
        System.out.println("Aprovado? " + tentativa.isAprovado());

        // === PROGRESSO ===
        Progresso progresso = gestor.consultarProgresso(aluno, aula1);
        progresso.marcarConcluido();
        System.out.println("Status da aula: " + progresso.getStatus());

        // === INSÍGNIA ===
        Insignia insignia = new Insignia("Primeira Aula Concluída", "Parabéns por concluir sua primeira aula!");
        insignia.conceder(aluno, curso); // Simula concessão

        // === PLAYGROUND ===
        TarefaPrompting tarefa = new TarefaPrompting();
        try {
            SessaoPlayground sessao = new SessaoPlayground(aluno, tarefa);
            sessao.iniciar();
            Parametros params = new Parametros().put("prompt", "Explique o que é uma variável.");
            Resultado resultado = sessao.executar(params);
            System.out.println("Resultado do Playground: " + resultado.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("=== Sistema testado com sucesso ===");
    }
}