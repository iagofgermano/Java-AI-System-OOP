import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Classe que representa uma sessão no playground de programação.
 * Permite aos usuários praticar e executar código em um ambiente controlado.
 */
public class SessaoPlayground {

    // Atributos
    private String id;
    private Usuario usuario;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int duracaoMaxima; // em minutos
    private boolean ativa;
    private String codigoAtual;
    private String resultadoExecucao;

    /**
     * Construtor da classe SessaoPlayground
     *
     * @param usuario Usuário dono da sessão
     * @param duracaoMaxima Duração máxima da sessão em minutos
     */
    public SessaoPlayground(Usuario usuario, int duracaoMaxima) {
        this.id = gerarIdUnico();
        this.usuario = usuario;
        this.dataInicio = LocalDateTime.now();
        this.dataFim = null;
        this.duracaoMaxima = duracaoMaxima;
        this.ativa = true;
        this.codigoAtual = "";
        this.resultadoExecucao = "";

        // Iniciar thread para monitorar tempo da sessão
        iniciarMonitoramentoTempo();
    }

    /**
     * Gera um ID único para a sessão
     *
     * @return ID único da sessão
     */
    private String gerarIdUnico() {
        return "SP-" + System.currentTimeMillis() + "-" +
                String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * Inicia uma thread para monitorar o tempo da sessão
     */
    private void iniciarMonitoramentoTempo() {
        Thread monitor = new Thread(() -> {
            try {
                Thread.sleep(duracaoMaxima * 60 * 1000L); // Converter minutos para milissegundos
                if (ativa) {
                    encerrar();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }

    /**
     * Executa o código atual no playground
     *
     * @param codigo Código a ser executado
     * @return Resultado da execução
     */
    public String executarCodigo(String codigo) {
        if (!ativa) {
            throw new IllegalStateException("Sessão não está ativa");
        }

        this.codigoAtual = codigo;
        this.resultadoExecucao = simularExecucao(codigo);

        return resultadoExecucao;
    }

    /**
     * Simula a execução do código (em ambiente real, isso seria uma chamada ao interpretador)
     *
     * @param codigo Código a ser executado
     * @return Resultado simulado da execução
     */
    private String simularExecucao(String codigo) {
        // Simulação simples - em ambiente real, isso conectaria ao interpretador
        StringBuilder resultado = new StringBuilder();
        resultado.append("=== Execução iniciada em ").append(LocalDateTime.now()).append(" ===\n");
        resultado.append("Código executado:\n").append(codigo).append("\n");
        resultado.append("--- Resultado ---\n");

        // Simulação básica de processamento
        if (codigo.contains("print") || codigo.contains("console.log")) {
            resultado.append("Olá, Mundo! (simulação)\n");
        } else if (codigo.contains("erro") || codigo.contains("exception")) {
            resultado.append("Erro: Exceção simulada\n");
        } else {
            resultado.append("Execução concluída com sucesso\n");
        }

        resultado.append("=== Execução finalizada ===\n");
        return resultado.toString();
    }

    /**
     * Encerra a sessão
     */
    public void encerrar() {
        if (ativa) {
            this.dataFim = LocalDateTime.now();
            this.ativa = false;
            System.out.println("Sessão " + id + " encerrada para o usuário " +
                    usuario.getNome());
        }
    }

    /**
     * Prolonga a sessão por mais minutos
     *
     * @param minutosAdicionais Minutos a serem adicionados
     */
    public void prolongar(int minutosAdicionais) {
        if (!ativa) {
            throw new IllegalStateException("Não é possível prolongar uma sessão inativa");
        }

        this.duracaoMaxima += minutosAdicionais;
        System.out.println("Sessão " + id + " prolongada por " + minutosAdicionais + " minutos");
    }

    /**
     * Verifica se a sessão está expirada
     *
     * @return true se a sessão estiver expirada, false caso contrário
     */
    public boolean isExpirada() {
        if (dataFim != null) {
            return true; // Sessão já encerrada
        }

        Duration duracaoAtual = Duration.between(dataInicio, LocalDateTime.now());
        return duracaoAtual.toMinutes() >= duracaoMaxima;
    }

    /**
     * Obtém o tempo restante da sessão em minutos
     *
     * @return Tempo restante em minutos, ou 0 se expirada
     */
    public long getTempoRestanteMinutos() {
        if (!ativa || isExpirada()) {
            return 0;
        }

        Duration duracaoAtual = Duration.between(dataInicio, LocalDateTime.now());
        long minutosPassados = duracaoAtual.toMinutes();
        return Math.max(0, duracaoMaxima - minutosPassados);
    }

    // Getters

    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public boolean isAtiva() {
        return ativa && !isExpirada();
    }

    public String getCodigoAtual() {
        return codigoAtual;
    }

    public String getResultadoExecucao() {
        return resultadoExecucao;
    }

    @Override
    public String toString() {
        return "SessaoPlayground{" +
                "id='" + id + '\'' +
                ", usuario=" + usuario.getNome() +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", duracaoMaxima=" + duracaoMaxima +
                ", ativa=" + isAtiva() +
                ", tempoRestante=" + getTempoRestanteMinutos() + " minutos" +
                '}';
    }
}