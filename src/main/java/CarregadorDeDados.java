import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

public class CarregadorDeDados {

    public CarregadorDeDados() {
        // Construtor padrão
    }

    /**
     * Carrega uma lista de cursos a partir de um arquivo TXT.
     *
     * @param arquivo caminho do arquivo contendo os dados dos cursos
     * @return lista de cursos carregados
     */
    public List<Curso> carregarCursos(Path arquivo) {
        List<Curso> cursos = new ArrayList<>();
        Map<String, Curso> cursosMap = new HashMap<>();
        Map<String, Modulo> modulosMap = new HashMap<>();
        Map<String, Aula> aulasMap = new HashMap<>();
        Map<String, Quiz> quizzesMap = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(arquivo)) {
            String linha;
            Curso cursoAtual = null;
            Modulo moduloAtual = null;
            Aula aulaAtual = null;
            Quiz quizAtual = null;
            Questao questaoAtual = null;

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split("\\|");
                String tipo = partes[0];

                switch (tipo) {
                    case "CURSO":
                        if (partes.length >= 5) {
                            String id = partes[1];
                            String titulo = partes[2];
                            String descricao = partes[3];
                            boolean publicado = Boolean.parseBoolean(partes[4]);

                            cursoAtual = new Curso(titulo, descricao);
                            // Setar o ID manualmente se houver setter
                            cursos.add(cursoAtual);
                            cursosMap.put(id, cursoAtual);
                        }
                        break;

                    case "MODULO":
                        if (partes.length >= 4 && cursoAtual != null) {
                            String id = partes[1];
                            int ordem = Integer.parseInt(partes[2]);
                            String titulo = partes[3];

                            moduloAtual = new Modulo(ordem, titulo);
                            cursoAtual.adicionarModulo(moduloAtual);
                            modulosMap.put(id, moduloAtual);
                        }
                        break;

                    case "AULA":
                        if (partes.length >= 4 && moduloAtual != null) {
                            String id = partes[1];
                            int ordem = Integer.parseInt(partes[2]);
                            int duracaoMin = Integer.parseInt(partes[3]);

                            aulaAtual = new Aula(ordem, duracaoMin);
                            moduloAtual.adicionarAula(aulaAtual);
                            aulasMap.put(id, aulaAtual);
                        }
                        break;

                    case "BLOCO_TEXTO":
                        if (partes.length >= 3 && aulaAtual != null) {
                            int ordem = Integer.parseInt(partes[1]);
                            String texto = partes[2];

                            BlocoTexto bloco = new BlocoTexto(ordem, texto);
                            aulaAtual.adicionarBloco(bloco);
                        }
                        break;

                    case "BLOCO_CODIGO":
                        if (partes.length >= 4 && aulaAtual != null) {
                            int ordem = Integer.parseInt(partes[1]);
                            String linguagem = partes[2];
                            String codigo = partes[3];

                            BlocoCodigo bloco = new BlocoCodigo(ordem, linguagem, codigo);
                            aulaAtual.adicionarBloco(bloco);
                        }
                        break;

                    case "BLOCO_IMAGEM":
                        if (partes.length >= 4 && aulaAtual != null) {
                            int ordem = Integer.parseInt(partes[1]);
                            String caminho = partes[2];
                            String descricaoAlt = partes[3];

                            BlocoImagem bloco = new BlocoImagem(ordem, caminho, descricaoAlt);
                            aulaAtual.adicionarBloco(bloco);
                        }
                        break;

                    case "QUIZ":
                        if (partes.length >= 3 && aulaAtual != null) {
                            String id = partes[1];
                            int notaMinima = Integer.parseInt(partes[2]);

                            quizAtual = new Quiz(notaMinima);
                            aulaAtual.definirQuiz(quizAtual);
                            quizzesMap.put(id, quizAtual);
                        }
                        break;

                    case "QUESTAO_UMA_ESCOLHA":
                        if (partes.length >= 5 && quizAtual != null) {
                            String enunciado = partes[1];
                            double peso = Double.parseDouble(partes[2]);
                            int indiceCorreto = Integer.parseInt(partes[3]);

                            questaoAtual = new QUmaEscolha(enunciado, peso);
                            // Setar indiceCorreto se houver setter
                            quizAtual.adicionarQuestao(questaoAtual);
                        }
                        break;

                    case "QUESTAO_MULTIPLA_SELECAO":
                        if (partes.length >= 4 && quizAtual != null) {
                            String enunciado = partes[1];
                            double peso = Double.parseDouble(partes[2]);

                            questaoAtual = new QMultiplaSelecao(enunciado, peso);
                            quizAtual.adicionarQuestao(questaoAtual);
                        }
                        break;

                    case "OPCAO":
                        if (partes.length >= 3 && questaoAtual != null) {
                            String texto = partes[1];
                            boolean correta = Boolean.parseBoolean(partes[2]);

                            // Adicionar opção à questão (precisa de método apropriado)
                            if (questaoAtual instanceof QUmaEscolha) {
                                ((QUmaEscolha) questaoAtual).adicionarOpcao(texto, correta);
                            } else if (questaoAtual instanceof QMultiplaSelecao) {
                                ((QMultiplaSelecao) questaoAtual).adicionarOpcao(texto, correta);
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de cursos: " + e.getMessage());
            e.printStackTrace();
        }

        return cursos;
    }

    /**
     * Carrega uma lista de alunos a partir de um arquivo TXT.
     *
     * @param arquivo caminho do arquivo contendo os dados dos alunos
     * @return lista de alunos carregados
     */
    public List<Aluno> carregarAlunos(Path arquivo) {
        List<Aluno> alunos = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(arquivo)) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split("\\|");

                if (partes.length >= 5 && "ALUNO".equals(partes[0])) {
                    String id = partes[1];
                    String nome = partes[2];
                    String email = partes[3];
                    String senhaHash = partes[4];

                    Aluno aluno = new Aluno(nome, email);
                    // Aqui você precisaria de um setter para senhaHash se necessário
                    alunos.add(aluno);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de alunos: " + e.getMessage());
            e.printStackTrace();
        }

        return alunos;
    }
}