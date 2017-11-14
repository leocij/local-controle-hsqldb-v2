package com.lemelo.saida;

import com.lemelo.util.BibliotecaString;
import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SaidaDao {
    void insert(Saida saida) throws SQLException {
        String dataHoraStr = saida.getDataHora();
        String descricaoStr = saida.getDescricao();
        String valorStr = saida.getValor();
        String ultimaEdicaoStr = saida.getUltimaEdicao();

        String saidaSqlInsert = new BibliotecaString().saidaSqlInsert(dataHoraStr,descricaoStr,valorStr,ultimaEdicaoStr);
        new FabricaConexao().insert(saidaSqlInsert);
    }

    public ObservableList<Saida> listAll() throws SQLException {
        ObservableList<Saida> saidas = FXCollections.observableArrayList();
        String saidaSqlSelect = "select * from saida order by id desc";
        ResultSet resultSet = new FabricaConexao().getResultSet(saidaSqlSelect);
        while (resultSet.next()) {
            Saida saida = new Saida();
            saida.setId(resultSet.getInt("id"));
            saida.setDataHora(resultSet.getString("data_hora"));
            saida.setDescricao(resultSet.getString("descricao"));
            saida.setValor(resultSet.getString("valor"));
            saida.setUltimaEdicao(resultSet.getString("ultima_edicao"));

            saidas.add(saida);
        }
        resultSet.close();
        return saidas;
    }

    void apagar(Integer id) {
        String saidaSqlDelete = "delete from saida where id = " + id;
        new FabricaConexao().delete(saidaSqlDelete);
    }

    void update(Saida saida, String ultimaEdicaoEditar, Integer idEditar) {
        String saidaSqlUpdate = new BibliotecaString().saidaSqlUpdate(saida,ultimaEdicaoEditar,idEditar);
        new FabricaConexao().update(saidaSqlUpdate);
    }
}
