import java.util.*;

public class Aluno extends Usuario {
    private Map<UUID, Progresso> progressoPorAula;
    private List<InsigniaDoUsuario> insignias;

    public Aluno(String nome, String email) {
        super(nome, email);
        this.progressoPorAula = new HashMap<>();
        this.insignias = new ArrayList<>();
    }

    public void iniciarTrilha(Curso curso) {
        // Implementação para iniciar uma trilha de aprendizado
        // Isso pode incluir criar inscrições, inicializar progresso, etc.
    }

    public Progresso obterProgresso(Aula aula) {
        return progressoPorAula.get(aula.getId());
    }

    // Métodos auxiliares para manipulação interna (não especificados no diagrama)
    protected void adicionarProgresso(Progresso progresso) {
        progressoPorAula.put(progresso.getAula().getId(), progresso);
    }

    protected void adicionarInsignia(InsigniaDoUsuario insignia) {
        insignias.add(insignia);
    }

    // Getters
    public Map<UUID, Progresso> getProgressoPorAula() {
        return new HashMap<>(progressoPorAula); // Retorna cópia defensiva
    }

    public List<InsigniaDoUsuario> getInsignias() {
        return new ArrayList<>(insignias); // Retorna cópia defensiva
    }
}