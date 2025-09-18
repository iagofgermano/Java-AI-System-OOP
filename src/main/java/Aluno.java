import java.util.*;

public class Aluno extends Usuario {
    public final Map<UUID, Progresso> progressoPorAula;
    public final List<InsigniaDoUsuario> insignias;
    public final List<Inscricao> inscricoes;

    public Aluno(String nome, String email, String senha) {
        super(nome, email, senha);
        this.progressoPorAula = new HashMap<>();
        this.insignias = new ArrayList<>();
        this.inscricoes = new ArrayList<>();
    }

    public Aluno(UUID id, String nome, String email, String senha) {
        super(id, nome, email, senha);
        this.progressoPorAula = new HashMap<>();
        this.insignias = new ArrayList<>();
        this.inscricoes = new ArrayList<>();
    }
    public void iniciarTrilha(Curso curso) {
        if (!curso.isPublicado()) {
            throw new IllegalStateException("Não é possível iniciar trilha em curso não publicado.");
        }

        for (Modulo modulo : curso.getModulos()) {
            for (Aula aula : modulo.getAulas()) {
                if (!progressoPorAula.containsKey(aula.getId())) {
                    Progresso progresso = new Progresso(this, aula);
                    progressoPorAula.put(aula.getId(), progresso);
                }
            }
        }
    }

    public Progresso obterProgresso(Aula aula) {
        Progresso progresso = progressoPorAula.get(aula.getId());
        if (progresso == null) {
            progresso = new Progresso(this, aula);
            progressoPorAula.put(aula.getId(), progresso);
        }
        return progresso;
    }

    public boolean concluiuAula(Aula aula) {
        if (aula == null) {
            return false;
        }

        Progresso progresso = progressoPorAula.get(aula.getId());
        if (progresso == null) {
            return false;
        }

        return progresso.getStatus() == StatusProgresso.COMPLETED;
    }

    protected void adicionarInsignia(InsigniaDoUsuario insignia) {
        insignias.add(insignia);
    }

    public List<InsigniaDoUsuario> getInsignias() {
        return new ArrayList<>(insignias);
    }
}