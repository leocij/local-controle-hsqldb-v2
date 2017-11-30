package com.lemelo.data_controle;

import com.lemelo.util.FabricaConexao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataControleDao {
    public void inicializa() throws SQLException {
        String dataControleStr = buscaDataControle();
        if (dataControleStr.equals("") || dataControleStr == null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dataAtual = sdf1.format(Calendar.getInstance().getTime());
            String dataControleInsert = "insert into data_controle (data_controle) values ('"+dataAtual+"')";
            new FabricaConexao().insert(dataControleInsert);
        }
    }

    public String buscaDataControle() throws SQLException {
        //TODO retornar somente ultimo registro

        //TODO testar se retorna somente a ultima data

        String dataControleSelect = "select data_controle from data_controle order by id desc limit 1";
        ResultSet resultSet = new FabricaConexao().getResultSet(dataControleSelect);
        String dataControleStr = "";
        while (resultSet.next()) {
            dataControleStr = resultSet.getString("data_controle");
        }
        return dataControleStr;
    }

    public void updateDataControle(String dataAtualStr) {
        String dataControleUpdate = "update data_controle set data_controle = '"+dataAtualStr+"'";
        new FabricaConexao().update(dataControleUpdate);
    }

    public void insert(String dataAtualStr) {
        String dataControleInsert = "insert into data_controle (data_controle) values ('"+dataAtualStr+"')";
        new FabricaConexao().insert(dataControleInsert);
    }
}
