package com.lemelo.util;

import com.lemelo.saida.Saida;

public class BibliotecaString {
    public String urlBanco() {
        //Como conectar no banco.
        //java -classpath hsqldb.jar org.hsqldb.util.DatabaseManager
        //Standalone
        //jdbc:hsqldb:file:E:/Leoci/Projetos Intellij/local-controle-hsqldb-v2/database/db
        return "jdbc:hsqldb:file:database/db";
    }

    public String createSaldoTable() {
        return "create table if not exists saldo (id integer identity primary key, data varchar(20), saldo varchar(30))";
    }

    public String createEntradaTable() {
        return "create table if not exists entrada (id integer identity primary key, data_hora varchar(20), descricao varchar(100), valor varchar(30), ultima_edicao varchar(20))";
    }

    public String createSaidaTable() {
        return "create table if not exists saida (id integer identity primary key, data_hora varchar(20), descricao varchar(100), valor varchar(30), ultima_edicao varchar(20))";
    }

    public String saidaSqlInsert(String dataHoraStr, String descricaoStr, String valorStr, String ultimaEdicaoStr) {
        return "insert into saida (data_hora, descricao, valor, ultima_edicao) values ('" + dataHoraStr + "','" + descricaoStr + "','" + valorStr + "', '" + ultimaEdicaoStr + "')";
    }

    public String saidaSqlUpdate(Saida saida, String ultimaEdicaoEditar, Integer idEditar) {
        return "update saida set data_hora = '" + saida.getDataHora() + "', descricao = '" + saida.getDescricao() + "', valor = '" + saida.getValor() + "', ultima_edicao = '" + ultimaEdicaoEditar + "' where id = " + idEditar;
    }

    public String createTotalEntradaTable() {
        return "create table if not exists total_entrada (id integer identity primary key, data varchar(20), valor varchar(30))";
    }
}
