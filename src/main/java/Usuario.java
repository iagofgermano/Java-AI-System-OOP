import java.util.Objects;
import java.util.UUID;

/**
 * Classe abstrata Usuario (modelada conforme UML)
 */
public abstract class Usuario {

    protected final UUID id;
    protected String nome;
    protected String email;
    protected String senhaHash;    // privado conforme UML
    protected boolean autenticado; // flag de autenticação

    /**
     * Construtor protegido conforme diagrama (#Usuario(...))
     */
    protected Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.senhaHash = hashSenha(senha);
        this.autenticado = false;
    }

    protected Usuario(UUID id, String nome, String email, String senha) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.senhaHash = senha;
        this.autenticado = false;
    }

    public void autenticar(String email, String senha) {
        try {
            if (!Objects.equals(this.email, email) || senha == null || senha.isEmpty() || this.senhaHash == null) {
                this.autenticado = false;
                throw new Exception("Falha na autenticação");
            }
            this.autenticado = this.senhaHash.equals(hashSenha(senha));
            if (!this.autenticado) {
                throw new Exception("Senha incorreta");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void sair() {
        try {
            if(!this.autenticado) {
                throw new Exception("Usuário não estava logado");
            }
            this.autenticado = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public UUID getId() {
        return id;
    }

    /* ---------- Helpers protegidos (não expostos no UML) ---------- */

    protected void setSenha(String senha) {
        this.senhaHash = hashSenha(senha);
    }

    protected String hashSenha(String senha) {
        return Integer.toString(Objects.requireNonNull(senha).hashCode());
    }

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

    public String getSenhaHash() {
        return senhaHash;
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
