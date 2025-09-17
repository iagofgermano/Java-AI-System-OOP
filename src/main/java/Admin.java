import java.util.UUID;

public class Admin extends Usuario {

    // Construtor
    public Admin(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public Admin(UUID id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }


    public void publicarCurso(Curso curso) {
        if (curso != null) {
            curso.publicar();
        } else {
            throw new IllegalArgumentException("Curso não pode ser nulo.");
        }
    }

    public void despublicarCurso(Curso curso) {
        if (curso != null) {
            curso.despublicar();
        } else {
            throw new IllegalArgumentException("Curso não pode ser nulo.");
        }
    }
}