package model;

import exception.EstoqueInsuficienteException;
import exception.ProdutoVencidoException;
import interfaces.CalculadoraDesconto;

public class ItemVenda {

    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda(Produto produto, int quantidade)
            throws EstoqueInsuficienteException, ProdutoVencidoException {

        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (produto instanceof ProdutoAlimenticio) {
            ProdutoAlimenticio alimenticio = (ProdutoAlimenticio) produto;
            if (alimenticio.estaVencido()) {
                throw new ProdutoVencidoException(
                        "O produto \"" + produto.getNome() + "\" está vencido desde "
                                + alimenticio.getDataValidade() + " e não pode ser adicionado à venda.");
            }
        }

        if (!produto.temEstoqueSuficiente(quantidade)) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o produto \"" + produto.getNome()
                            + "\". Disponível: " + produto.getQuantidadeEstoque()
                            + " | Solicitado: " + quantidade);
        }

        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
        this.produto.setQuantidadeAtual(quantidade);
    }

    public double calcularSubtotal() {
        double valorBruto = precoUnitario * quantidade;

        if (produto instanceof CalculadoraDesconto) {
            CalculadoraDesconto calc = (CalculadoraDesconto) produto;
            return calc.calcularDesconto(valorBruto);
        }

        return valorBruto;
    }

    public int getPercentualDesconto() {
        if (produto instanceof CalculadoraDesconto) {
            return ((CalculadoraDesconto) produto).getPercentualDesconto();
        }
        return 0;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    @Override
    public String toString() {
        int desconto = getPercentualDesconto();
        String descontoStr = desconto > 0 ? String.format(" (-%d%%)", desconto) : "";
        return String.format("%s x%d @ R$ %.2f%s = R$ %.2f",
                produto.getNome(), quantidade, precoUnitario, descontoStr, calcularSubtotal());
    }
}
