import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.nio.file.*;

public class ArmazenamentoEmMemoria {

    // Usando ConcurrentHashMap para thread-safety
    private final Map<UUID, Curso> cursos;
    private final Map<UUID, Aluno> alunos;
    private final Map<UUID, Quiz> quizzes;
    private final Map<UUID, Modulo> modulos;
    private final Map<UUID, Aula> aulas;

    // Diretórios para persistência
    private final Path diretorioDados;

    public ArmazenamentoEmMemoria() {
        this.cursos = new ConcurrentHashMap<>();
        this.alunos = new ConcurrentHashMap<>();
        this.quizzes = new ConcurrentHashMap<>();
        this.modulos = new ConcurrentHashMap<>();
        this.aulas = new ConcurrentHashMap<>();
        this.diretorioDados = Paths.get("dados");
        criarDiretoriosSeNaoExistir();
    }

    public ArmazenamentoEmMemoria(Path diretorioDados) {
        this.cursos = new ConcurrentHashMap<>();
        this.alunos = new ConcurrentHashMap<>();
        this.quizzes = new ConcurrentHashMap<>();
        this.modulos = new ConcurrentHashMap<>();
        this.aulas = new ConcurrentHashMap<>();
        this.diretorioDados = diretorioDados;
        criarDiretoriosSeNaoExistir();
    }

    private void criarDiretoriosSeNaoExistir() {
        try {
            Files.createDirectories(diretorioDados);
        } catch (IOException e) {
            System.err.println("Erro ao criar diretórios: " + e.getMessage());
        }
    }

    // =========================
    // MÉTODOS PARA CURSOS
    // =========================

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

        // Salvar também os quizzes associados às aulas do curso
        for (Aula aula : curso.listarAulas()) {
            if (aula.getQuiz() != null) {
                quizzes.put(aula.getQuiz().getId(), aula.getQuiz());
            }
        }

        // Persistir no arquivo
        persistirCursos();
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

    // =========================
    // MÉTODOS PARA ALUNOS
    // =========================

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

        // Persistir no arquivo
        persistirAlunos();
    }

    public void removerAluno(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser null");
        }
        alunos.remove(id);
        persistirAlunos();
    }

    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunos.values());
    }

    // =========================
    // MÉTODOS PARA PERSISTÊNCIA EM ARQUIVOS
    // =========================

    public void persistirCursos() {
        try {
            Path arquivoCursos = diretorioDados.resolve("cursos.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(arquivoCursos,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Curso curso : cursos.values()) {
                    // Escrever cabeçalho do curso
                    writer.write(String.format("CURSO|%s|%s|%s|%s%n",
                            curso.getId().toString(),
                            curso.getTitulo(),
                            curso.getDescricao(),
                            curso.isPublicado()));

                    // Escrever módulos
                    for (Modulo modulo : curso.getModulos()) {
                        writer.write(String.format("MODULO|%s|%d|%s%n",
                                modulo.getId().toString(),
                                modulo.getOrdem(),
                                modulo.getTitulo()));

                        // Escrever aulas
                        for (Aula aula : modulo.getAulas()) {
                            writer.write(String.format("AULA|%s|%d|%d%n",
                                    aula.getId().toString(),
                                    aula.getOrdem(),
                                    aula.getDuracaoMin()));

                            // Escrever blocos de conteúdo
                            for (BlocoConteudo bloco : aula.getBlocos()) {
                                if (bloco instanceof BlocoTexto) {
                                    BlocoTexto bt = (BlocoTexto) bloco;
                                    writer.write(String.format("BLOCO_TEXTO|%d|%s%n",
                                            bt.getOrdem(), bt.getTexto()));
                                } else if (bloco instanceof BlocoCodigo) {
                                    BlocoCodigo bc = (BlocoCodigo) bloco;
                                    writer.write(String.format("BLOCO_CODIGO|%d|%s|%s%n",
                                            bc.getOrdem(), bc.getLinguagem(), bc.getCodigo()));
                                } else if (bloco instanceof BlocoImagem) {
                                    BlocoImagem bi = (BlocoImagem) bloco;
                                    writer.write(String.format("BLOCO_IMAGEM|%d|%s|%s%n",
                                            bi.getOrdem(), bi.getCaminho(), bi.getDescricaoAlt()));
                                }
                            }

                            // Escrever quiz se existir
                            if (aula.getQuiz() != null) {
                                Quiz quiz = aula.getQuiz();
                                writer.write(String.format("QUIZ|%s|%d%n",
                                        quiz.getId().toString(),
                                        quiz.getNotaMinima()));

                                // Escrever questões
                                for (Questao questao : quiz.getQuestoes()) {
                                    if (questao instanceof QUmaEscolha) {
                                        QUmaEscolha q = (QUmaEscolha) questao;
                                        writer.write(String.format("QUESTAO_UMA_ESCOLHA|%s|%f|%d%n",
                                                q.getEnunciado(), q.getPeso(), q.getIndiceCorreto()));

                                        // Escrever opções
                                        for (int i = 0; i < q.getOpcoes().size(); i++) {
                                            Opcao opcao = q.getOpcoes().get(i);
                                            writer.write(String.format("OPCAO|%s|%s%n",
                                                    opcao.getTexto(), opcao.isCorreta()));
                                        }
                                    } else if (questao instanceof QMultiplaSelecao){

                                    } else {

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
                    writer.write(String.format("ALUNO|%s|%s|%s|%s%n",
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
}