import java.util.*;

public class Aluno extends Usuario {
    public final Map<UUID, Progresso> progressoPorAula;
    public final List<InsigniaDoUsuario> insignias;

    public Aluno(String nome, String email) {
        super(nome, email);
        this.progressoPorAula = new HashMap<>();
        this.insignias = new ArrayList<>();
    }

    public void iniciarTrilha(Curso curso) {
        // Verifica se o curso está publicado antes de permitir a inscrição
        if (!curso.isPublicado()) {
            throw new IllegalStateException("Não é possível iniciar trilha em curso não publicado.");
        }

        // Para cada módulo do curso
        for (Modulo modulo : curso.getModulos()) {
            // Para cada aula do módulo
            for (Aula aula : modulo.getAulas()) {
                // Cria um novo registro de progresso para o aluno nesta aula, se ainda não existir
                if (!progressoPorAula.containsKey(aula.getId())) {
                    Progresso progresso = new Progresso(this, aula);
                    progressoPorAula.put(aula.getId(), progresso);
                }
            }
        }

        // Opcional: registrar uma inscrição formal no curso (se não existir)
        // Isso pode ser feito no GestorAcademico, mas aqui garantimos que o aluno esteja "inscrito"
    }

    public Progresso obterProgresso(Aula aula) {
        Progresso progresso = progressoPorAula.get(aula.getId());
        if (progresso == null) {
            // Se não existe progresso registrado, cria um novo com status NOT_STARTED
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


    // Métodos auxiliares para manipulação interna (não especificados no diagrama)
    protected void adicionarProgresso(Progresso progresso) {
        progressoPorAula.put(progresso.getAula().getId(), progresso);
    }

    protected void adicionarInsignia(InsigniaDoUsuario insignia) {
        insignias.add(insignia);
    }

    public List<InsigniaDoUsuario> getInsignias() {
        return new ArrayList<>(insignias); // Retorna cópia defensiva
    }
}