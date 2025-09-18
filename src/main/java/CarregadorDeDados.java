import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CarregadorDeDados {
    private final Map<UUID, Curso> cursos = new HashMap<>();
    private final Map<UUID, Modulo> modulos = new HashMap<>();
    private final Map<UUID, Aula> aulas = new HashMap<>();
    private final Map<UUID, Quiz> quizzes = new HashMap<>();
    private final Map<UUID, Aluno> alunos = new HashMap<>();
    private final Map<UUID, Insignia> insignias = new HashMap<>();
    private final Map<UUID, InsigniaDoUsuario> insigniasConcedidas = new HashMap<>();
    private final Map<UUID, Admin> admins = new HashMap<>();
    private final String diretorioBase = "dados";
    public CarregadorDeDados() {

    }

    public void carregarTodosDados() throws IOException {
        System.out.println("Carregando dados do diretório: " + diretorioBase);

        // Carregar em ordem de dependência
        carregarInsignias(Paths.get(diretorioBase, "insignias.txt"));
        carregarInsigniasConcedidas(Paths.get(diretorioBase, "insignias_concedidas.txt"));
        carregarAlunos(Paths.get(diretorioBase, "alunos.txt"));
        carregarAdmins(Paths.get(diretorioBase, "admins.txt"));
        carregarCursos(Paths.get(diretorioBase, "cursos.txt"));
        carregarModulos(Paths.get(diretorioBase, "modulos.txt"));
        carregarAulas(Paths.get(diretorioBase, "aulas.txt"));
        carregarBlocosTexto(Paths.get(diretorioBase, "blocos.txt"));
        carregarQuizzes(Paths.get(diretorioBase, "quizzes.txt"));
        carregarQuestoes(Paths.get(diretorioBase, "questoes.txt"));

        System.out.println("Dados carregados com sucesso!");
        System.out.println("- Cursos: " + cursos.size());
        System.out.println("- Módulos: " + modulos.size());
        System.out.println("- Aulas: " + aulas.size());
        System.out.println("- Quizzes: " + quizzes.size());
        System.out.println("- Alunos: " + alunos.size());
        System.out.println("- Insignias: " + insignias.size());
        System.out.println("- Concessões: " + insigniasConcedidas.size());
        System.out.println("- Admins: " + admins.size());
    }

    public void carregarAlunos(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            if (partes.length == 5) {
                UUID id = UUID.fromString(partes[1]);
                String nome = partes[2];
                String email = partes[3];
                String senhaHash = partes[4];

                Aluno aluno = new Aluno(id, nome, email, senhaHash);
                alunos.put(id, aluno);
            }
        }
    }

    public void carregarAdmins(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            if (partes.length == 5) {
                UUID id = UUID.fromString(partes[1]);
                String nome = partes[2];
                String email = partes[3];
                String senhaHash = partes[4];

                Admin admin = new Admin(id, nome, email, senhaHash);
                admins.put(id, admin);
            }
        }
    }

    /**
     * Carrega uma lista de cursos a partir de um arquivo .txt.
     * Formato esperado: UUID;titulo;descricao
     */
    public void carregarCursos(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("CURSO;")) {
                String[] partes = linha.split(";", 5); // limita para evitar split no texto
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    String titulo = partes[2];
                    String descricao = partes[3];
                    boolean publicado = Boolean.parseBoolean(partes[4]);

                    Curso curso = new Curso(id, titulo, descricao);
                    // Se tiver setter para publicado:
                    try {
                        curso.getClass().getMethod("setPublicado", boolean.class).invoke(curso, publicado);
                    } catch (Exception e) {
                        System.err.println("Não foi possível definir publicado para curso " + id);
                    }
                    cursos.put(id, curso);
                }
            }
        }
    }

    public void carregarModulos(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("MODULO;")) {
                String[] partes = linha.split(";", 5);
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    UUID cursoId = UUID.fromString(partes[2]);
                    int ordem = Integer.parseInt(partes[3]);
                    String titulo = partes[4];

                    Modulo modulo = new Modulo(id, ordem, titulo);
                    modulos.put(id, modulo);

                    Curso curso = cursos.get(cursoId);
                    if (curso != null) {
                        curso.adicionarModulo(modulo);
                    } else {
                        System.err.println("Curso não encontrado para módulo: " + id);
                    }
                }
            }
        }
    }

    public void carregarAulas(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("AULA;")) {
                String[] partes = linha.split(";", 5);
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    UUID moduloId = UUID.fromString(partes[2]);
                    int ordem = Integer.parseInt(partes[3]);
                    int duracaoMin = Integer.parseInt(partes[4]);

                    Aula aula = new Aula(id, ordem, duracaoMin);
                    aulas.put(id, aula);

                    Modulo modulo = modulos.get(moduloId);
                    if (modulo != null) {
                        modulo.adicionarAula(aula);
                    } else {
                        System.err.println("Módulo não encontrado para aula: " + id);
                    }
                }
            }
        }
    }

    public void carregarBlocosTexto(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";", 5);
            if (partes.length < 4) continue;

            int ordem = Integer.parseInt(partes[1]);
            UUID aulaId = UUID.fromString(partes[partes.length - 1]); // último campo é sempre aulaId
            Aula aula = aulas.get(aulaId);
            if (aula == null) continue;

            if (linha.startsWith("BLOCO_TEXTO;") && partes.length == 4) {
                String texto = partes[2];
                aula.adicionarBloco(new BlocoTexto(ordem, texto));
            } else if (linha.startsWith("BLOCO_CODIGO;") && partes.length == 5) {
                String linguagem = partes[2];
                String codigo = partes[3];
                aula.adicionarBloco(new BlocoCodigo(ordem, linguagem, codigo));
            } else if (linha.startsWith("BLOCO_IMAGEM;") && partes.length == 5) {
                String caminho = partes[2];
                String descricaoAlt = partes[3];
                aula.adicionarBloco(new BlocoImagem(ordem, caminho, descricaoAlt));
            }
        }
    }

    public void carregarQuizzes(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("QUIZ;")) {
                String[] partes = linha.split(";", 4);
                if (partes.length == 4) {
                    UUID id = UUID.fromString(partes[1]);
                    UUID aulaId = UUID.fromString(partes[2]);
                    int notaMinima = Integer.parseInt(partes[3]);

                    Quiz quiz = new Quiz(id, notaMinima);
                    quizzes.put(id, quiz);

                    Aula aula = aulas.get(aulaId);
                    if (aula != null) {
                        aula.definirQuiz(quiz);
                    } else {
                        System.err.println("Aula não encontrada para quiz: " + id);
                    }
                }
            }
        }
    }

    public void carregarQuestoes(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("QUESTAO_UMA_ESCOLHA;")) {
                String[] partes = linha.split(";", 6);
                if (partes.length == 6) {
                    UUID quizId = UUID.fromString(partes[1]);
                    String enunciado = partes[2].replace("\\;", ";");
                    double peso = Double.parseDouble(partes[3]);
                    int indiceCorreto = Integer.parseInt(partes[4]);
                    String[] opcoesStr = partes[5].split("\\|");

                    QUmaEscolha questao = new QUmaEscolha(enunciado, peso);
                    for (int i = 0; i < opcoesStr.length; i++) {
                        String texto = opcoesStr[i].replace("\\|", "|");
                        questao.adicionarOpcao(texto, i == indiceCorreto);
                    }

                    Quiz quiz = quizzes.get(quizId);
                    if (quiz != null) {
                        quiz.adicionarQuestao(questao);
                    }
                }
            } else if (linha.startsWith("QUESTAO_MULTIPLA_SELECAO;")) {
                String[] partes = linha.split(";", 6);
                if (partes.length == 6) {
                    UUID quizId = UUID.fromString(partes[1]);
                    String enunciado = partes[2].replace("\\;", ";");
                    double peso = Double.parseDouble(partes[3]);
                    String[] opcoesStr = partes[4].split("\\|");
                    String[] indicesCorretosStr = partes[5].split(",");

                    Set<Integer> indicesCorretos = Arrays.stream(indicesCorretosStr)
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .collect(Collectors.toSet());

                    QMultiplaSelecao questao = new QMultiplaSelecao(enunciado, peso);
                    for (int i = 0; i < opcoesStr.length; i++) {
                        String texto = opcoesStr[i].replace("\\|", "|");
                        questao.adicionarOpcao(texto, indicesCorretos.contains(i));
                    }

                    Quiz quiz = quizzes.get(quizId);
                    if (quiz != null) {
                        quiz.adicionarQuestao(questao);
                    }
                }
            } else if (linha.startsWith("QUESTAO_VERDADEIRO_FALSO;")) {
                String[] partes = linha.split(";", 5);
                if (partes.length == 5) {
                    UUID quizId = UUID.fromString(partes[1]);
                    String enunciado = partes[2].replace("\\;", ";");
                    double peso = Double.parseDouble(partes[3]);
                    boolean correto = Boolean.parseBoolean(partes[4]);

                    QVerdadeiroFalso questao = new QVerdadeiroFalso(enunciado, peso, correto);

                    Quiz quiz = quizzes.get(quizId);
                    if (quiz != null) {
                        quiz.adicionarQuestao(questao);
                    }
                }
            }
        }
    }

    private Questao criarQuestaoUmaEscolha(String enunciado, double peso, String dadosExtras) {
        String[] partes = dadosExtras.split("\\|");
        QUmaEscolha questao = new QUmaEscolha(enunciado, peso);

        // Última parte é o índice correto
        int indiceCorreto = Integer.parseInt(partes[partes.length - 1]);

        // Adicionar opções
        for (int i = 0; i < partes.length - 1; i++) {
            boolean correta = (i == indiceCorreto);
            questao.adicionarOpcao(partes[i], correta);
        }

        return questao;
    }

    private Questao criarQuestaoMultiplaSelecao(String enunciado, double peso, String dadosExtras) {
        String[] partes = dadosExtras.split("\\|");
        QMultiplaSelecao questao = new QMultiplaSelecao(enunciado, peso);

        // Última parte são os índices corretos
        String[] indicesCorretosStr = partes[partes.length - 1].split(",");
        Set<Integer> indicesCorretos = new HashSet<>();
        for (String indiceStr : indicesCorretosStr) {
            indicesCorretos.add(Integer.parseInt(indiceStr.trim()));
        }

        // Adicionar opções
        for (int i = 0; i < partes.length - 1; i++) {
            boolean correta = indicesCorretos.contains(i);
            questao.adicionarOpcao(partes[i], correta);
        }

        return questao;
    }

    private Questao criarQuestaoVerdadeiroFalso(String enunciado, double peso, String dadosExtras) {
        boolean respostaCorreta = Boolean.parseBoolean(dadosExtras);
        return new QVerdadeiroFalso(enunciado, peso, respostaCorreta);
    }

    public List<Insignia> carregarInsignias(Path arquivo) throws IOException {
        List<Insignia> listaInsignias = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";");
            if (partes.length == 4) {
                UUID id = UUID.fromString(partes[1]);
                String nome = partes[2];
                String descricao = partes[3];

                Insignia insignia = new Insignia(nome, descricao);
                insignias.put(id, insignia);
                listaInsignias.add(insignia);
            }
        }
        return listaInsignias;
    }

    public void carregarInsigniasConcedidas(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);
        for (String linha : linhas) {
            if (linha.startsWith("CONCESSAO;")) {
                String[] partes = linha.split(";", 5);
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    UUID alunoId = UUID.fromString(partes[2]);
                    UUID insigniaId = UUID.fromString(partes[3]);
                    LocalDateTime data = LocalDateTime.parse(partes[4]);

                    Aluno aluno = alunos.get(alunoId);
                    Insignia insignia = insignias.get(insigniaId);

                    if (aluno != null && insignia != null) {
                        InsigniaDoUsuario concessao = new InsigniaDoUsuario(id, aluno, insignia, data);
                        insigniasConcedidas.put(id, concessao);
                    }
                }
            }
        }
    }

    // Getters para acesso aos dados carregados
    public Map<UUID, Curso> getCursos() { return cursos; }
    public Map<UUID, Modulo> getModulos() { return modulos; }
    public Map<UUID, Aula> getAulas() { return aulas; }
    public Map<UUID, Quiz> getQuizzes() { return quizzes; }
    public Map<UUID, Aluno> getAlunos() { return alunos; }
    public Map<UUID, Insignia> getInsignias() { return insignias; }
    public Map<UUID, InsigniaDoUsuario> getInsigniasConcedidas() {return insigniasConcedidas;}
    public Map<UUID, Admin> getAdmins() {return admins;}
}