package com.lemelo.saida;

import com.lemelo.util.BibliotecaString;
import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaidaDao {
    void insert(Saida saida) throws SQLException {
        String dataHoraStr = saida.getDataHora();
        String descricaoStr = saida.getDescricao();
        String valorStr = saida.getValor();
        String ultimaEdicaoStr = saida.getUltimaEdicao();

        String saidaSqlInsert = new BibliotecaString().saidaSqlInsert(dataHoraStr,descricaoStr,valorStr,ultimaEdicaoStr);
        new FabricaConexao().insert(saidaSqlInsert);
    }

    void apagar(Integer id) {
        String saidaSqlDelete = "delete from saida where id = " + id;
        new FabricaConexao().delete(saidaSqlDelete);
    }

    void update(Saida saida, String ultimaEdicaoEditar, Integer idEditar) {
        String saidaSqlUpdate = new BibliotecaString().saidaSqlUpdate(saida,ultimaEdicaoEditar,idEditar);
        new FabricaConexao().update(saidaSqlUpdate);
    }

    public ObservableList<Saida> mostraTudoDoMesAtual() throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAno = dataAtual.substring(3,10);

        ObservableList<Saida> saidas = FXCollections.observableArrayList();
        String saidaSqlSelect = "select * from saida where data_hora like '%"+mesAno+"%' order by id desc";
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

    public ObservableList<Saida> buscaSaidaPorData(String newValue) throws SQLException {
        ObservableList<Saida> saidas = FXCollections.observableArrayList();
        String saidaSqlSelect = "select * from saida where data_hora like '%"+newValue+"%'";
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

    public ObservableList<Saida> buscaSaidaPorDescricao(String newValue) throws SQLException {
        ObservableList<Saida> saidas = FXCollections.observableArrayList();
        String saidaSqlSelect = "select * from saida where descricao like '%"+newValue+"%'";
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

    public void insertSaida(String descricao, String numeroParcela, String valorParcela) {
        SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dataHoraStr = dataHoraSdf.format(Calendar.getInstance().getTime());
        String descricaoStr = descricao+" - parcela de número "+numeroParcela;
        String valorStr = valorParcela;
        String ultimaEdicaoStr = "-";

        String saidaSqlInsert = new BibliotecaString().saidaSqlInsert(dataHoraStr,descricaoStr,valorStr,ultimaEdicaoStr);
        new FabricaConexao().insert(saidaSqlInsert);
    }

    public void removeSaida(String descricao, String numeroParcela, String valorParcela) {
        String saidaSqlDelete = "delete from saida where descricao like '"+descricao+" - parcela de número "+numeroParcela+"' and valor like '"+valorParcela+"'";
        new FabricaConexao().delete(saidaSqlDelete);
    }
}
