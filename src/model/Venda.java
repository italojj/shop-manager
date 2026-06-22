package model;

import enums.StatusVenda;
import exception.EstoqueInsuficienteException;
import exception.TransicaoEstadoInvalidaException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venda {

    private int idVenda;
    private LocalDateTime dataHora;
    private StatusVenda status;
    private Cliente cliente;
    private Funcionario funcionario;
    private List<ItemVenda> itens;

    public Venda(int idVenda, Cliente cliente, Funcionario funcionario) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.dataHora = LocalDateTime.now();
        this.status = StatusVenda.ABERTA;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemVenda item) {
        if (status != StatusVenda.ABERTA) {
            throw new IllegalStateException("Só é possível adicionar itens em vendas com o status Aberta.");
        }
        itens.add(item);
    }

    public void removerItem(ItemVenda item) {
        if (status != StatusVenda.ABERTA) {
            throw new IllegalStateException("Só é possível remover itens em vendas com o status Aberta.");
        }
        itens.remove(item);
    }

    public void finalizar() throws TransicaoEstadoInvalidaException, EstoqueInsuficienteException {
        if (status != StatusVenda.ABERTA) {
            throw new TransicaoEstadoInvalidaException(
                    "Não é possível finalizar uma venda com o status " + status + ".");
        }
        if (itens.isEmpty()) {
            throw new IllegalStateException("Não é possível finalizar uma venda sem itens.");
        }
        for (ItemVenda item : itens) {
            if (!item.getProduto().temEstoqueSuficiente(item.getQuantidade())) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para \"" + item.getProduto().getNome()
                                + "\" ao finalizar a venda.");
            }
        }
        for (ItemVenda item : itens) {
            item.getProduto().baixarEstoque(item.getQuantidade());
        }
        status = StatusVenda.FINALIZADA;
    }

    public void cancelar() throws TransicaoEstadoInvalidaException {
        if (status != StatusVenda.ABERTA) {
            throw new TransicaoEstadoInvalidaException(
                    "Não é possível cancelar uma venda com o status " + status + ".");
        }
        status = StatusVenda.CANCELADA;
    }

    public double getValorTotal() {
        double total = 0;
        for (ItemVenda item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public double calcularTotal() {
        return getValorTotal();
    }

    public int getIdVenda() {
        return idVenda;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }
}