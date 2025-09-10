import java.util.*;
import java.time.LocalDateTime;

/**
 * Classe abstrata que representa um usuário no sistema.
 * Contém atributos e métodos comuns a todos os tipos de usuários.
 */
public abstract class Usuario implements AutoCloseable {

    // Atributos
    protected UUID id;
    protected String nome;
    protected String email;
    protected String senhaHash;
    protected LocalDateTime dataRegistro;
    protected boolean ativo;
    protected List<SessaoPlayground> sessoesPlayground;

    /**
     * Construtor da classe Usuario
     *
     * @param nome Nome do usuário
     * @param email Email do usuário
     */
    protected Usuario(String nome, String email) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senhaHash = null;
        this.dataRegistro = LocalDateTime.now();
        this.ativo = true;
        this.sessoesPlayground = new ArrayList<>();
    }

    /**
     * Método abstrato para definir o tipo de usuário
     * Deve ser implementado pelas subclasses
     */
    public abstract String getTipoUsuario();

    /**
     * Método para autenticação do usuário
     *
     * @param senha Senha fornecida para autenticação
     * @return true se a autenticação for bem sucedida, false caso contrário
     */
    public boolean autenticar(String senha) {
        if (senhaHash == null) {
            return false;
        }
        return senhaHash.equals(hashSenha(senha));
    }

    /**
     * Método para definir a senha do usuário
     *
     * @param senha Nova senha
     */
    public void setSenha(String senha) {
        this.senhaHash = hashSenha(senha);
    }

    /**
     * Método para desativar o usuário
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Método para reativar o usuário
     */
    public void reativar() {
        this.ativo = true;
    }

    /**
     * Método para iniciar uma sessão no playground
     *
     * @param duracaoMaxima Duração máxima da sessão em minutos
     * @return A sessão criada
     */
    public SessaoPlayground iniciarSessaoPlayground(int duracaoMaxima) {
        SessaoPlayground sessao = new SessaoPlayground(this, duracaoMaxima);
        sessoesPlayground.add(sessao);
        return sessao;
    }

    /**
     * Método para obter as sessões ativas do usuário
     *
     * @return Lista de sessões ativas
     */
    public List<SessaoPlayground> getSessoesAtivas() {
        List<SessaoPlayground> ativas = new ArrayList<>();
        for (SessaoPlayground sessao : sessoesPlayground) {
            if (sessao.isAtiva()) {
                ativas.add(sessao);
            }
        }
        return ativas;
    }

    /**
     * Método auxiliar para hash de senha
     *
     * @param senha Senha a ser hasheada
     * @return Hash da senha
     */
    protected String hashSenha(String senha) {
        // Implementação simples de hash - em produção use bcrypt, scrypt ou similar
        return Integer.toString(senha.hashCode());
    }

    /**
     * Implementação do método close() da interface AutoCloseable
     * Encerra todas as sessões ativas do usuário
     */
    @Override
    public void close() {
        System.out.println("Encerrando sessões do usuário: " + this.nome);

        // Encerrar todas as sessões ativas
        for (SessaoPlayground sessao : getSessoesAtivas()) {
            try {
                sessao.encerrar();
            } catch (Exception e) {
                System.err.println("Erro ao encerrar sessão: " + e.getMessage());
            }
        }

        System.out.println("Todas as sessões do usuário " + this.nome + " foram encerradas.");
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<SessaoPlayground> getSessoesPlayground() {
        return new ArrayList<>(sessoesPlayground); // Retorna cópia para proteger o encapsulamento
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataRegistro=" + dataRegistro +
                ", ativo=" + ativo +
                ", tipo=" + getTipoUsuario() +
                '}';
    }
}