import java.util.Objects;
import java.util.UUID;

/**
 * Classe abstrata Usuario (modelada conforme UML)
 */
public abstract class Usuario {

    private final UUID id;
    private String nome;
    private String email;
    private String senhaHash;    // privado conforme UML
    private boolean autenticado; // flag de autenticação

    /**
     * Construtor protegido conforme diagrama (#Usuario(...))
     */
    protected Usuario(String nome, String email) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.senhaHash = null;
        this.autenticado = false;
    }

    /**
     * Tenta autenticar este usuário usando email + senha.
     * Se o email informado não for o do usuário, falha (autenticado = false).
     * Método tem retorno void conforme UML — resultado é guardado em 'autenticado'.
     */
    public void autenticar(String email, String senha) {
        if (!Objects.equals(this.email, email) || senha == null || senha.isEmpty() || this.senhaHash == null) {
            this.autenticado = false;
            return;
        }
        this.autenticado = this.senhaHash.equals(hashSenha(senha));
    }

    /**
     * Desloga o usuário (marca como não autenticado)
     */
    public void sair() {
        this.autenticado = false;
    }

    /**
     * Getter do id (conforme UML: getId(): UUID)
     */
    public UUID getId() {
        return id;
    }

    /* ---------- Helpers protegidos (não expostos no UML) ---------- */

    /**
     * Permite subclasses definirem a senha (armazenada como hash).
     * Mantido protegido para não ser público.
     */
    protected void setSenha(String senha) {
        this.senhaHash = hashSenha(senha);
    }

    /**
     * Função simples de hash (exemplo). Em produção, use bcrypt/Argon2/etc.
     */
    protected String hashSenha(String senha) {
        return Integer.toString(Objects.requireNonNull(senha).hashCode());
    }

    /* ---------- Getters/setters auxiliares (opc.) ---------- */

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email);
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", autenticado=" + autenticado +
                '}';
    }
}
