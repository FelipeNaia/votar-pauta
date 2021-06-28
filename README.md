Bem vindo ao votar-pauta!

Como configurar o projeto em sua máquina:
Basta rodar 
$ docker-compose up -d
para subir o kafka e o banco de dados postgres
(Caso você já tenha essas portas ocupadas, pode substituir po outras em application.properties e no docker-compose)

Após isso, importar o projeto em uma IDE de sua preferencia, baixar as dependencias do maven e rodar o Sprint


====================================================================================================================

Como utilizar:

Para cadastrar uma pauta (Nome obrigatório): 
PUT em localhost:8081/pauta
body: {
	"nome": "Pauta Teste"
}

Para abrir uma pauta: 
POST em localhost:8081/pauta/{Id da pauta previamente cadastrada}/abrir
body: {
	"tempoEmMinutos": 5
}

Para votar: 
POST em localhost:8081/voto
body: {
	"cpf": "123456789",
	"votoValor": "SIM/NAO",
	"pautaId": {Id da pauta previamente aberta}
}


====================================================================================================================

O que ainda falta implementar:
- Testes unitários
- Não permitir o mesmo CPF ser usado para votar duas vezes
- Validação do CPF pela api user-info do Heroku
- Versionamento da api