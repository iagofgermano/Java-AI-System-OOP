import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Parametros {
    private Map<String, String> valores;

    // Construtor
    public Parametros() {
        this.valores = new HashMap<>();
    }

    // Método para adicionar um par chave-valor
    public Parametros put(String chave, String valor) {
        this.valores.put(chave, valor);
        return this; // Permite encadeamento
    }

    // Método para obter um valor opcional com base na chave
    public Optional<String> get(String chave) {
        return Optional.ofNullable(this.valores.get(chave));
    }

    // Método para obter um valor como inteiro
    public int getInt(String chave) {
        String valor = this.valores.get(chave);
        if (valor == null || valor.isEmpty()) {
            throw new NumberFormatException("Valor não encontrado ou vazio para a chave: " + chave);
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Falha ao converter o valor '" + valor + "' da chave '" + chave + "' para inteiro.");
        }
    }
}