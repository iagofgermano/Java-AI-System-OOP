import java.util.HashMap;
import java.util.Map;

public class Resultado {
    private String resumo;
    private Map<String, String> metricas;

    public Resultado(String resumo) {
        this.resumo = resumo;
        this.metricas = new HashMap<>();
    }

    public void adicionarMetrica(String nome, String valor) {
        metricas.put(nome, valor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resultado{");
        sb.append("resumo='").append(resumo).append('\'');
        sb.append(", metricas=").append(metricas);
        sb.append('}');
        return sb.toString();
    }
}