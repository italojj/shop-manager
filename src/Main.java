import menu.MenuPrincipal;
import model.Cliente;
import model.Fornecedor;
import model.Funcionario;
import model.ItemVenda;
import model.Produto;
import model.Venda;
import exception.EntidadeNaoEncontradaException;
import repository.GerenciadorDados;

public class Main {
    public static void main(String[] args) {
        GerenciadorDados<Cliente> clientes = new GerenciadorDados<>(Cliente.class, "clientes", Cliente::getIdCliente);
        GerenciadorDados<Funcionario> funcionarios = new GerenciadorDados<>(Funcionario.class, "funcionarios",
                Funcionario::getIdFuncionario);
        GerenciadorDados<Produto> produtos = new GerenciadorDados<>(Produto.class, "produtos", Produto::getId);
        GerenciadorDados<Fornecedor> fornecedores = new GerenciadorDados<>(Fornecedor.class, "fornecedores",
                Fornecedor::getId);
        GerenciadorDados<Venda> vendas = new GerenciadorDados<>(Venda.class, "vendas", Venda::getIdVenda);

        clientes.carregar();
        funcionarios.carregar();
        produtos.carregar();
        fornecedores.carregar();
        vendas.carregar();

        // Re-vincula os itens das vendas aos produtos do catálogo: o Gson
        // desserializa uma CÓPIA do produto dentro de cada venda, então sem isto
        // a baixa de estoque ao finalizar uma venda carregada não chegaria ao catálogo.
        for (Venda venda : vendas.listarTodos()) {
            for (ItemVenda item : venda.getItens()) {
                try {
                    item.setProduto(produtos.buscarPorId(item.getProduto().getId()));
                } catch (EntidadeNaoEncontradaException e) {
                    // Produto removido do catálogo: mantém a cópia embutida na venda.
                }
            }
        }

        MenuPrincipal menu = new MenuPrincipal(clientes, funcionarios, produtos, fornecedores, vendas);
        menu.iniciar();

        clientes.salvar();
        funcionarios.salvar();
        produtos.salvar();
        fornecedores.salvar();
        vendas.salvar();
        System.out.println("Dados salvos em data/.");
    }
}