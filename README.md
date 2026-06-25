# NullBank

Projeto da disciplina **DIM0517 - Gerência de Configuração e Mudanças**.

## Equipe

- Allyson Gustavo Silva Do Carmo - [AllysonGustavo1](https://github.com/AllysonGustavo1)
- Gabriel Estacio De Souza Passos - [gabrielestacio](https://github.com/gabrielestacio)
- Jose Ben Hur Nascimento De Oliveira - [Benhurds12](https://github.com/Benhurds12)

## Linguagem e Stack de Desenvolvimento

- Linguagem: Java 21
- Build: Maven
- Interface: Console
- API REST: Spring Boot
- Container: Docker

## Estrutura do Projeto

```
src/main/java/com/nullbank/
├── App.java              # Ponto de entrada da aplicação
├── model/
│   └── Conta.java        # Entidade conta bancária
├── service/
│   └── ContaService.java # Lógica de negócio
└── ui/
    └── NullBankConsole.java # Interface console
```

## Como Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar a API REST
mvn spring-boot:run

# Ou gerar o JAR e executar
mvn clean package
java -jar target/nullbank-3.0.0-SNAPSHOT.jar
```

Para executar a interface console:

```bash
java -jar target/nullbank-3.0.0-SNAPSHOT.jar --console
```

## Como Executar com Docker

```bash
mvn clean package
docker build -t nullbank .
docker run --rm -p 8080:8080 nullbank
```

A API REST ficará disponível em:

```text
http://localhost:8080
```

Imagem no Docker Hub: <https://hub.docker.com/r/allysongustavo/nullbank>

## Exemplos de Endpoints

### Cadastrar conta simples

```bash
curl -X POST http://localhost:8080/banco/conta \
  -H "Content-Type: application/json" \
  -d "{\"numero\":1,\"tipo\":\"simples\",\"saldoInicial\":100.0}"
```

### Cadastrar conta bônus

```bash
curl -X POST http://localhost:8080/banco/conta \
  -H "Content-Type: application/json" \
  -d "{\"numero\":2,\"tipo\":\"bonus\",\"saldoInicial\":0.0}"
```

### Consultar conta

```bash
curl http://localhost:8080/banco/conta/1
```

### Consultar saldo

```bash
curl http://localhost:8080/banco/conta/1/saldo
```

### Creditar valor

```bash
curl -X PUT http://localhost:8080/banco/conta/1/credito \
  -H "Content-Type: application/json" \
  -d "{\"valor\":50.0}"
```

### Debitar valor

```bash
curl -X PUT http://localhost:8080/banco/conta/1/debito \
  -H "Content-Type: application/json" \
  -d "{\"valor\":25.0}"
```

### Transferir valor

```bash
curl -X PUT http://localhost:8080/banco/conta/transferencia \
  -H "Content-Type: application/json" \
  -d "{\"from\":1,\"to\":2,\"amount\":30.0}"
```

### Aplicar rendimento em contas poupança

```bash
curl -X PUT http://localhost:8080/banco/conta/rendimento \
  -H "Content-Type: application/json" \
  -d "{\"taxa\":10.0}"
```

## Funcionalidades

- Cadastrar Conta
- Consultar Saldo
- Crédito
- Débito
- Transferência
