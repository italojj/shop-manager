package model;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Cliente extends Pessoa {
    private int idCliente;
    private LocalDate dataCadastro;
    // transient: evita ciclo Cliente <-> Venda na serialização (StackOverflow no Gson).
    private transient List<Venda> historicoCompras = new ArrayList<>();

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Venda> getHistoricoCompras() {
        if (historicoCompras == null) {
            historicoCompras = new ArrayList<>();
        }
        return historicoCompras;
    }

    public void setHistoricoCompras(List<Venda> historicoCompras) {
        this.historicoCompras = historicoCompras;
    }
}