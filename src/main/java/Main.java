import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArmazenamentoEmMemoria armazenamento = new ArmazenamentoEmMemoria();
    private static final CarregadorDeDados carregador =  new CarregadorDeDados();
    private static final GestorAcademico gestor = new GestorAcademico(armazenamento);
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        try {
            carregador.carregarTodosDados();
            armazenamento.getAlunos().putAll(carregador.getAlunos());
            armazenamento.getCursos().putAll(carregador.getCursos());
            armazenamento.getAdmins().putAll(carregador.getAdmins());
            armazenamento.getModulos().putAll(carregador.getModulos());
            armazenamento.getAulas().putAll(carregador.getAulas());
            armazenamento.getInsignias().putAll(carregador.getInsignias());
            armazenamento.getInsigniasConcedidas().putAll(carregador.getInsigniasConcedidas());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Optional<Aluno> alunoOpt = carregador.getAlunos().values().stream()
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

        Optional<Admin> adminOpt = carregador.getAdmins().values().stream()
                .filter(a -> a.email.equals(email))
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
        try {
            carregador.carregarTodosDados();
        } catch (Exception e) {
            System.out.println("Erro ao carregar todos os dados.");
        }
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
        try {
            carregador.carregarTodosDados();
        } catch (Exception e) {
            System.out.println("Erro ao carregar todos os dados.");
        }
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
        List<Inscricao> inscricoes = carregador.obterInscricoesPorAluno(aluno);

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
        try {
            List<Inscricao> inscricoes = carregador.obterInscricoesPorAluno(aluno);

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
        System.out.println("4. Criar Insignia");
        System.out.println("5. Sair da Conta");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        switch (opcao) {
            case 1 -> listarCursosAdmin();
            case 2 -> publicarDespublicar(admin);
            case 3 -> criarCurso(admin);
            case 4 -> criarInsignia(admin);
            case 5 -> {
                admin.sair();
                usuarioLogado = null;
                System.out.println("✅ Logout realizado.");
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void listarCursosAdmin() {
        List<Curso> cursos = armazenamento.listarCursos();
        try {
            if (cursos.isEmpty()) {
                throw new RuntimeException("Nenhum curso cadastrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                armazenamento.persistirCursos();
                System.out.println("✅ Curso publicado.");
            }
        }
    }

    private static void criarInsignia(Admin admin) {
        System.out.print("Qual será o nome da Insígnia?");
        String nome = scanner.nextLine();
        System.out.print("Qual é o objetivo da Insignia?");
        String descricao = scanner.nextLine();
        Insignia insignia = new Insignia(nome, descricao);
        armazenamento.salvarInsignia(insignia);
        try {
            carregador.carregarTodosDados();
        } catch (Exception e) {
            System.out.println("Erro ao carregar dados.");
        }

        System.out.println("Insignia criada com sucesso.");
    }

    private static void criarCurso(Admin admin) {
        System.out.print("Título do curso: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Curso curso = new Curso(titulo, descricao);

        // Adicionar módulos
        int numModulo = 1;
        while (true) {
            System.out.println("\n--- Criando Módulo " + numModulo + " ---");
            System.out.print("Título do módulo (ou 'fim' para encerrar): ");
            String tituloModulo = scanner.nextLine();
            if (tituloModulo.equalsIgnoreCase("fim")) break;

            Modulo modulo = new Modulo(numModulo, tituloModulo);

            // Adicionar aulas
            int numAula = 1;
            while (true) {
                System.out.println("\n--- Criando Aula " + numAula + " no Módulo " + numModulo + " ---");
                System.out.print("Duração da aula (em minutos): ");
                int duracao = lerInteiro();
                if (duracao <= 0) {
                    System.out.println("Duração inválida. Tente novamente.");
                    continue;
                }

                Aula aula = new Aula(numAula, duracao);

                // Adicionar blocos de conteúdo
                int ordemBloco = 1;
                while (true) {
                    System.out.println("\nAdicionar bloco à Aula " + numAula + ":");
                    System.out.println("1. Texto");
                    System.out.println("2. Imagem");
                    System.out.println("3. Código");
                    System.out.println("0. Finalizar blocos");
                    System.out.print("Escolha: ");
                    int tipoBloco = lerInteiro();

                    if (tipoBloco == 0) break;

                    switch (tipoBloco) {
                        case 1 -> {
                            System.out.print("Conteúdo do texto: ");
                            String texto = scanner.nextLine();
                            aula.adicionarBloco(new BlocoTexto(ordemBloco, texto));
                            ordemBloco++;
                        }
                        case 2 -> {
                            System.out.print("Caminho da imagem: ");
                            String caminho = scanner.nextLine();
                            System.out.print("Descrição alternativa (alt text): ");
                            String alt = scanner.nextLine();
                            aula.adicionarBloco(new BlocoImagem(ordemBloco, caminho, alt));
                            ordemBloco++;
                        }
                        case 3 -> {
                            System.out.print("Linguagem (ex: java, python): ");
                            String lang = scanner.nextLine();
                            System.out.print("Código: ");
                            StringBuilder codigo = new StringBuilder();
                            System.out.println("Digite o código (digite 'FIM' em uma linha vazia para terminar):");
                            String linha;
                            while (!(linha = scanner.nextLine()).equals("FIM")) {
                                codigo.append(linha).append("\n");
                            }
                            aula.adicionarBloco(new BlocoCodigo(ordemBloco, lang, codigo.toString().trim()));
                            ordemBloco++;
                        }
                        default -> System.out.println("Tipo inválido.");
                    }
                }

                // Adicionar quiz?
                System.out.print("\nDeseja adicionar um quiz a esta aula? (s/n): ");
                if (scanner.nextLine().equalsIgnoreCase("s")) {
                    Quiz quiz = criarQuizInterativo();
                    if (quiz != null) {
                        aula.definirQuiz(quiz);
                    }
                }

                modulo.adicionarAula(aula);
                numAula++;

                System.out.print("Deseja adicionar outra aula neste módulo? (s/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("s")) break;
            }

            curso.adicionarModulo(modulo);
            numModulo++;

            System.out.print("Deseja adicionar outro módulo? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) break;
        }

        armazenamento.salvarCurso(curso);
        try {
            carregador.carregarTodosDados();
        } catch (Exception e) {
            System.out.println("Erro ao carregar o curso.");
        }
        System.out.println("\n✅ Curso '" + curso.getTitulo() + "' criado como rascunho. Publique quando estiver pronto.");
    }

    private static Quiz criarQuizInterativo() {
        System.out.print("Nota mínima para aprovação (0-100): ");
        int notaMinima = lerInteiro();
        if (notaMinima < 0 || notaMinima > 100) {
            System.out.println("Nota inválida. Usando 60 como padrão.");
            notaMinima = 60;
        }

        Quiz quiz = new Quiz(notaMinima);

        int numQuestao = 1;
        while (true) {
            System.out.println("\n--- Questão " + numQuestao + " ---");
            System.out.print("Enunciado: ");
            String enunciado = scanner.nextLine();
            System.out.print("Peso (ex: 1.0): ");
            double peso = Double.parseDouble(scanner.nextLine());

            System.out.println("Tipo de questão:");
            System.out.println("1. Uma Escolha");
            System.out.println("2. Múltipla Seleção");
            System.out.println("3. Verdadeiro/Falso");
            System.out.print("Escolha: ");
            int tipo = lerInteiro();

            Questao questao = switch (tipo) {
                case 1 -> criarQuestaoUmaEscolha(enunciado, peso);
                case 2 -> criarQuestaoMultiplaSelecao(enunciado, peso);
                case 3 -> criarQuestaoVerdadeiroFalso(enunciado, peso);
                default -> {
                    System.out.println("Tipo inválido. Pulando questão.");
                    yield null;
                }
            };

            if (questao != null) {
                quiz.adicionarQuestao(questao);
                numQuestao++;
            }

            System.out.print("Adicionar outra questão? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) break;
        }

        return quiz;
    }

    private static QUmaEscolha criarQuestaoUmaEscolha(String enunciado, double peso) {
        QUmaEscolha q = new QUmaEscolha(enunciado, peso);
        int opcaoNum = 1;
        while (true) {
            System.out.print("Opção " + opcaoNum + " (texto): ");
            String texto = scanner.nextLine();
            System.out.print("É correta? (s/n): ");
            boolean correta = scanner.nextLine().equalsIgnoreCase("s");
            q.adicionarOpcao(texto, correta);
            opcaoNum++;
            System.out.print("Adicionar outra opção? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) break;
        }
        return q;
    }

    private static QMultiplaSelecao criarQuestaoMultiplaSelecao(String enunciado, double peso) {
        QMultiplaSelecao q = new QMultiplaSelecao(enunciado, peso);
        int opcaoNum = 1;
        while (true) {
            System.out.print("Opção " + opcaoNum + " (texto): ");
            String texto = scanner.nextLine();
            System.out.print("É correta? (s/n): ");
            boolean correta = scanner.nextLine().equalsIgnoreCase("s");
            q.adicionarOpcao(texto, correta);
            opcaoNum++;
            System.out.print("Adicionar outra opção? (s/n): ");
            if (!scanner.nextLine().equalsIgnoreCase("s")) break;
        }
        return q;
    }

    private static QVerdadeiroFalso criarQuestaoVerdadeiroFalso(String enunciado, double peso) {
        System.out.print("A afirmação é verdadeira? (s/n): ");
        boolean correta = scanner.nextLine().equalsIgnoreCase("s");
        return new QVerdadeiroFalso(enunciado, peso, correta);
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
}