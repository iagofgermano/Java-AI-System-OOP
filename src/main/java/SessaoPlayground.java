import java.util.*;

public class SessaoPlayground implements AutoCloseable {
    private Aluno aluno;
    private TarefaIA tarefa;
    private Resultado ultimoResultado;
    private boolean ativa;
    private Scanner scanner; // interação simples via console

    public SessaoPlayground(Aluno aluno, TarefaIA tarefa) {
        this.aluno = aluno;
        this.tarefa = tarefa;
        this.ultimoResultado = null;
        this.ativa = false;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        if (!ativa) {
            this.ativa = true;
            System.out.println("Sessão iniciada para o aluno: " + aluno.getNome());
        } else {
            System.out.println("A sessão já está ativa.");
        }
    }

    public Resultado executar(Parametros parametros) {
        if (!ativa) {
            throw new IllegalStateException("A sessão não foi iniciada.");
        }

        this.ultimoResultado = tarefa.executar(parametros);
        System.out.println("Execução concluída. Resultado: " + ultimoResultado.toString());
        return this.ultimoResultado;
    }

    public void interagirComOpcoes(List<Opcao> opcoes) {
        if (!ativa) {
            throw new IllegalStateException("A sessão não foi iniciada.");
        }

        System.out.println("\nEscolha uma das opções:");
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i + 1) + ". " + opcoes.get(i).getTexto());
        }

        int escolha = scanner.nextInt();
        scanner.nextLine(); // limpar buffer

        if (escolha < 1 || escolha > opcoes.size()) {
            System.out.println("❌ Opção inválida.");
            return;
        }

        Opcao escolhida = opcoes.get(escolha - 1);
        System.out.println("👉 Você escolheu: " + escolhida.getTexto());

        Parametros params = new Parametros();

        switch (escolhida.getTexto()) {
            case "Usar dataset pequeno" -> {
                System.out.print("🔹 Digite o texto para classificar: ");
                params.put("texto", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Qual é o tamanho máximo de registros? ");
                params.put("tamanhoMax", scanner.nextLine());

                System.out.print("🔹 Deseja balancear as classes (sim/nao)? ");
                params.put("balancear", scanner.nextLine());

                System.out.print("🔹 Qual algoritmo usar (SVM, Árvore, Regressão)? ");
                params.put("algoritmo", scanner.nextLine());
            }
            case "Usar dataset grande" -> {
                System.out.print("🔹 Digite o texto para classificar: ");
                params.put("texto", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Quantos registros deseja processar? ");
                params.put("numRegistros", scanner.nextLine());

                System.out.print("🔹 Deseja usar GPU (sim/nao)? ");
                params.put("gpu", scanner.nextLine());

                System.out.print("🔹 Qual limite de tempo em segundos? ");
                params.put("timeout", scanner.nextLine());
            }
            case "Testar com dado fictício" -> {
                System.out.print("🔹 Digite o texto para classificar: ");
                params.put("texto", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Qual valor deseja simular para a entrada? ");
                params.put("entradaSimulada", scanner.nextLine());

                System.out.print("🔹 Deve gerar ruído aleatório (sim/nao)? ");
                params.put("ruido", scanner.nextLine());

                System.out.print("🔹 Quantas repetições do teste? ");
                params.put("repeticoes", scanner.nextLine());
            }
            case "Gerar resposta curta" -> {
                System.out.print("🔹 Digite a pergunta para a IA: ");
                params.put("prompt", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Deseja limitar a resposta a quantas palavras? ");
                params.put("limitePalavras", scanner.nextLine());

                System.out.print("🔹 Deseja permitir emojis (sim/nao)? ");
                params.put("emojis", scanner.nextLine());
            }
            case "Gerar resposta detalhada" -> {
                System.out.print("🔹 Digite a pergunta para a IA: ");
                params.put("prompt", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Deseja incluir exemplos (sim/nao)? ");
                params.put("exemplos", scanner.nextLine());

                System.out.print("🔹 Qual nível de detalhe (baixo/médio/alto)? ");
                params.put("nivelDetalhe", scanner.nextLine());
            }
            case "Gerar resposta criativa" -> {
                System.out.print("🔹 Digite o tema da criação: ");
                params.put("prompt", scanner.nextLine()); // OBRIGATÓRIO

                System.out.print("🔹 Deseja usar rima (sim/nao)? ");
                params.put("rima", scanner.nextLine());

                System.out.print("🔹 Deseja estilo formal ou informal? ");
                params.put("estilo", scanner.nextLine());
            }
            default -> System.out.println("Nenhuma pergunta configurada para esta opção.");
        }

        executar(params);
    }

    @Override
    public void close() {
        if (ativa) {
            this.ativa = false;
            System.out.println("Sessão encerrada para o aluno: " + aluno.getNome());
        }
    }

    public Aluno getAluno() {
        return aluno;
    }
}
