package com.lemelo.entrada;

import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntradaDao {
    void insert(Entrada entrada) throws SQLException {
        String dataHoraStr = entrada.getDataHora();
        String descricaoStr = entrada.getDescricao();
        String valorStr = entrada.getValor();
        String ultimaEdicaoStr = entrada.getUltimaEdicao();

        String entradaSqlInsert = "insert into entrada (data_hora, descricao, valor, ultima_edicao) values ('" + dataHoraStr + "','" + descricaoStr + "','" + valorStr + "', '" + ultimaEdicaoStr + "')";
        new FabricaConexao().insert(entradaSqlInsert);
    }

    public ObservableList<Entrada> listAll() throws SQLException {
        ObservableList<Entrada> entradas = FXCollections.observableArrayList();
        String entradaSqlSelect = "select * from entrada order by id desc";
        ResultSet resultSet = new FabricaConexao().getResultSet(entradaSqlSelect);
        while (resultSet.next()) {
            Entrada entrada = new Entrada();
            entrada.setId(resultSet.getInt("id"));
            entrada.setDataHora(resultSet.getString("data_hora"));
            entrada.setDescricao(resultSet.getString("descricao"));
            entrada.setValor(resultSet.getString("valor"));
            entrada.setUltimaEdicao(resultSet.getString("ultima_edicao"));

            entradas.add(entrada);
        }
        resultSet.close();
        return entradas;
    }

    void apagar(Integer id) {
        String entradaSqlDelete = "delete from entrada where id = " + id;
        new FabricaConexao().delete(entradaSqlDelete);
    }

    void update(Entrada entrada, Integer idEditar, String ultimaEdicaoEditar) {
        String entradaSqlUpdate = "update entrada set data_hora = '" + entrada.getDataHora() + "', descricao = '" + entrada.getDescricao() + "', valor = '" + entrada.getValor() + "', ultima_edicao = '" + ultimaEdicaoEditar + "' where id = " + idEditar;
        new FabricaConexao().update(entradaSqlUpdate);
    }
}
