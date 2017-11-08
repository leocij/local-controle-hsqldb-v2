package com.lemelo.entrada;

import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntradaDao {
    public void insert(Entrada entrada) throws SQLException {
        String dataHoraStr = entrada.getDataHora();
        String descricaoStr = entrada.getDescricao();
        String valorStr = entrada.getValor();

        String entradaSqlInsert = "insert into entrada (data_hora, descricao, valor) values ('" + dataHoraStr + "','" + descricaoStr + "','" + valorStr + "')";
        new FabricaConexao().insert(entradaSqlInsert);
    }

    public ObservableList<Entrada> listAll() throws SQLException {
        ObservableList<Entrada> entradas = FXCollections.observableArrayList();
        String entradaSqlSelect = "select * from entrada";
        ResultSet resultSet = new FabricaConexao().getResultSet(entradaSqlSelect);
        while (resultSet.next()) {
            Entrada entrada = new Entrada();
            entrada.setId(resultSet.getInt("id"));
            entrada.setDataHora(resultSet.getString("data_hora"));
            entrada.setDescricao(resultSet.getString("descricao"));
            entrada.setValor(resultSet.getString("valor"));

            entradas.add(entrada);
        }
        resultSet.close();
        return entradas;
    }
}
