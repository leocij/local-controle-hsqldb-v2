package com.lemelo.util;

import com.lemelo.saida.Saida;

public class BibliotecaString {
    public String urlBanco() {
        return "jdbc:hsqldb:file:database/db";
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
}
