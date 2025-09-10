import java.util.ArrayList;
import java.util.List;

public class Aluno extends Usuario {
    private String matricula;
    private List<Inscricao> inscricoes;
    private List<InsigniaDoUsuario> insignias;

    public Aluno(String nome, String email, String senha, String matricula) {
        super(nome, email);
        this.matricula = matricula;
        this.inscricoes = new ArrayList<>();
        this.insignias = new ArrayList<>();
    }

    // Getters
    public String getMatricula() { return matricula; }
    public List<Inscricao> getInscricoes() { return new ArrayList<>(inscricoes); }
    public List<InsigniaDoUsuario> getInsignias() { return new ArrayList<>(insignias); }

    // Métodos
    public void adicionarInscricao(Inscricao inscricao) {
        inscricoes.add(inscricao);
    }

    public void adicionarInsignia(InsigniaDoUsuario insignia) {
        insignias.add(insignia);
    }

    public String getTipoUsuario(){
        return "Aluno";
    }
}