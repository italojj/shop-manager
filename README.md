# ShopManager

Sistema de gerenciamento de loja desenvolvido em **Java** como trabalho avaliativo da disciplina de **Linguagem de Programação**.

A aplicação cobre os principais conceitos de Programação Orientada a Objetos — herança, encapsulamento, polimorfismo, exceções personalizadas, estado dinâmico e persistência em arquivos — dentro de um contexto real de gestão comercial.

---

## Grupo

| Membro | GitHub |
|---|---|
| Italo | [@italojj](https://github.com/italojj) |
| Jonas | — |
| William | — |

---

## Como compilar e rodar

### Pré-requisitos

- Java 17 ou superior
- Gson (incluído na pasta `lib/`)

### Clonar o repositório

```bash
git clone https://github.com/italojj/shop-manager.git
cd shop-manager
```

### Compilar

```bash
javac -cp lib/gson.jar -d out src/**/*.java
```

### Executar

```bash
java -cp out:lib/gson.jar Main
```

> No Windows, substitua `:` por `;` no classpath:
> ```bash
> java -cp out;lib/gson.jar Main
> ```

---

## Estrutura do projeto

```
shop-manager/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Pessoa.java
│   │   ├── Cliente.java
│   │   ├── Funcionario.java
│   │   ├── Vendedor.java
│   │   ├── Gerente.java
│   │   ├── Produto.java
│   │   ├── ProdutoEletronico.java
│   │   ├── ProdutoAlimenticio.java
│   │   ├── Fornecedor.java
│   │   ├── ItemVenda.java
│   │   └── Venda.java
│   ├── enums/
│   │   └── StatusVenda.java
│   ├── exception/
│   │   ├── EstoqueInsuficienteException.java
│   │   ├── ProdutoVencidoException.java
│   │   ├── CpfDuplicadoException.java
│   │   ├── TransicaoEstadoInvalidaException.java
│   │   └── EntidadeNaoEncontradaException.java
│   ├── repository/
│   │   └── GerenciadorDados.java
│   └── menu/
│       └── MenuPrincipal.java
├── data/
│   ├── clientes.json
│   ├── funcionarios.json
│   ├── produtos.json
│   ├── fornecedores.json
│   └── vendas.json
├── lib/
│   └── gson.jar
├── docs/
│   ├── DocumentoDeVisao.md
│   └── DiagramaUML.png
├── .gitignore
└── README.md
```

---

## Modelagem de Classes

O sistema possui 12 classes funcionais organizadas em três hierarquias:

```
Pessoa (abstrata)
├── Cliente
└── Funcionario (abstrata)
    ├── Vendedor
    └── Gerente

Produto (abstrata)
├── ProdutoEletronico
└── ProdutoAlimenticio
```

O diagrama UML completo está em [`docs/DiagramaUML.png`](docs/DiagramaUML.png).

---

## Funcionalidades

- CRUD de Clientes, Funcionários, Produtos e Fornecedores
- Registro de vendas com múltiplos itens
- Ciclo de vida da venda: `ABERTA -> FINALIZADA -> CANCELADA`
- Desconto progressivo por quantidade em produtos eletrônicos
- Bloqueio de venda de produtos alimentícios vencidos
- Cálculo de comissão (Vendedor) e bônus (Gerente)
- Persistência automática em arquivos JSON
- Carregamento dos dados ao iniciar a aplicação

---

## Regras de Negócio

| ID | Descrição |
|---|---|
| RN-01 | Venda bloqueada se estoque for insuficiente |
| RN-02 | Venda bloqueada para produtos alimentícios vencidos |
| RN-03 | Transição de estado da venda segue fluxo definido |
| RN-04 | Remuneração calculada de forma diferente para Vendedor e Gerente |
| RN-05 | Desconto progressivo por quantidade em eletrônicos |
| RN-06 | CPF duplicado é rejeitado no cadastro |

---

## Persistência

Os dados são salvos automaticamente em arquivos `.json` na pasta `data/` ao encerrar a aplicação e recarregados ao iniciar. A serialização é feita com a biblioteca **Gson**.

---

## Documentação

O documento de visão completo está em [`docs/DocumentoDeVisao.md`](docs/DocumentoDeVisao.md).

---

## Disciplina

Linguagem de Programação — Atividade Unidade 03  
Professor: Jefferson Gomes Dutra
