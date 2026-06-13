// Arquivo: EstoqueInsuficienteException.java
public class EstoqueInsuficienteException extends Exception {
    
    // Construtor que recebe a mensagem de erro personalizada
    public String EstoqueInsuficienteException(String mensagem) {
        // Passa a mensagem para a classe mãe (Exception)
        super(mensagem);
    }
}