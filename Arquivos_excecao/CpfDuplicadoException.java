// Arquivo: CpfDuplicadoException.java
public class CpfDuplicadoException extends Exception {
    
    // Construtor que recebe a mensagem de erro personalizada
    public String CpfDuplicadoException(String mensagem) {
        // Passa a mensagem para a classe mãe (Exception)
        super(mensagem);
    }
}