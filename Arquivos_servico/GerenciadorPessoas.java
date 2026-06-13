// Arquivo: GerenciadorPessoas.java
import java.util.List;
import java.util.ArrayList;

public class GerenciadorPessoas {

    // Listas privadas para garantir o encapsulamento
    private List<Cliente> clientesCadastrados = new ArrayList<>();
    private List<Funcionario> funcionariosCadastrados = new ArrayList<>();

    public void cadastrarCliente(Cliente novoCliente) throws CpfDuplicadoException {
        
        // Validação de Unicidade usando a lista interna
        for (Cliente cliente : this.clientesCadastrados) {
            if (cliente.getCpf().equals(novoCliente.getCpf())) {
                throw new CpfDuplicadoException("O CPF " + novoCliente.getCpf() + " já está cadastrado no sistema.");
            }
        }
        
        // Se passar pela validação, adiciona na lista interna
        this.clientesCadastrados.add(novoCliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public void cadastrarFuncionario(Funcionario novoFuncionario) throws CpfDuplicadoException {
        
        // Validação de Unicidade usando a lista interna
        for (Funcionario funcionario : this.funcionariosCadastrados) {
            if (funcionario.getCpf().equals(novoFuncionario.getCpf())) {
                throw new CpfDuplicadoException("O CPF " + novoFuncionario.getCpf() + " já está cadastrado no sistema.");
            }
        }
        
        // Se passar pela validação, adiciona na lista interna
        this.funcionariosCadastrados.add(novoFuncionario);
        System.out.println("Funcionário cadastrado com sucesso!");
    }



    public float calcularRemuneracaoFuncionario(Funcionario funcionario, List<Venda> vendas) {
        // Invoca o método polimórfico diretamente do objeto recebido
        return funcionario.calcularRemuneracao(vendas);
    }

    // Métodos Getters caso precise consultar as listas de pessoas
    public List<Cliente> getClientesCadastrados() {
        return clientesCadastrados;
    }

    public List<Funcionario> getFuncionariosCadastrados() {
        return funcionariosCadastrados;
    }
}