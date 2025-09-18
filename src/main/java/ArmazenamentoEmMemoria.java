import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class ArmazenamentoEmMemoria {
    private final Map<UUID, Curso> cursos;
    private final Map<UUID, Aluno> alunos;
    private final Map<UUID, Quiz> quizzes;
    private final Map<UUID, Modulo> modulos;
    private final Map<UUID, Aula> aulas;
    private final Map<UUID, Admin> admins;
    private final Map<UUID, Inscricao> inscricoes;
    private final Map<UUID, Insignia> insignias;
    private final Map<UUID, InsigniaDoUsuario> insigniasConcedidas;
    private final CarregadorDeDados carregador;

    private final Path diretorioDados;

    public ArmazenamentoEmMemoria() {
        this.cursos = new ConcurrentHashMap<>();
        this.alunos = new ConcurrentHashMap<>();
        this.quizzes = new ConcurrentHashMap<>();
        this.modulos = new ConcurrentHashMap<>();
        this.aulas = new ConcurrentHashMap<>();
        this.admins = new ConcurrentHashMap<>();
        this.inscricoes = new ConcurrentHashMap<>();
        this.insignias = new ConcurrentHashMap<>();
        this.insigniasConcedidas = new ConcurrentHashMap<>();
        this.carregador = new CarregadorDeDados();
        try {
            carregador.carregarTodosDados();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.diretorioDados = Paths.get("dados");
        criarDiretoriosSeNaoExistir();
    }

    private void criarDiretoriosSeNaoExistir() {
        try {
            Files.createDirectories(diretorioDados);
        } catch (IOException e) {
            System.err.println("Erro ao criar diretórios: " + e.getMessage());
        }
    }

    public Curso obterCurso(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do curso não pode ser null");
        }
        return cursos.get(id);
    }

    public void salvarCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("Curso não pode ser null");
        }
        cursos.put(curso.getId(), curso);
        for (Aula aula : curso.listarAulas()) {
            if (aula.getQuiz() != null) {
                quizzes.put(aula.getQuiz().getId(), aula.getQuiz());
            }
        }
        persistirCursos();
    }

    public void salvarInscricao(Inscricao i) {
        inscricoes.put(i.getId(), i);
        persistirInscricoes();

    }

    public void removerCurso(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do curso não pode ser null");
        }
        cursos.remove(id);
        persistirCursos();
    }

    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos.values());
    }

    public List<Curso> listarCursosPublicados() {
        return cursos.values().stream()
                .filter(curso -> {
                    try {
                        return (boolean) curso.getClass().getMethod("isPublicado").invoke(curso);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(ArrayList::new, (list, curso) -> list.add(curso), ArrayList::addAll);
    }

    public Aluno obterAluno(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser null");
        }
        return alunos.get(id);
    }

    public void salvarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser null");
        }
        alunos.put(aluno.getId(), aluno);
        persistirAlunos();
    }

    public void salvarAdmin(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin não pode ser null");
        }
        admins.put(admin.getId(), admin);
        persistirAdmins();
    }

    public void salvarInsignia(Insignia insignia) {
        if (insignia == null) {
            throw new IllegalArgumentException("Insígnia não pode ser null");
        }
        insignias.put(insignia.getId(), insignia);
        persistirInsignias();
    }

    public Map<UUID, Insignia> getInsignias() {
        return insignias;
    }

    public Map<UUID, Aluno> getAlunos() {
        return alunos;
    }

    public Map<UUID, Admin> getAdmins() {
        return admins;
    }

    public Map<UUID, Curso> getCursos() {
        return cursos;
    }

    public Map<UUID, Aula> getAulas() {
        return aulas;
    }

    public Map<UUID, Modulo> getModulos() {
        return modulos;
    }

    public Map<UUID, Quiz> getQuizzes() {
        return quizzes;
    }

    public Map<UUID, Inscricao> getInscricoes() {
        return inscricoes;
    }

    public Map<UUID, InsigniaDoUsuario> getInsigniasConcedidas() {
        return insigniasConcedidas;
    }

    public void persistirCursos() {
        try {
            Path arquivoCursos = diretorioDados.resolve("cursos.txt");
            Path arquivoModulos = diretorioDados.resolve("modulos.txt");
            Path arquivoAulas = diretorioDados.resolve("aulas.txt");
            Path arquivoBlocos = diretorioDados.resolve("blocos.txt");
            Path arquivoQuizzes = diretorioDados.resolve("quizzes.txt");
            Path arquivoQuestoes = diretorioDados.resolve("questoes.txt");
            try (
                    BufferedWriter writerCursos = Files.newBufferedWriter(arquivoCursos, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    BufferedWriter writerModulos = Files.newBufferedWriter(arquivoModulos, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    BufferedWriter writerAulas = Files.newBufferedWriter(arquivoAulas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    BufferedWriter writerBlocos = Files.newBufferedWriter(arquivoBlocos, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    BufferedWriter writerQuizzes = Files.newBufferedWriter(arquivoQuizzes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    BufferedWriter writerQuestoes = Files.newBufferedWriter(arquivoQuestoes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            ) {

                for (Curso curso : cursos.values()) {
                    writerCursos.append(String.format("CURSO;%s;%s;%s;%s%n",
                            curso.getId().toString(),
                            curso.getTitulo(),
                            curso.getDescricao(),
                            curso.isPublicado()));

                    for (Modulo modulo : curso.getModulos()) {
                        writerModulos.append(String.format("MODULO;%s;%s;%d;%s%n",
                                modulo.getId().toString(),
                                curso.getId().toString(),
                                modulo.getOrdem(),
                                modulo.getTitulo()));

                        for (Aula aula : modulo.getAulas()) {
                            writerAulas.append(String.format("AULA;%s;%s;%d;%d%n",
                                    aula.getId().toString(),
                                    modulo.getId().toString(),
                                    aula.getOrdem(),
                                    aula.getDuracaoMin()));

                            for (BlocoConteudo bloco : aula.getBlocos()) {
                                if (bloco instanceof BlocoTexto) {
                                    BlocoTexto bt = (BlocoTexto) bloco;
                                    writerBlocos.append(String.format("BLOCO_TEXTO;%d;%s;%s%n",
                                            bt.getOrdem(), bt.getTexto(), aula.getId().toString()));
                                } else if (bloco instanceof BlocoCodigo) {
                                    BlocoCodigo bc = (BlocoCodigo) bloco;
                                    writerBlocos.append(String.format("BLOCO_CODIGO;%d;%s;%s;%s%n",
                                            bc.getOrdem(), bc.getLinguagem(), bc.getCodigo(), aula.getId().toString()));
                                } else if (bloco instanceof BlocoImagem) {
                                    BlocoImagem bi = (BlocoImagem) bloco;
                                    writerBlocos.append(String.format("BLOCO_IMAGEM;%d;%s;%s;%s%n",
                                            bi.getOrdem(), bi.getCaminho(), bi.getDescricaoAlt(), aula.getId().toString()));
                                }
                            }

                            if (aula.getQuiz() != null) {
                                Quiz quiz = aula.getQuiz();
                                writerQuizzes.append(String.format("QUIZ;%s;%s;%d%n",
                                        quiz.getId().toString(),
                                        aula.getId().toString(),
                                        quiz.getNotaMinima()));

                                for (Questao questao : quiz.getQuestoes()) {
                                    if (questao instanceof QUmaEscolha) {
                                        QUmaEscolha q = (QUmaEscolha) questao;
                                        String opcoesStr = q.getOpcoes().stream()
                                                .map(op -> op.getTexto().replace("|", "\\|"))
                                                .collect(Collectors.joining("|"));
                                        writerQuestoes.append(String.format("QUESTAO_UMA_ESCOLHA;%s;%s;%f;%d;%s%n",
                                                quiz.getId().toString(),
                                                q.getEnunciado().replace(";", "\\;"),
                                                q.getPeso(),
                                                q.getIndiceCorreto(),
                                                opcoesStr));

                                    } else if (questao instanceof QMultiplaSelecao) {
                                        QMultiplaSelecao q = (QMultiplaSelecao) questao;
                                        String opcoesStr = q.getOpcoes().stream()
                                                .map(op -> op.getTexto().replace("|", "\\|"))
                                                .collect(Collectors.joining("|"));
                                        String indicesCorretos = q.getOpcoes().stream()
                                                .filter(Opcao::isCorreta)
                                                .map(op -> String.valueOf(q.getOpcoes().indexOf(op)))
                                                .collect(Collectors.joining(","));
                                        writerQuestoes.append(String.format("QUESTAO_MULTIPLA_SELECAO;%s;%s;%f;%s;%s%n",
                                                quiz.getId().toString(),
                                                q.getEnunciado().replace(";", "\\;"),
                                                q.getPeso(),
                                                opcoesStr,
                                                indicesCorretos));
                                    } else if (questao instanceof QVerdadeiroFalso) {
                                        QVerdadeiroFalso q = (QVerdadeiroFalso) questao;
                                        writerQuestoes.append(String.format("QUESTAO_VERDADEIRO_FALSO;%s;%s;%f;%s%n",
                                                quiz.getId().toString(),
                                                q.getEnunciado().replace(";", "\\;"),
                                                q.getPeso(),
                                                q.isCorreto()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir cursos: " + e.getMessage());
        }
    }

    public void persistirAlunos() {
        try {
            Path arquivoAlunos = diretorioDados.resolve("alunos.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoAlunos,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Aluno aluno : alunos.values()) {
                    writer.append(String.format("ALUNO;%s;%s;%s;%s%n",
                            aluno.getId().toString(),
                            aluno.getNome(),
                            aluno.getEmail(),
                            aluno.getSenhaHash()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir alunos: " + e.getMessage());
        }
    }

    public void persistirAdmins() {
        try {
            Path arquivoAlunos = diretorioDados.resolve("admins.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoAlunos,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Admin admin : admins.values()) {
                    writer.append(String.format("ADMIN;%s;%s;%s;%s%n",
                            admin.getId().toString(),
                            admin.getNome(),
                            admin.getEmail(),
                            admin.getSenhaHash()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir admins: " + e.getMessage());
        }
    }

    public void persistirInsignias() {
        try {
            Path arquivoInsignias = diretorioDados.resolve("insignias.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoInsignias,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Insignia insignia : insignias.values()) {
                    writer.append(String.format("INSIGNIA;%s;%s;%s%n",
                            insignia.getId().toString(),
                            insignia.getNome().replace(";", "\\;"),
                            insignia.getDescricao().replace(";", "\\;")));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir insígnias: " + e.getMessage());
        }
    }

    public void persistirInsigniasConcedidas() {
        try {
            Path arquivoConcessoes = diretorioDados.resolve("insignias_usuarios.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoConcessoes,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (InsigniaDoUsuario concessao : insigniasConcedidas.values()) {
                    writer.append(String.format("CONCESSAO;%s;%s;%s;%s%n",
                            concessao.getId().toString(),
                            concessao.getAluno().getId().toString(),
                            concessao.getInsignia().getId().toString(),
                            concessao.getData().toString()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir concessões de insígnias: " + e.getMessage());
        }
    }

    public void persistirInscricoes() {
        try {
            Path arquivoInscricoes = diretorioDados.resolve("inscricoes.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoInscricoes,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Inscricao inscricao : inscricoes.values()) {
                    writer.append(String.format("INSCRICAO;%s;%s;%s;%s%n",
                            inscricao.getId().toString(),
                            inscricao.getAluno().getId().toString(),
                            inscricao.getCurso().getId().toString(),
                            inscricao.getStatus().toString()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao persistir inscrições: " + e.getMessage());
        }
    }
}