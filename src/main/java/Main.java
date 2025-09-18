import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArmazenamentoEmMemoria armazenamento = new ArmazenamentoEmMemoria();
    private static GestorAcademico gestor = new GestorAcademico(armazenamento);
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        CarregadorDeDados carregador =  new CarregadorDeDados();

        System.out.println("🎓 Bem-vindo ao Sistema de ensino de IA CLI 🎓");
        while (true) {
            if (usuarioLogado == null) {
                menuLogin();
            } else if (usuarioLogado instanceof Aluno) {
                menuAluno((Aluno) usuarioLogado);
            } else if (usuarioLogado instanceof Admin) {
                menuAdmin((Admin) usuarioLogado);
            }
        }
    }

    // ==================== LOGIN ====================
    private static void menuLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.println("1. Entrar como Aluno");
        System.out.println("2. Entrar como Admin");
        System.out.println("3. Registrar novo Aluno");
        System.out.println("4. Registrar novo Admin");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1 -> loginAluno();
            case 2 -> loginAdmin();
            case 3 -> registrarAluno();
            case 4 -> registrarAdmin();
            case 0 -> {
                System.out.println("Até logo!");
                System.exit(0);
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void loginAluno() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Optional<Aluno> alunoOpt = armazenamento.getAlunos().values().stream()
                .filter(a -> a.email.equals(email))
                .findFirst();

        if (alunoOpt.isPresent() && alunoOpt.get().autenticar(email, senha)) {
            usuarioLogado = alunoOpt.get();
            System.out.println("Login bem-sucedido como Aluno: " + usuarioLogado.nome);
        } else {
            System.out.println("Email ou senha incorretos.");
        }
    }

    private static void loginAdmin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Optional<Admin> adminOpt = armazenamento.getAdmins().values().stream()
                .filter(a -> a instanceof Admin && a.email.equals(email))
                .map(a -> (Admin) a)
                .findFirst();

        if (adminOpt.isPresent() && adminOpt.get().autenticar(email, senha)) {
            usuarioLogado = adminOpt.get();
            System.out.println("✅ Login bem-sucedido como Admin: " + usuarioLogado.nome);
        } else {
            System.out.println("❌ Email ou senha incorretos.");
        }
    }

    private static void registrarAluno() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Aluno aluno = new Aluno(nome, email, senha);
        armazenamento.salvarAluno(aluno);
        System.out.println("✅ Aluno registrado com sucesso!");
    }

    private static void registrarAdmin() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Admin admin = new Admin(nome, email, senha);

        armazenamento.salvarAdmin(admin);
        System.out.println("✅ Admin registrado com sucesso!");
    }

    // ==================== MENU ALUNO ====================
    private static void menuAluno(Aluno aluno) {
        System.out.println("\n--- MENU ALUNO: " + aluno.nome + " ---");
        System.out.println("1. Listar Cursos Disponíveis");
        System.out.println("2. Ver Minhas Inscrições");
        System.out.println("3. Iniciar/Continuar Trilha");
        System.out.println("4. Ver Progresso");
        System.out.println("5. Ver Insignias");
        System.out.println("6. Abrir Playground");
        System.out.println("7. Sair da Conta");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1 -> listarCursos();
            case 2 -> verInscricoes(aluno);
            case 3 -> iniciarTrilha(aluno);
            case 4 -> verProgresso(aluno);
            case 5 -> verInsignias(aluno);
            case 6 -> abrirPlayground(aluno);
            case 7 -> {
                aluno.sair();
                usuarioLogado = null;
                System.out.println("✅ Logout realizado.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void listarCursos() {
        List<Curso> cursos = armazenamento.listarCursos().stream()
                .filter(c -> c.isPublicado())
                .collect(Collectors.toList());

        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso disponível.");
            return;
        }

        System.out.println("\n📚 Cursos Disponíveis:");
        for (int i = 0; i < cursos.size(); i++) {
            Curso c = cursos.get(i);
            System.out.printf("%d. %s - %s\n", i + 1, c.getTitulo(), c.getDescricao());
        }

        System.out.print("Digite o número do curso para se inscrever (0 para cancelar): ");
        int escolha = lerInteiro();
        if (escolha > 0 && escolha <= cursos.size()) {
            if (usuarioLogado instanceof Aluno aluno) {
                Curso curso = cursos.get(escolha - 1);
                gestor.inscrever(aluno, curso);
                System.out.println("✅ Inscrição realizada em: " + curso.getTitulo());
            }
        }
    }

    private static void verInscricoes(Aluno aluno) {
        List<Inscricao> inscricoes = armazenamento.obterInscricoesPorAluno(aluno);

        if (inscricoes.isEmpty()) {
            System.out.println("Você não está inscrito em nenhum curso.");
            return;
        }

        System.out.println("\n📝 Suas Inscrições:");
        for (int i = 0; i < inscricoes.size(); i++) {
            Inscricao inc = inscricoes.get(i);
            System.out.printf("%d. %s [%s]\n", i + 1, inc.getCurso().getTitulo(), inc.getStatus());
        }
    }

    private static void iniciarTrilha(Aluno aluno) {
        // Simulando acesso ao campo privado (em produção, use métodos getters)
        try {
            java.lang.reflect.Field field = Aluno.class.getDeclaredField("inscricoes");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Inscricao> inscricoes = (List<Inscricao>) field.get(aluno);

            if (inscricoes.isEmpty()) {
                System.out.println("Você não está inscrito em nenhum curso. Inscreva-se primeiro!");
                return;
            }

            System.out.println("\nEscolha um curso para iniciar:");
            for (int i = 0; i < inscricoes.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, inscricoes.get(i).getCurso().getTitulo());
            }
            System.out.print("Escolha: ");
            int escolha = lerInteiro();
            if (escolha > 0 && escolha <= inscricoes.size()) {
                Curso curso = inscricoes.get(escolha - 1).getCurso();
                aluno.iniciarTrilha(curso);
                navegarCurso(aluno, curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void navegarCurso(Aluno aluno, Curso curso) {
        System.out.println("\n📖 Curso: " + curso.getTitulo());
        for (Modulo mod : curso.getModulos()) {
            System.out.printf("  📂 Módulo %d: %s\n", mod.getOrdem(), mod.getTitulo());
            for (Aula aula : mod.getAulas()) {
                Progresso prog = aluno.obterProgresso(aula);
                String status = prog != null ? prog.getStatus().toString() : "NOT_STARTED";
                System.out.printf("    📄 Aula %d (%d min) - [%s]\n", aula.getOrdem(), aula.getDuracaoMin(), status);
            }
        }

        System.out.print("\nDigite o número da aula para abrir (0 para voltar): ");
        int aulaNum = lerInteiro();
        if (aulaNum == 0) return;

        // Encontrar aula por ordem global? Ou por módulo?
        // Simplificação: percorre todos os módulos
        Aula aulaSelecionada = null;
        for (Modulo mod : curso.getModulos()) {
            for (Aula aula : mod.getAulas()) {
                if (aula.getOrdem() == aulaNum) {
                    aulaSelecionada = aula;
                    break;
                }
            }
            if (aulaSelecionada != null) break;
        }

        if (aulaSelecionada == null) {
            System.out.println("Aula não encontrada.");
            return;
        }

        abrirAula(aluno, aulaSelecionada);
    }

    private static void abrirAula(Aluno aluno, Aula aula) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📄 AULA " + aula.getOrdem());
        System.out.println("=".repeat(50));

        // Marcar como em progresso
        Progresso prog = aluno.obterProgresso(aula);
        if (prog == null) {
            prog = new Progresso(aluno, aula);
            // Simular adição ao mapa (em produção, use método)
            try {
                java.lang.reflect.Field field = Aluno.class.getDeclaredField("progressoPorAula");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<UUID, Progresso> map = (Map<UUID, Progresso>) field.get(aluno);
                map.put(aula.getId(), prog);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        prog.marcarEmProgresso();

        // Mostrar blocos
        for (BlocoConteudo bloco : aula.getBlocos()) {
            System.out.println(bloco.render());
            System.out.println();
            pressioneEnter();
        }

        // Se tiver quiz
        if (aula.getQuiz() != null) {
            System.out.println("📝 Quiz disponível!");
            System.out.print("Deseja responder agora? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                realizarQuiz(aluno, aula.getQuiz());
            }
        }

        prog.marcarConcluido();
        System.out.println("✅ Aula concluída!");
    }

    private static void realizarQuiz(Aluno aluno, Quiz quiz) {
        RespostaSheet respostas = new RespostaSheet();
        System.out.println("\n" + "-".repeat(40));
        System.out.println("   📝 QUIZ - Nota mínima: " + quiz.getNotaMinima());
        System.out.println("-".repeat(40));

        for (int i = 0; i < quiz.getQuestoes().size(); i++) {
            Questao q = quiz.getQuestoes().get(i);
            System.out.printf("\n%d. %s (Peso: %.1f)\n", i + 1, q.enunciado, q.peso);

            if (q instanceof QUmaEscolha uma) {
                for (int j = 0; j < uma.getOpcoes().size(); j++) {
                    System.out.printf("   %c) %s\n", 'A' + j, uma.getOpcoes().get(j).getTexto());
                }
                System.out.print("Resposta (letra): ");
                char resp = scanner.nextLine().toUpperCase().charAt(0);
                int indice = resp - 'A';
                respostas.adicionar(q, new Resposta(String.valueOf(indice)));
            } else if (q instanceof QMultiplaSelecao multi) {
                for (int j = 0; j < multi.getOpcoes().size(); j++) {
                    System.out.printf("   [%d] %s\n", j + 1, multi.getOpcoes().get(j).getTexto());
                }
                System.out.print("Respostas (ex: 1,3,4): ");
                String input = scanner.nextLine();
                String indices = Arrays.stream(input.split(","))
                        .map(String::trim)
                        .map(s -> String.valueOf(Integer.parseInt(s) - 1))
                        .collect(Collectors.joining(","));
                respostas.adicionar(q, new Resposta(indices));
            } else if (q instanceof QVerdadeiroFalso vf) {
                System.out.print("Verdadeiro ou Falso? (V/F): ");
                String resp = scanner.nextLine().toUpperCase();
                boolean valor = resp.equals("V");
                respostas.adicionar(q, new Resposta(String.valueOf(valor)));
            }
        }

        TentativaQuiz tentativa = quiz.submeter(aluno, respostas);
        System.out.printf("\n✅ Nota: %.2f - %s\n", tentativa.getNota(), tentativa.isAprovado() ? "APROVADO" : "REPROVADO");
    }

    private static void verProgresso(Aluno aluno) {
        try {
            java.lang.reflect.Field field = Aluno.class.getDeclaredField("progressoPorAula");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<UUID, Progresso> progressoMap = (Map<UUID, Progresso>) field.get(aluno);

            if (progressoMap.isEmpty()) {
                System.out.println("Nenhum progresso registrado.");
                return;
            }

            System.out.println("\n📊 Seu Progresso:");
            for (Progresso p : progressoMap.values()) {
                System.out.printf("- Aula %d: %s (última visualização: %s)\n",
                        p.getAula().getOrdem(), p.getStatus(), p.getUltimaVisualizacao());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void verInsignias(Aluno aluno) {
        try {
            java.lang.reflect.Field field = Aluno.class.getDeclaredField("insignias");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<InsigniaDoUsuario> insignias = (List<InsigniaDoUsuario>) field.get(aluno);

            if (insignias.isEmpty()) {
                System.out.println("Nenhuma insígnia conquistada ainda.");
                return;
            }

            System.out.println("\n🎖️  Suas Insígnias:");
            for (InsigniaDoUsuario i : insignias) {
                System.out.printf("- %s (%s) - Conquistada em: %s\n",
                        i.getInsignia().getNome(), i.getInsignia().getDescricao(), i.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void abrirPlayground(Aluno aluno) {
        System.out.println("\n🤖 Bem-vindo ao Playground de IA!");
        System.out.println("1. Classificação Binária");
        System.out.println("2. Prompting");
        System.out.print("Escolha a tarefa: ");

        int opcao = lerInteiro();
        TarefaIA tarefa = switch (opcao) {
            case 1 -> new TarefaClassificacaoBinaria();
            case 2 -> new TarefaPrompting();
            default -> {
                System.out.println("Tarefa inválida.");
                yield null;
            }
        };

        if (tarefa == null) return;

        try {
            SessaoPlayground sessao = new SessaoPlayground(aluno, tarefa);
            sessao.iniciar();
            Parametros params = new Parametros();

            while (true) {
                System.out.println("\nDigite parâmetros (chave=valor) ou 'executar' para rodar, 'sair' para sair:");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("sair")) break;
                if (input.equalsIgnoreCase("executar")) {
                    Resultado resultado = sessao.executar(params);
                    System.out.println("\n✅ Resultado:\n" + resultado.toString());
                    params = new Parametros(); // reset
                } else if (input.contains("=")) {
                    String[] partes = input.split("=", 2);
                    params.put(partes[0].trim(), partes[1].trim());
                    System.out.println("✅ Parâmetro adicionado.");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro na sessão: " + e.getMessage());
        }
    }

    // ==================== MENU ADMIN ====================
    private static void menuAdmin(Admin admin) {
        System.out.println("\n--- MENU ADMIN: " + admin.nome + " ---");
        System.out.println("1. Listar Cursos");
        System.out.println("2. Publicar/Despublicar Curso");
        System.out.println("3. Criar Novo Curso");
        System.out.println("4. Sair da Conta");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1 -> listarCursosAdmin();
            case 2 -> publicarDespublicar(admin);
            case 3 -> criarCurso(admin);
            case 4 -> {
                admin.sair();
                usuarioLogado = null;
                System.out.println("✅ Logout realizado.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void listarCursosAdmin() {
        List<Curso> cursos = armazenamento.listarCursos();
        if (cursos.isEmpty()) {
            throw new RuntimeException("Nenhum curso cadastrado.");
        }

        for (int i = 0; i < cursos.size(); i++) {
            Curso c = cursos.get(i);
            System.out.printf("%d. %s [%s] - Criado em: %s\n",
                    i + 1, c.getTitulo(), c.isPublicado() ? "PUBLICADO" : "RASCUNHO", c.getCriadoEm());
        }
    }

    private static void publicarDespublicar(Admin admin) {
        listarCursosAdmin();
        System.out.print("Digite o número do curso: ");
        int escolha = lerInteiro();
        List<Curso> cursos = armazenamento.listarCursos();
        if (escolha > 0 && escolha <= cursos.size()) {
            Curso curso = cursos.get(escolha - 1);
            if (curso.isPublicado()) {
                admin.despublicarCurso(curso);
                System.out.println("✅ Curso despublicado.");
            } else {
                admin.publicarCurso(curso);
                System.out.println("✅ Curso publicado.");
            }
        }
    }

    private static void criarCurso(Admin admin) {
        System.out.print("Título do curso: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Curso curso = new Curso(titulo, descricao);

        Modulo mod1 = new Modulo(1, "Introdução");
        Aula aula1 = new Aula(1, 10);
        aula1.adicionarBloco("Bem-vindo ao curso!");
        aula1.adicionarBloco(new BlocoTexto(1, "Este é um bloco de texto de exemplo."));
        aula1.adicionarBloco(new BlocoCodigo(2, "java", "public class OlaMundo { }"));
        mod1.adicionarAula(aula1);

        Quiz quiz = new Quiz(70);
        QUmaEscolha q1 = new QUmaEscolha("Qual é a capital da França?", 1.0);
        q1.adicionarOpcao("Londres", false);
        q1.adicionarOpcao("Paris", true);
        q1.adicionarOpcao("Berlim", false);
        quiz.adicionarQuestao(q1);
        aula1.definirQuiz(quiz);

        curso.adicionarModulo(mod1);
        armazenamento.salvarCurso(curso);
        System.out.println("✅ Curso criado como rascunho. Publique quando estiver pronto.");
    }

    // ==================== UTILS ====================
    private static int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void pressioneEnter() {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    // ==================== DADOS EXEMPLO ====================
    private static void inicializarDados() {
        CarregadorDeDados carregador = new CarregadorDeDados();
        try {
            carregador.carregarTodosDados();
        } catch (Exception e) {
            System.err.println("Erro ao carregar os dados.");
        }
        System.out.println("✅ Dados carregados.");
    }
}