import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CarregadorDeDados {
    private final Map<UUID, Curso> cursos = new HashMap<>();
    private final Map<UUID, Modulo> modulos = new HashMap<>();
    private final Map<UUID, Aula> aulas = new HashMap<>();
    private final Map<UUID, Quiz> quizzes = new HashMap<>();
    private final Map<UUID, Aluno> alunos = new HashMap<>();
    private final Map<UUID, Insignia> insignias = new HashMap<>();
    private final String diretorioBase = "dados";
    public CarregadorDeDados() {

    }

    public void carregarTodosDados() throws IOException {
        System.out.println("Carregando dados do diretório: " + diretorioBase);

        // Carregar em ordem de dependência
        carregarInsignias(Paths.get(diretorioBase, "insignias.txt"));
        carregarAlunos(Paths.get(diretorioBase, "alunos.txt"));
        carregarCursos(Paths.get(diretorioBase, "cursos.txt"));
        carregarModulos(Paths.get(diretorioBase, "modulos.txt"));
        carregarAulas(Paths.get(diretorioBase, "aulas.txt"));
        carregarBlocosTexto(Paths.get(diretorioBase, "blocos_texto.txt"));
        carregarQuizzes(Paths.get(diretorioBase, "quizzes.txt"));
        carregarQuestoes(Paths.get(diretorioBase, "questoes.txt"));

        System.out.println("Dados carregados com sucesso!");
        System.out.println("- Cursos: " + cursos.size());
        System.out.println("- Módulos: " + modulos.size());
        System.out.println("- Aulas: " + aulas.size());
        System.out.println("- Quizzes: " + quizzes.size());
        System.out.println("- Alunos: " + alunos.size());
        System.out.println("- Insignias: " + insignias.size());
    }

    public List<Aluno> carregarAlunos(Path arquivo) throws IOException {
        List<Aluno> alunos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            if (partes.length == 4) {
                UUID id = UUID.fromString(partes[0]);
                String nome = partes[1];
                String email = partes[2];
                String senhaHash = partes[3];

                Aluno aluno = new Aluno(nome, email, senhaHash);
                alunos.add(aluno);
            }
        }

        return alunos;
    }

    public List<Admin> carregarAdmins(Path arquivo) throws IOException {
        List<Admin> admins = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            if (partes.length == 4) {
                UUID id = UUID.fromString(partes[0]);
                String nome = partes[1];
                String email = partes[2];
                String senhaHash = partes[3];

                Admin admin = new Admin(id, nome, email, senhaHash);
                admins.add(admin);
            }
        }

        return admins;
    }

    /**
     * Carrega uma lista de cursos a partir de um arquivo .txt.
     * Formato esperado: UUID;titulo;descricao
     */
    public List<Curso> carregarCursos(Path arquivo) throws IOException {
        List<Curso> cursos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (String linha : linhas) {
            String[] partes = linha.split(";");
            if (partes.length == 3) {
                UUID id = UUID.fromString(partes[0]);
                String titulo = partes[1];
                String descricao = partes[2];

                Curso curso = new Curso(id, titulo, descricao);
                cursos.add(curso);
            }
        }

        return cursos;
    }

    public List<Modulo> carregarModulos(Path arquivo) throws IOException {
        List<Modulo> listaModulos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";");
            if (partes.length >= 4) {
                UUID id = UUID.fromString(partes[0]);
                UUID cursoId = UUID.fromString(partes[1]);
                int ordem = Integer.parseInt(partes[2]);
                String titulo = partes[3];

                Modulo modulo = new Modulo(id, ordem, titulo);
                modulos.put(id, modulo);
                listaModulos.add(modulo);

                // Associar ao curso
                Curso curso = cursos.get(cursoId);
                if (curso != null) {
                    curso.adicionarModulo(modulo);
                }
            }
        }
        return listaModulos;
    }

    public List<Aula> carregarAulas(Path arquivo) throws IOException {
        List<Aula> listaAulas = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";");
            if (partes.length >= 4) {
                UUID id = UUID.fromString(partes[0]);
                UUID moduloId = UUID.fromString(partes[1]);
                int ordem = Integer.parseInt(partes[2]);
                int duracaoMin = Integer.parseInt(partes[3]);

                Aula aula = new Aula(id, ordem, duracaoMin);
                aulas.put(id, aula);
                listaAulas.add(aula);

                // Associar ao módulo
                Modulo modulo = modulos.get(moduloId);
                if (modulo != null) {
                    modulo.adicionarAula(aula);
                }
            }
        }
        return listaAulas;
    }

    public List<BlocoTexto> carregarBlocosTexto(Path arquivo) throws IOException {
        List<BlocoTexto> listaBlocos = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";", 4); // Limita para não dividir o texto
            if (partes.length >= 3) {
                int ordem = Integer.parseInt(partes[1]);
                String texto = partes[2];
                UUID aulaId = UUID.fromString(partes[3]);

                BlocoTexto bloco = new BlocoTexto(ordem, texto);
                listaBlocos.add(bloco);

                // Associar à aula
                Aula aula = aulas.get(aulaId);
                if (aula != null) {
                    aula.adicionarBloco(bloco);
                }
            }
        }
        return listaBlocos;
    }

    public List<Quiz> carregarQuizzes(Path arquivo) throws IOException {
        List<Quiz> listaQuizzes = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";");
            if (partes.length >= 3) {
                UUID id = UUID.fromString(partes[0]);
                UUID aulaId = UUID.fromString(partes[1]);
                int notaMinima = Integer.parseInt(partes[2]);

                Quiz quiz = new Quiz(notaMinima);
                quizzes.put(id, quiz);
                listaQuizzes.add(quiz);

                // Associar à aula
                Aula aula = aulas.get(aulaId);
                if (aula != null) {
                    aula.definirQuiz(quiz);
                }
            }
        }
        return listaQuizzes;
    }

    public List<Questao> carregarQuestoes(Path arquivo) throws IOException {
        List<Questao> listaQuestoes = new ArrayList<>();
        List<String> linhas = Files.readAllLines(arquivo);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            String[] partes = linha.split(";", 6); // Limita para não dividir os dados extras
            if (partes.length >= 6) {
                UUID id = UUID.fromString(partes[0]);
                UUID quizId = UUID.fromString(partes[1]);
                String tipo = partes[2];
                String enunciado = partes[3];
                double peso = Double.parseDouble(partes[4]);
                String dadosExtras = partes[5];

                Questao questao = criarQuestaoPorTipo(tipo, enunciado, peso, dadosExtras);
                if (questao != null) {
                    listaQuestoes.add(questao);

                    // Associar ao quiz
                    Quiz quiz = quizzes.get(quizId);
                    if (quiz != null) {
                        quiz.adicionarQuestao(questao);
                    }
                }
            }
        }
        return listaQuestoes;
    }

    private Questao criarQuestaoPorTipo(String tipo, String enunciado, double peso, String dadosExtras) {
        switch (tipo) {
            case "UMA_ESCOLHA":
                return criarQuestaoUmaEscolha(enunciado, peso, dadosExtras);
            case "MULTIPLA_SELECAO":
                return criarQuestaoMultiplaSelecao(enunciado, peso, dadosExtras);
            case "VERDADEIRO_FALSO":
                return criarQuestaoVerdadeiroFalso(enunciado, peso, dadosExtras);
            default:
                System.err.println("Tipo de questão desconhecido: " + tipo);
                return null;
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
            if (partes.length >= 3) {
                UUID id = UUID.fromString(partes[0]);
                String nome = partes[1];
                String descricao = partes[2];

                Insignia insignia = new Insignia(nome, descricao);
                insignias.put(id, insignia);
                listaInsignias.add(insignia);
            }
        }
        return listaInsignias;
    }

    // Getters para acesso aos dados carregados
    public Map<UUID, Curso> getCursos() { return cursos; }
    public Map<UUID, Modulo> getModulos() { return modulos; }
    public Map<UUID, Aula> getAulas() { return aulas; }
    public Map<UUID, Quiz> getQuizzes() { return quizzes; }
    public Map<UUID, Aluno> getAlunos() { return alunos; }
    public Map<UUID, Insignia> getInsignias() { return insignias; }
}