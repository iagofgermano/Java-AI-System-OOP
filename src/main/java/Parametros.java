public class Parametros {
    private Map<String, Object> valores;

    public Parametros() {
        this.valores = new HashMap<>();
    }

    public Map<String, Object> getValores() { return valores; }

    public void parametros() {
        // Inicialização dos parâmetros
    }

    public void adicionarChave(String chave, Object valor) {
        valores.put(chave, valor);
    }

    public Optional<Object> getChave(String chave) {
        return Optional.ofNullable(valores.get(chave));
    }

    public Optional<String> getChaveString(String chave) {
        Object valor = valores.get(chave);
        return valor instanceof String ? Optional.of((String) valor) : Optional.empty();
    }

    public int getChaveInt(String chave) {
        Object valor = valores.get(chave);
        return valor instanceof Integer ? (Integer) valor : 0;
    }
}
