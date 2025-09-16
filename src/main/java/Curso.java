import java.util.*;
import java.time.LocalDateTime;

public class Curso {
    private UUID id;
    private String titulo;
    private String descricao;
    private boolean publicado;
    private List<Modulo> modulos;
    private LocalDateTime criadoEm;

    public Curso(String titulo, String descricao) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.publicado = false;
        this.modulos = new ArrayList<>();
        this.criadoEm = LocalDateTime.now();
    }

    public void adicionarModulo(Modulo mod) {
        modulos.add(mod);
    }

    public List<Aula> listarAulas() {
        List<Aula> todas = new ArrayList<>();
        for (Modulo mod : modulos) {
            todas.addAll(mod.getAulas());
        }
        return todas;
    }

    public Modulo obterModuloPorOrdem(int ordem) {
        for (Modulo mod : modulos) {
            if (mod.getOrdem() == ordem) {
                return mod;
            }
        }
        return null;
    }

    public void publicar() {
        this.publicado = true;
    }

    public void despublicar() {
        this.publicado = false;
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public boolean isPublicado() { return publicado; }
    public List<Modulo> getModulos() { return new ArrayList<>(modulos); }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
