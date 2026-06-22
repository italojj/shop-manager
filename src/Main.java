import menu.MenuPrincipal;
import model.Cliente;
import model.Fornecedor;
import model.Funcionario;
import model.Produto;
import model.Venda;
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