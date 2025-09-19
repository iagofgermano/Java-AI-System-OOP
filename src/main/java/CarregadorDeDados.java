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
    private final Map<UUID, Inscricao> inscricoes = new HashMap<>();
    private final String diretorioBase = "dados";
    public CarregadorDeDados() {

    }

    public void carregarTodosDados() throws IOException {
        try {
            Files.createDirectories(Paths.get(diretorioBase));
        } catch (FileAlreadyExistsException e) {
            System.out.println("Pasta já existe");
        } catch (IOException e){
            System.out.println("Erro ao criar o diretório");
        }

        Path pathInsignias = Paths.get(diretorioBase, "insignias.txt");
        Path pathAlunos = Paths.get(diretorioBase, "alunos.txt");
        Path pathInsigniasConcedidas =  Paths.get(diretorioBase, "insignias_concedidas.txt");
        Path pathAdmins = Paths.get(diretorioBase, "admins.txt");
        Path pathCursos = Paths.get(diretorioBase, "cursos.txt");
        Path pathModulos = Paths.get(diretorioBase, "modulos.txt");
        Path pathAulas = Paths.get(diretorioBase, "aulas.txt");
        Path pathBlocosTexto = Paths.get(diretorioBase, "blocos.txt");
        Path pathQuizzes = Paths.get(diretorioBase, "quizzes.txt");
        Path pathQuestoes =  Paths.get(diretorioBase, "questoes.txt");
        Path pathInscricoes = Paths.get(diretorioBase, "inscricoes.txt");

        carregarInsignias((!Files.exists(pathInsignias)) ? Files.createFile(pathInsignias) : pathInsignias);
        carregarInsigniasConcedidas((!Files.exists(pathInsigniasConcedidas)) ? Files.createFile(pathInsigniasConcedidas) : pathInsigniasConcedidas);
        carregarAlunos((!Files.exists(pathAlunos)) ? Files.createFile(pathAlunos) : pathAlunos);
        carregarAdmins((!Files.exists(pathAdmins)) ? Files.createFile(pathAdmins) : pathAdmins);
        carregarCursos((!Files.exists(pathCursos)) ? Files.createFile(pathCursos) : pathCursos);
        carregarModulos((!Files.exists(pathModulos)) ? Files.createFile(pathModulos) : pathModulos);
        carregarAulas((!Files.exists(pathAulas)) ? Files.createFile(pathAulas) : pathAulas);
        carregarBlocosTexto((!Files.exists(pathBlocosTexto)) ? Files.createFile(pathBlocosTexto) : pathBlocosTexto);
        carregarQuizzes((!Files.exists(pathQuizzes)) ? Files.createFile(pathQuizzes) : pathQuizzes);
        carregarQuestoes((!Files.exists(pathQuestoes)) ? Files.createFile(pathQuestoes) : pathQuestoes);
        carregarInscricoes((!Files.exists(pathInscricoes))  ? Files.createFile(pathInscricoes) : pathInscricoes);
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

    public void carregarCursos(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("CURSO;")) {
                String[] partes = linha.split(";", 5);
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    String titulo = partes[2];
                    String descricao = partes[3];
                    boolean publicado = Boolean.parseBoolean(partes[4]);

                    Curso curso = new Curso(id, titulo, descricao, publicado);
                    cursos.put(id, curso);
                }
            }
        }
    }


    public void carregarInscricoes(Path arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            if (linha.startsWith("INSCRICAO;")) {
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    UUID id = UUID.fromString(partes[1]);
                    UUID alunoId = UUID.fromString(partes[2]);
                    UUID cursoId = UUID.fromString(partes[3]);
                    StatusInscricao status = StatusInscricao.valueOf(partes[4]);

                    Aluno aluno = alunos.get(alunoId);
                    Curso curso = cursos.get(cursoId);

                    Inscricao inscricao = new Inscricao(id, aluno, curso, status);
                    inscricoes.put(id, inscricao);
                }
            }
        }
    }

    public List<Inscricao> obterInscricoesPorAluno(Aluno aluno) {
        return inscricoes.values().stream()
                .filter(i -> i.getAluno().getId().equals(aluno.getId()))
                .filter(i -> i.getCurso().isPublicado())
                .collect(Collectors.toList());
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
            UUID aulaId = UUID.fromString(partes[partes.length - 1]);
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
                    double peso = Double.parseDouble(partes[3].replace(",", "."));
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
                    double peso = Double.parseDouble(partes[3].replace(",","."));
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
                    double peso = Double.parseDouble(partes[3].replace(",", "."));
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

    public Map<UUID, Curso> getCursos() { return cursos; }
    public Map<UUID, Modulo> getModulos() { return modulos; }
    public Map<UUID, Aula> getAulas() { return aulas; }
    public Map<UUID, Quiz> getQuizzes() { return quizzes; }
    public Map<UUID, Aluno> getAlunos() { return alunos; }
    public Map<UUID, Insignia> getInsignias() { return insignias; }
    public Map<UUID, InsigniaDoUsuario> getInsigniasConcedidas() {return insigniasConcedidas;}
    public Map<UUID, Admin> getAdmins() {return admins;}
}