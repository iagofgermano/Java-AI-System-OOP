import java.util.*;

public class Modulo {
    private UUID id;
    private int ordem;
    private String titulo;
    private List<Aula> aulas;

    public Modulo(int ordem, String titulo) {
        this.id = UUID.randomUUID();
        this.ordem = ordem;
        this.titulo = titulo;
        this.aulas = new ArrayList<>();
    }

    public void adicionarAula(Aula a) {
        aulas.add(a);
    }

    public Aula obterAulaPorOrdem(int ordem) {
        for (Aula a : aulas) {
            if (a.getOrdem() == ordem) {
                return a;
            }
        }
        return null;
    }

    // Getters
    public UUID getId() { return id; }
    public int getOrdem() { return ordem; }
    public String getTitulo() { return titulo; }
    public List<Aula> getAulas() { return new ArrayList<>(aulas); }
}
