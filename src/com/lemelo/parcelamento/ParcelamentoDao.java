package com.lemelo.parcelamento;

import com.lemelo.saida.SaidaDao;
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
        String dataAlteracao = parcelamento.getDataAlteracao();

        Integer totalParcelaInt = Integer.parseInt(totalParcelaStr);
        for (int i=0; i<totalParcelaInt; i++) {

            String parcelamentoSqlInsert = "insert into parcelamento (descricao, vencimento, valor_parcela, valor_total, numero_parcela, total_parcela, status, data_alteracao)"
                    +" values ('"+descricaoStr+"','"+vencimentoStr+"','"+valorParcelaStr+"','"+valorTotalStr+"','"+Integer.toString(i + 1)+"','"+totalParcelaStr+"','"+statusStr+"','"+dataAlteracao+"')";

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

    public ObservableList<Parcelamento> buscaPorSemPagar() throws SQLException, ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date dataAtual = sdf2.parse(dataAtualStr);

        ObservableList<Parcelamento> parcelamentos = FXCollections.observableArrayList();
        String parcelamentoSqlSelect = "select * from parcelamento";
        ResultSet resultSet = new FabricaConexao().getResultSet(parcelamentoSqlSelect);
        while (resultSet.next()) {
            if (resultSet.getString("status").equals("-")) {

                String vencimentoStr = resultSet.getString("vencimento");
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
                Date vencimento = sdf3.parse(vencimentoStr);

                if (vencimento.before(dataAtual)) {
                    Parcelamento parcelamento = new Parcelamento();
                    parcelamento.setId(resultSet.getInt("id"));
                    parcelamento.setDescricao(resultSet.getString("descricao"));
                    parcelamento.setVencimento(resultSet.getString("vencimento"));
                    parcelamento.setValorParcela(resultSet.getString("valor_parcela"));
                    parcelamento.setValorTotal(resultSet.getString("valor_total"));
                    parcelamento.setNumeroParcela(resultSet.getString("numero_parcela"));
                    parcelamento.setTotalParcela(resultSet.getString("total_parcela"));
                    parcelamento.setStatus(resultSet.getString("status"));
                    parcelamento.setDataAlteracao(resultSet.getString("data_alteracao"));

                    parcelamentos.add(parcelamento);
                }
            }
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
            parcelamento.setDataAlteracao(resultSet.getString("data_alteracao"));

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

    public void update(Parcelamento parcelamento) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dataAlteracao = sdf3.format(Calendar.getInstance().getTime());
        String parcelamentoSqlUpdate = "";
        if (parcelamento.getStatus().equals("-")) {
            parcelamentoSqlUpdate = "update parcelamento set status = 'pago', data_alteracao = '"+dataAlteracao+"' where id = " + parcelamento.getId();
            SaidaDao saidaDao = new SaidaDao();
            saidaDao.insertSaida(parcelamento.getDescricao(), parcelamento.getNumeroParcela(), parcelamento.getValorParcela());
        } else if (parcelamento.getStatus().equals("pago")) {
            parcelamentoSqlUpdate = "update parcelamento set status = '-', data_alteracao = '"+dataAlteracao+"' where id = " + parcelamento.getId();
            SaidaDao saidaDao = new SaidaDao();
            saidaDao.removeSaida(parcelamento.getDescricao(), parcelamento.getNumeroParcela(), parcelamento.getValorParcela());
        }
        new FabricaConexao().update(parcelamentoSqlUpdate);
    }

    public ObservableList<Parcelamento> buscaPorDescricaoSemPagar(String newValue) throws SQLException {
        ObservableList<Parcelamento> parcelamentos = FXCollections.observableArrayList();
        String parcelamentoSqlSelect = "select * from parcelamento where descricao like '%"+newValue+"%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(parcelamentoSqlSelect);
        while (resultSet.next()) {
            if(resultSet.getString("status").equals("-")){
                Parcelamento parcelamento = new Parcelamento();
                parcelamento.setId(resultSet.getInt("id"));
                parcelamento.setDescricao(resultSet.getString("descricao"));
                parcelamento.setVencimento(resultSet.getString("vencimento"));
                parcelamento.setValorParcela(resultSet.getString("valor_parcela"));
                parcelamento.setValorTotal(resultSet.getString("valor_total"));
                parcelamento.setNumeroParcela(resultSet.getString("numero_parcela"));
                parcelamento.setTotalParcela(resultSet.getString("total_parcela"));
                parcelamento.setStatus(resultSet.getString("status"));
                parcelamento.setDataAlteracao(resultSet.getString("data_alteracao"));

                parcelamentos.add(parcelamento);
            }
        }
        resultSet.close();
        return parcelamentos;
    }

    public void updateEditar(Parcelamento parcelamento, Integer idEditar, String numeroParcelaEditar, String statusEditar, String dataAlteracaoEditar) {
        String parcelamentoSqlUpdate = "update parcelamento set descricao='"+parcelamento.getDescricao()+"', vencimento='"+parcelamento.getVencimento()+"',valor_parcela='"+parcelamento.getValorParcela()+"',valor_total='"+parcelamento.getValorTotal()+"',numero_parcela='"+numeroParcelaEditar+"',total_parcela='"+parcelamento.getTotalParcela()+"',status='"+statusEditar+"',data_alteracao='"+dataAlteracaoEditar+"' where id="+idEditar;
        new FabricaConexao().update(parcelamentoSqlUpdate);
    }

    public void apagar(Integer id) {
        String parcelamentoSqlDelete = "delete from parcelamento where id = " + id;
        new FabricaConexao().delete(parcelamentoSqlDelete);
    }
}
