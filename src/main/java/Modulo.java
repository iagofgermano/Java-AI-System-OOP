import java.util.*;
import java.time.LocalDateTime;

/**
 * Classe que representa um módulo dentro de um curso.
 * Contém aulas relacionadas a um tema específico.
 */
public class Modulo {

    // Atributos
    private String id;
    private String titulo;
    private String descricao;
    private int ordem; // Ordem do módulo no curso
    private int cargaHoraria; // em horas
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<Aula> aulas;
    private Curso curso; // Referência ao curso pai

    /**
     * Construtor da classe Modulo
     *
     * @param titulo Título do módulo
     * @param descricao Descrição do módulo
     * @param cargaHoraria Carga horária do módulo em horas
     */
    public Modulo(String titulo, String descricao, int cargaHoraria) {
        this.id = gerarIdUnico();
        this.titulo = titulo != null ? titulo : "Módulo sem título";
        this.descricao = descricao != null ? descricao : "";
        this.ordem = 0;
        this.cargaHoraria = Math.max(0, cargaHoraria);
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.aulas = new ArrayList<>();
        this.curso = null;
    }

    /**
     * Construtor da classe Modulo com ordem específica
     *
     * @param titulo Título do módulo
     * @param descricao Descrição do módulo
     * @param cargaHoraria Carga horária do módulo em horas
     * @param ordem Ordem do módulo no curso
     */
    public Modulo(String titulo, String descricao, int cargaHoraria, int ordem) {
        this(titulo, descricao, cargaHoraria);
        this.ordem = ordem;
    }

    /**
     * Gera um ID único para o módulo
     *
     * @return ID único do módulo
     */
    private String gerarIdUnico() {
        return "MOD-" + System.currentTimeMillis() + "-" +
                String.format("%04d", (int)(Math.random() * 10000));
    }

    /**
     * Adiciona uma aula ao módulo
     *
     * @param aula Aula a ser adicionada
     * @return true se adicionada com sucesso, false caso contrário
     */
    public boolean adicionarAula(Aula aula) {
        if (aula == null) {
            return false;
        }

        if (!aulas.contains(aula)) {
            aulas.add(aula);
            atualizarCargaHoraria();
            this.dataAtualizacao = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Remove uma aula do módulo
     *
     * @param aula Aula a ser removida
     * @return true se removida com sucesso, false caso contrário
     */
    public boolean removerAula(Aula aula) {
        boolean removida = aulas.remove(aula);
        if (removida) {
            atualizarCargaHoraria();
            this.dataAtualizacao = LocalDateTime.now();
        }
        return removida;
    }

    /**
     * Adiciona uma aula em uma posição específica
     *
     * @param aula Aula a ser adicionada
     * @param indice Posição onde a aula será inserida
     * @return true se adicionada com sucesso, false caso contrário
     */
    public boolean adicionarAula(Aula aula, int indice) {
        if (aula == null || indice < 0 || indice > aulas.size()) {
            return false;
        }

        if (!aulas.contains(aula)) {
            aulas.add(indice, aula);
            atualizarCargaHoraria();
            this.dataAtualizacao = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Atualiza a carga horária do módulo com base nas aulas
     */
    private void atualizarCargaHoraria() {
        int cargaTotal = 0;
        for (Aula aula : aulas) {
            cargaTotal += aula.getDuracao(); // assumindo que duração está em horas
        }
        this.cargaHoraria = cargaTotal;
    }

    /**
     * Obtém o número total de aulas no módulo
     *
     * @return Número total de aulas
     */
    public int getTotalAulas() {
        return aulas.size();
    }

    /**
     * Verifica se o módulo está completo (tem aulas)
     *
     * @return true se o módulo tem aulas, false caso contrário
     */
    public boolean isCompleto() {
        return !aulas.isEmpty();
    }

    /**
     * Obtém uma aula específica pelo índice
     *
     * @param indice Índice da aula
     * @return Aula na posição especificada, ou null se índice inválido
     */
    public Aula getAula(int indice) {
        if (indice >= 0 && indice < aulas.size()) {
            return aulas.get(indice);
        }
        return null;
    }

    /**
     * Obtém a próxima aula após uma aula específica
     *
     * @param aulaAtual Aula atual
     * @return Próxima aula, ou null se for a última
     */
    public Aula getProximaAula(Aula aulaAtual) {
        int indice = aulas.indexOf(aulaAtual);
        if (indice >= 0 && indice < aulas.size() - 1) {
            return aulas.get(indice + 1);
        }
        return null;
    }

    /**
     * Obtém a aula anterior a uma aula específica
     *
     * @param aulaAtual Aula atual
     * @return Aula anterior, ou null se for a primeira
     */
    public Aula getAulaAnterior(Aula aulaAtual) {
        int indice = aulas.indexOf(aulaAtual);
        if (indice > 0) {
            return aulas.get(indice - 1);
        }
        return null;
    }

    /**
     * Reordena as aulas do módulo
     *
     * @param novaOrdem Lista com a nova ordem das aulas
     * @return true se reordenado com sucesso, false caso contrário
     */
    public boolean reordenarAulas(List<Aula> novaOrdem) {
        if (novaOrdem == null || novaOrdem.size() != aulas.size()) {
            return false;
        }

        // Verificar se todas as aulas existem no módulo
        for (Aula aula : novaOrdem) {
            if (!aulas.contains(aula)) {
                return false;
            }
        }

        this.aulas = new ArrayList<>(novaOrdem);
        this.dataAtualizacao = LocalDateTime.now();
        return true;
    }

    /**
     * Calcula o progresso do módulo para um aluno específico
     *
     * @param aluno Aluno para calcular o progresso
     * @return Progresso em porcentagem (0-100)
     */
    public double getProgresso(Aluno aluno) {
        if (aluno == null || aulas.isEmpty()) {
            return 0.0;
        }

        int totalAulas = aulas.size();
        int aulasConcluidas = 0;

        for (Aula aula : aulas) {
            Progresso progresso = aluno.obterProgresso(aula);
            if (progresso != null && progresso.isConcluido()) {
                aulasConcluidas++;
            }
        }

        return (double) aulasConcluidas / totalAulas * 100.0;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo != null ? titulo : "Módulo sem título";
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao : "";
        this.dataAtualizacao = LocalDateTime.now();
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = Math.max(0, cargaHoraria);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public List<Aula> getAulas() {
        return new ArrayList<>(aulas); // Retorna cópia para proteger encapsulamento
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
        this.dataAtualizacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Modulo{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ordem=" + ordem +
                ", cargaHoraria=" + cargaHoraria + "h" +
                ", totalAulas=" + getTotalAulas() +
                ", completo=" + isCompleto() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Modulo modulo = (Modulo) obj;
        return Objects.equals(id, modulo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}