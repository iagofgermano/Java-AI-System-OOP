import java.util.UUID;

public class Admin extends Usuario {

    // Construtor
    public Admin(String nome, String email) {
        super(nome, email);
    }

    // Métodos específicos do Admin

    /**
     * Publica um curso, alterando seu status para publicado.
     *
     * @param curso O curso a ser publicado.
     */
    public void publicarCurso(Curso curso) {
        if (curso != null) {
            curso.publicar();
        } else {
            throw new IllegalArgumentException("Curso não pode ser nulo.");
        }
    }

    /**
     * Despublica um curso, alterando seu status para não publicado.
     *
     * @param curso O curso a ser despublicado.
     */
    public void despublicarCurso(Curso curso) {
        if (curso != null) {
            curso.despublicar();
        } else {
            throw new IllegalArgumentException("Curso não pode ser nulo.");
        }
    }
}