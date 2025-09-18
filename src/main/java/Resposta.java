import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class Resposta {
    private String valor;

    public Resposta(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public int indiceSelecionado() {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Valor da resposta não é um índice válido: " + valor);
        }
    }

    public Set<Integer> indicesSelecionados() {
        if (valor == null || valor.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Integer> indices = new HashSet<>();
        String[] partes = valor.split(",");
        for (String parte : partes) {
            try {
                indices.add(Integer.parseInt(parte.trim()));
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Valor da resposta contém índice inválido: " + parte);
            }
        }
        return indices;
    }
}