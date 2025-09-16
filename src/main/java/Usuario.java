import java.util.*;
import java.time.LocalDateTime;

/**
 * Classe abstrata que representa um usuário no sistema.
 * Contém atributos e métodos comuns a todos os tipos de usuários.
 */
public abstract class Usuario implements AutoCloseable {

    // Atributos
    protected final UUID id; // id deve ser imutável após a criação
    protected String nome;
    protected String email;
    protected String senhaHash;
    protected final LocalDateTime dataRegistro;
    protected boolean ativo;
    protected final List<SessaoPlayground> sessoesPlayground;

    /**
     * Construtor da classe Usuario
     *
     * @param nome Nome do usuário
     * @param email Email do usuário
     */
    protected Usuario(String nome, String email) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
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
        return senhaHash != null && senhaHash.equals(hashSenha(senha));
    }

    /**
     * Define a senha do usuário, armazenando sua versão hasheada
     *
     * @param senha Nova senha
     */
    public void setSenha(String senha) {
        this.senhaHash = hashSenha(senha);
    }

    /**
     * Desativa o usuário
     */
    public void desativar() {
        this.ativo = false;
    }

    /**
     * Reativa o usuário
     */
    public void reativar() {
        this.ativo = true;
    }

    /**
     * Inicia uma nova sessão no playground
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
     * Obtém todas as sessões ativas do usuário
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
     * Gera um hash simples da senha
     * OBS: Em produção use bcrypt, scrypt ou Argon2.
     *
     * @param senha Senha a ser hasheada
     * @return Hash da senha
     */
    protected String hashSenha(String senha) {
        return Integer.toString(Objects.requireNonNull(senha, "Senha não pode ser nula").hashCode());
    }

    /**
     * Implementação do método close() da interface AutoCloseable
     * Encerra todas as sessões ativas do usuário
     */
    @Override
    public void close() {
        System.out.println("Encerrando sessões do usuário: " + this.nome);

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
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<SessaoPlayground> getSessoesPlayground() {
        return new ArrayList<>(sessoesPlayground); // Cópia defensiva
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
