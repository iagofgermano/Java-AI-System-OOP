public class Admin extends Usuario {
    private String nivelPermissao;

    public Admin(String nome, String email, String senha, String nivelPermissao) {
        super(nome, email);
        this.nivelPermissao = nivelPermissao;
    }

    public String getNivelPermissao() {
        return nivelPermissao;
    }

    public void setNivelPermissao(String nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
    }

    @Override
    public String getTipoUsuario() {
        return "Admin";
    }
}