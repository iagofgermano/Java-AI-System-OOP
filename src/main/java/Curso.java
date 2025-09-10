import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String titulo;
    private String descricao;
    private List<Modulo> modulos;

    public Curso(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.modulos = new ArrayList<>();
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public List<Modulo> getModulos() { return new ArrayList<>(modulos); }

    public void adicionarModulo(Modulo modulo) {
        modulos.add(modulo);
    }
}