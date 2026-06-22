package exception;

public class ProdutoVencidoException extends Exception {

    public ProdutoVencidoException(String mensagem) {
        super(mensagem);
    }

    public ProdutoVencidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
