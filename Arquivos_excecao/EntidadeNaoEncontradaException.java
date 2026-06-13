// Arquivo: EntidadeNaoEncontradaException.java
public class EntidadeNaoEncontradaException extends Exception {
    
    // Construtor que recebe a mensagem de erro personalizada
    public String EntidadeNaoEncontradaException(String mensagem) {
        // Passa a mensagem para a classe mãe (Exception)
        super(mensagem);
    }
}