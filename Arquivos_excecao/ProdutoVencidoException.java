// Arquivo: ProdutoVencidoException.java
public class ProdutoVencidoException extends Exception {
    
    // Construtor que recebe a mensagem de erro personalizada
    public String ProdutoVencidoException(String mensagem) {
        // Passa a mensagem para a classe mãe (Exception)
        super(mensagem);
    }
}