// Arquivo: TransicaoEstadoInvalidaException.java
public class TransicaoEstadoInvalidaException extends Exception {
    
    // Construtor que recebe a mensagem de erro personalizada
    public String TransicaoEstadoInvalidaException(String mensagem) {
        // Passa a mensagem para a classe mãe (Exception)
        super(mensagem);
    }
}