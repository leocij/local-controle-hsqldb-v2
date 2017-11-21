package com.lemelo.parcelamento;

import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ParcelamentoDao {
    public void insert(Parcelamento parcelamento) throws ParseException {

        String descricaoStr = parcelamento.getDescricao();
        String vencimentoStr = parcelamento.getVencimento();
        String valorParcelaStr = parcelamento.getValorParcela();
        String valorTotalStr = parcelamento.getValorTotal();
        String totalParcelaStr = parcelamento.getTotalParcela();
        String statusStr = parcelamento.getStatus();

        Integer totalParcelaInt = Integer.parseInt(totalParcelaStr);
        for (int i=0; i<totalParcelaInt; i++) {

            String parcelamentoSqlInsert = "insert into parcelamento (descricao, vencimento, valor_parcela, valor_total, numero_parcela, total_parcela, status)"
                    +" values ('"+descricaoStr+"','"+vencimentoStr+"','"+valorParcelaStr+"','"+valorTotalStr+"','"+Integer.toString(i + 1)+"','"+totalParcelaStr+"','"+statusStr+"')";

            new FabricaConexao().insert(parcelamentoSqlInsert);

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf1.parse(vencimentoStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.MONTH, 1);
            Date date2 = cal.getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            vencimentoStr = sdf2.format(date2);
        }
    }

    public ObservableList<Parcelamento> buscaPorMesAno() throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAno = dataAtual.substring(3,10);

        ObservableList<Parcelamento> parcelamentos = FXCollections.observableArrayList();
        String parcelamentoSqlSelect = "select * from parcelamento where vencimento like '%"+mesAno+"%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(parcelamentoSqlSelect);
        while (resultSet.next()) {
            Parcelamento parcelamento = new Parcelamento();
            parcelamento.setId(resultSet.getInt("id"));
            parcelamento.setDescricao(resultSet.getString("descricao"));
            parcelamento.setVencimento(resultSet.getString("vencimento"));
            parcelamento.setValorParcela(resultSet.getString("valor_parcela"));
            parcelamento.setValorTotal(resultSet.getString("valor_total"));
            parcelamento.setNumeroParcela(resultSet.getString("numero_parcela"));
            parcelamento.setTotalParcela(resultSet.getString("total_parcela"));
            parcelamento.setStatus(resultSet.getString("status"));

            parcelamentos.add(parcelamento);
        }
        resultSet.close();
        return parcelamentos;
    }

    public ObservableList<Parcelamento> buscaPorDescricao(String newValue) throws SQLException {
        ObservableList<Parcelamento> parcelamentos = FXCollections.observableArrayList();
        String parcelamentoSqlSelect = "select * from parcelamento where descricao like '%"+newValue+"%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(parcelamentoSqlSelect);
        while (resultSet.next()) {
            Parcelamento parcelamento = new Parcelamento();
            parcelamento.setId(resultSet.getInt("id"));
            parcelamento.setDescricao(resultSet.getString("descricao"));
            parcelamento.setVencimento(resultSet.getString("vencimento"));
            parcelamento.setValorParcela(resultSet.getString("valor_parcela"));
            parcelamento.setValorTotal(resultSet.getString("valor_total"));
            parcelamento.setNumeroParcela(resultSet.getString("numero_parcela"));
            parcelamento.setTotalParcela(resultSet.getString("total_parcela"));
            parcelamento.setStatus(resultSet.getString("status"));

            parcelamentos.add(parcelamento);
        }
        resultSet.close();
        return parcelamentos;
    }

    public ObservableList<Parcelamento> buscaPorVencimento(String newValue) throws SQLException {
        ObservableList<Parcelamento> parcelamentos = FXCollections.observableArrayList();
        String parcelamentoSqlSelect = "select * from parcelamento where vencimento like '%"+newValue+"%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(parcelamentoSqlSelect);
        while (resultSet.next()) {
            Parcelamento parcelamento = new Parcelamento();
            parcelamento.setId(resultSet.getInt("id"));
            parcelamento.setDescricao(resultSet.getString("descricao"));
            parcelamento.setVencimento(resultSet.getString("vencimento"));
            parcelamento.setValorParcela(resultSet.getString("valor_parcela"));
            parcelamento.setValorTotal(resultSet.getString("valor_total"));
            parcelamento.setNumeroParcela(resultSet.getString("numero_parcela"));
            parcelamento.setTotalParcela(resultSet.getString("total_parcela"));
            parcelamento.setStatus(resultSet.getString("status"));

            parcelamentos.add(parcelamento);
        }
        resultSet.close();
        return parcelamentos;
    }
}
