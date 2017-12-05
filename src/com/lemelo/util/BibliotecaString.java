package com.lemelo.util;

import com.lemelo.saida.Saida;

public class BibliotecaString {
    public String urlBanco() {
        //Como conectar no banco.
        //java -classpath hsqldb.jar org.hsqldb.util.DatabaseManager
        //Standalone
        //jdbc:hsqldb:file:E:/Leoci/Projetos Intellij/local-controle-hsqldb-v2/database/db
        //jdbc:hsqldb:file:D:/ProjetosIntellij/local-controle-hsqldb-v2/database/db
        return "jdbc:hsqldb:file:database_v1/db";
    }

    public String createEntradaTable() {
        return "create table if not exists entrada (id integer identity primary key, data_hora varchar(20), descricao varchar(200), valor varchar(30), ultima_edicao varchar(20))";
    }

    public String createSaidaTable() {
        return "create table if not exists saida (id integer identity primary key, data_hora varchar(20), descricao varchar(200), valor varchar(30), ultima_edicao varchar(20))";
    }

    public String saidaSqlInsert(String dataHoraStr, String descricaoStr, String valorStr, String ultimaEdicaoStr) {
        return "insert into saida (data_hora, descricao, valor, ultima_edicao) values ('" + dataHoraStr + "','" + descricaoStr + "','" + valorStr + "', '" + ultimaEdicaoStr + "')";
    }

    public String saidaSqlUpdate(Saida saida, String ultimaEdicaoEditar, Integer idEditar) {
        return "update saida set data_hora = '" + saida.getDataHora() + "', descricao = '" + saida.getDescricao() + "', valor = '" + saida.getValor() + "', ultima_edicao = '" + ultimaEdicaoEditar + "' where id = " + idEditar;
    }

    public String createParcelamentoTable() {
        return "create table if not exists parcelamento (id integer identity primary key, descricao varchar(200), vencimento varchar(20), valor_parcela varchar(30), valor_total varchar(30), numero_parcela varchar(5), total_parcela varchar(5), status varchar(10),data_alteracao varchar(20))";
    }

    public String createFixaTable() {
        return "create table if not exists fixa (id integer identity primary key, descricao varchar(200), vencimento varchar(20), valor varchar(30))";
    }

    public String createGanhoTable() {
        return "create table if not exists ganho (id integer identity primary key, data varchar(20), dia_semana varchar(20), cliente varchar(200), status varchar(10), sessao varchar(2), quantidade varchar(2), valor varchar(30))";
    }

    public String createClienteTable() {
        return "create table if not exists cliente (id integer identity primary key, nome varchar(200))";
    }

    public String createDataControleTable() {
        return "create table if not exists data_controle (id integer identity primary key, data_controle varchar(20))";
    }

    public String createSobrouMesPassadoTable() {
        return "create table if not exists sobrou_mes_passado (id integer identity primary key, data varchar(20), sobrou_mes_passado varchar(30))";
    }
}
