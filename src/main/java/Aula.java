import java.util.ArrayList;
import java.util.List;

public class Aula {
    private String titulo;
    private String descricao;
    private List<BlocoConteudo> blocos;

    public Aula(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.blocos = new ArrayList<>();
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public List<BlocoConteudo> getBlocos() { return new ArrayList<>(blocos); }

    public void adicionarBloco(BlocoConteudo bloco) {
        blocos.add(bloco);
    }
}