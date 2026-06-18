package exception;

public class EstoqueInsuficienteException extends Exception {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }

    public EstoqueInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
