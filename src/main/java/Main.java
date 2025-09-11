import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Aprendizagem Online ===\n");

        // 1. Criar gestor acadêmico
        GestorAcademico gestor = new GestorAcademico();

        // 2. Criar usuários
        Aluno aluno1 = new Aluno("Maria Silva", "maria@email.com", "123456", "2023001");
        Aluno aluno2 = new Aluno("Pedro Santos", "pedro@email.com", "123456", "2023002");
        Admin admin = new Admin("Admin Sistema", "admin@sistema.com", "admin123", "MASTER");

        gestor.adicionarAluno(aluno1);
        gestor.adicionarAluno(aluno2);

        System.out.println("✓ Usuários criados:");
        System.out.println("  - " + aluno1.getNome() + " (" + aluno1.getMatricula() + ")");
        System.out.println("  - " + aluno2.getNome() + " (" + aluno2.getMatricula() + ")");
        System.out.println("  - " + admin.getNome() + " (Administrador)\n");

        // 3. Criar curso com conteúdo
        Curso cursoPOO = new Curso("Programação Orientada a Objetos", "Curso completo sobre POO em Java");

        // Criar módulo
        Modulo moduloBasico = new Modulo("Conceitos Básicos", "Introdução aos fundamentos de POO", 60);

        // Criar aulas
        Aula aulaClasses = new Aula("Classes e Objetos", "Entendendo o que são classes e objetos");
        Aula aulaHeranca = new Aula("Herança", "Como utilizar herança em Java");
        Aula aulaPolimorfismo = new Aula("Polimorfismo", "Conceito e aplicação de polimorfismo");

        // Adicionar blocos de conteúdo às aulas
        aulaClasses.adicionarBloco(new BlocoTexto("Introdução", "Classes são modelos para criar objetos..."));
        aulaClasses.adicionarBloco(new BlocoCodigo("Exemplo de Classe", "public class Pessoa { ... }", "java"));

        aulaHeranca.adicionarBloco(new BlocoTexto("O que é herança?", "Herança permite reutilizar código..."));
        aulaHeranca.adicionarBloco(new BlocoImagem("Diagrama de Herança", "Exemplo visual", "diagrama.png"));

        aulaPolimorfismo.adicionarBloco(new BlocoTexto("Polimorfismo", "Permite que objetos de diferentes classes..."));

        // Montar estrutura do curso
        moduloBasico.adicionarAula(aulaClasses);
        moduloBasico.adicionarAula(aulaHeranca);
        moduloBasico.adicionarAula(aulaPolimorfismo);
        cursoPOO.adicionarModulo(moduloBasico);

        gestor.adicionarCurso(cursoPOO);

        System.out.println("✓ Curso criado:");
        System.out.println("  - " + cursoPOO.getTitulo());
        System.out.println("  - Módulo: " + moduloBasico.getTitulo());
        System.out.println("  - Aulas: " + moduloBasico.getAulas().size() + " aulas\n");

        // 4. Inscrever alunos no curso
        gestor.inscreverAluno(aluno1, cursoPOO);
        gestor.inscreverAluno(aluno2, cursoPOO);

        System.out.println("✓ Alunos inscritos no curso:");
        for (Aluno aluno : gestor.getAlunos()) {
            System.out.println("  - " + aluno.getNome());
        }
        System.out.println();

        // 5. Atualizar progresso dos alunos
        gestor.atualizarProgresso(aluno1, aulaClasses, 100.0);
        gestor.atualizarProgresso(aluno1, aulaHeranca, 75.0);
        gestor.atualizarProgresso(aluno2, aulaClasses, 50.0);

        System.out.println("✓ Progresso atualizado:");
        System.out.println("  - " + aluno1.getNome() + ":");
        System.out.println("    * " + aulaClasses.getTitulo() + " - 100%");
        System.out.println("    * " + aulaHeranca.getTitulo() + " - 75%");
        System.out.println("  - " + aluno2.getNome() + ":");
        System.out.println("    * " + aulaClasses.getTitulo() + " - 50%\n");

        // 6. Criar quiz para avaliar conhecimento
        Quiz quizPOO = new Quiz(1.0);

        // Criar questões
        QMultiplaSelecao q1 = new QMultiplaSelecao("Qual conceito permite que uma classe herde características de outra?", 1.0);
        q1.adicionarOpcao("1. Encapsulamento");
        q1.adicionarOpcao("2. Herança");
        q1.adicionarOpcao("3. Polimorfismo");
        q1.setRespostaCorreta(1);

        QVerdadeiroFalso q2 = new QVerdadeiroFalso("(V ou F): Em Java, todas as classes herdam de Object.", 1.0, true);

        QUmaEscolha q3 = new QUmaEscolha("Qual palavra-chave é usada para herdar uma classe em Java?", 2.0, "extends");

        // Adicionar questões ao quiz
        quizPOO.adicionarQuestao(q1);
        quizPOO.adicionarQuestao(q2);
        quizPOO.adicionarQuestao(q3);

        System.out.println("✓ Quiz criado:");
        System.out.println("  - " + quizPOO.getQuestoes().size() + " questões\n");

        // 7. Alunos realizam o quiz
        System.out.println("✓ Alunos realizando o quiz...\n");

        // Aluno 1 - respostas corretas
        TentativaQuiz tentativa1 = new TentativaQuiz(quizPOO, aluno1);
        tentativa1.getRespostaSheet().adicionarResposta(q1, new Resposta("1")); // Herança
        tentativa1.getRespostaSheet().adicionarResposta(q2, new Resposta("verdadeiro"));
        tentativa1.getRespostaSheet().adicionarResposta(q3, new Resposta("extends"));
        tentativa1.finalizarTentativa();
        gestor.registrarTentativa(tentativa1);

        // Aluno 2 - respostas com erros
        TentativaQuiz tentativa2 = new TentativaQuiz(quizPOO, aluno2);
        tentativa2.getRespostaSheet().adicionarResposta(q1, new Resposta("0")); // Errado
        tentativa2.getRespostaSheet().adicionarResposta(q2, new Resposta("falso")); // Errado
        tentativa2.getRespostaSheet().adicionarResposta(q3, new Resposta("implements")); // Errado
        tentativa2.finalizarTentativa();
        gestor.registrarTentativa(tentativa2);

        // 8. Gerar resultados
        Resultado resultado1 = new Resultado(tentativa1);
        Resultado resultado2 = new Resultado(tentativa2);

        System.out.println("✓ Resultados do Quiz:");
        System.out.println("  - " + aluno1.getNome() + ": " + String.format("%.1f", resultado1.getNota()) + "/3.0");
        System.out.println("  - " + aluno2.getNome() + ": " + String.format("%.1f", resultado2.getNota()) + "/3.0\n");

        // 9. Criar e conceder insignias
        Insignia insigniaExcelencia = new Insignia("Excelência em POO",
                "Concedida a alunos que obtiveram nota máxima no quiz",
                "Nota 3.0 no Quiz Final");

        Insignia insigniaParticipacao = new Insignia("Participação Ativa",
                "Concedida a alunos que completaram mais de 50% do curso",
                "Progresso > 50% em pelo menos uma aula");

        gestor.adicionarInsignia(insigniaExcelencia);
        gestor.adicionarInsignia(insigniaParticipacao);

        // Conceder insignias baseadas no desempenho
        if (resultado1.getNota() == 3.0) {
            gestor.concederInsignia(aluno1, insigniaExcelencia);
            System.out.println("✓ Insignia concedida: " + aluno1.getNome() + " recebeu '" + insigniaExcelencia.getNome() + "'");
        }

        if (gestor.getProgresso(aluno1, aulaClasses).getPercentualConclusao() > 50.0) {
            gestor.concederInsignia(aluno1, insigniaParticipacao);
            System.out.println("✓ Insignia concedida: " + aluno1.getNome() + " recebeu '" + insigniaParticipacao.getNome() + "'");
        }

        if (gestor.getProgresso(aluno2, aulaClasses).getPercentualConclusao() > 50.0) {
            gestor.concederInsignia(aluno2, insigniaParticipacao);
            System.out.println("✓ Insignia concedida: " + aluno2.getNome() + " recebeu '" + insigniaParticipacao.getNome() + "'");
        }
        System.out.println();

        // 10. Relatório final
        System.out.println("=== RELATÓRIO FINAL ===");
        System.out.println("Curso: " + cursoPOO.getTitulo());
        System.out.println("Número de alunos: " + gestor.getAlunos().size());
        System.out.println();

        for (Aluno aluno : gestor.getAlunos()) {
            System.out.println("Aluno: " + aluno.getNome());

            // Mostrar progresso
            System.out.println("  Progresso:");
            for (Modulo modulo : cursoPOO.getModulos()) {
                for (Aula aula : modulo.getAulas()) {
                    Progresso progresso = gestor.getProgresso(aluno, aula);
                    if (progresso != null) {
                        System.out.println("    - " + aula.getTitulo() + ": " +
                                String.format("%.0f", progresso.getPercentualConclusao()) + "%");
                    }
                }
            }

            // Mostrar tentativas de quiz
            List<TentativaQuiz> tentativas = gestor.getTentativasPorAluno(aluno);
            if (!tentativas.isEmpty()) {
                System.out.println("  Tentativas de Quiz:");
                for (TentativaQuiz tentativa : tentativas) {
                    System.out.println("    - " + tentativa.getQuiz() +
                            ": " + String.format("%.1f", tentativa.getNota()) + "/3.0");
                }
            }

            // Mostrar insignias
            if (!aluno.getInsignias().isEmpty()) {
                System.out.println("  Insignias conquistadas:");
                for (InsigniaDoUsuario insignia : aluno.getInsignias()) {
                    System.out.println("    - " + insignia.getInsignia().getNome());
                }
            }

            System.out.println();
        }

        System.out.println("=== FIM DA DEMONSTRAÇÃO ===");
    }
}