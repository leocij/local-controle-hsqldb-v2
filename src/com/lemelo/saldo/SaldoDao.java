package com.lemelo.saldo;

import com.lemelo.entrada.EntradaDao;
import com.lemelo.entrada.TotalEntradaDao;
import com.lemelo.saida.TotalSaidaDao;
import com.lemelo.util.FabricaConexao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SaldoDao {
    public String buscaTotalEntrada(String mesAno) throws SQLException {

        String buscaTotalEntradaSqlSelect = "select total_entrada from saldo where data like '%" + mesAno + "%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaTotalEntradaSqlSelect);
        String totalEntradaStr = "";
        while (resultSet.next()) {
            totalEntradaStr = resultSet.getString("total_entrada");
        }

        return totalEntradaStr;
    }

    public void updateTotalEntrada(String mesAno, String novoTotalEntradaStr) {
        String totalEntradaUpdate = "update saldo set total_entrada = '" + novoTotalEntradaStr + "' where data like '%" + mesAno + "%'";
        new FabricaConexao().update(totalEntradaUpdate);
    }

    public void insertTotalEntrada(String totalEntradaNf) {
        String totalEntradaInsert = "insert into saldo (total_entrada) values ('" + totalEntradaNf + "')";
        new FabricaConexao().insert(totalEntradaInsert);
    }

    public void atualizaSobrouMesPassado() throws ParseException, SQLException {

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
        String dataAtualSubstr = dataAtualStr.substring(3,10);
//        if (dataAtualSubstr.equals("01")) {
//            SaldoDao saldoDao = new SaldoDao();
//            saldoDao.atualizaSobrouMesPassado(dataAtualStr);
//        }

        String dataUltimaConsultaSaldoStr = buscaDataUltimaConsultaSaldo();
        if (dataUltimaConsultaSaldoStr != null && !dataUltimaConsultaSaldoStr.equals("")) {
            String mesAnoUltimaConsultaSaldoStr = dataUltimaConsultaSaldoStr.substring(3,10);

            if (!mesAnoUltimaConsultaSaldoStr.equals(dataAtualStr)) {
                TotalEntradaDao totalEntradaDao = new TotalEntradaDao();
                String totalEntradaStr = totalEntradaDao.buscaTotalEntrada(mesAnoUltimaConsultaSaldoStr);
                if (totalEntradaStr.equals("")) {
                    totalEntradaStr = "R$ 0,00";
                }

                TotalSaidaDao totalSaidaDao = new TotalSaidaDao();
                String totalSaidaStr = totalSaidaDao.buscaTotalSaida(mesAnoUltimaConsultaSaldoStr);
                if (totalSaidaStr.equals("")) {
                    totalSaidaStr = "R$ 0,00";
                }

                String totalEntradaNf = NumberFormat.getCurrencyInstance().parse(totalEntradaStr).toString();
                String totalSaidaNf = NumberFormat.getCurrencyInstance().parse(totalSaidaStr).toString();

                BigDecimal totalEntradaBdc = new BigDecimal(totalEntradaNf);
                BigDecimal totalSaidaBdc = new BigDecimal(totalSaidaNf);

                BigDecimal sobrouMesPassadoBdc = totalEntradaBdc.subtract(totalSaidaBdc);

                String sobrouMesPassadoStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(sobrouMesPassadoBdc);

                String saldoSqlInsert = "insert into saldo (data,sobrou_mes_passado,saldo,data_ultima_consulta_saldo) values ('"+dataAtualStr+"','"+sobrouMesPassadoStr+"','"+sobrouMesPassadoStr+"','"+dataUltimaConsultaSaldoStr+"')";
                new FabricaConexao().insert(saldoSqlInsert);

                EntradaDao entradaDao = new EntradaDao();
                entradaDao.insertSobrouMesPassado(sobrouMesPassadoStr);
            }
        }
    }

    private String buscaDataUltimaConsultaSaldo() throws SQLException {
        String buscaDataUltimaConsultaSaldo = "select data_ultima_consulta_saldo from saldo order by id limit 1";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaDataUltimaConsultaSaldo);
        String buscaDataUltimaConsulta = "";
        while (resultSet.next()) {
            buscaDataUltimaConsulta = resultSet.getString("data_ultima_consulta_saldo");
        }

        return buscaDataUltimaConsulta;
    }

    public String buscaSobrouMesPassado(String mesAno) throws SQLException {
        String buscaSobrouMesPassadoSqlSelect = "select sobrou_mes_passado from saldo where data like '%" + mesAno + "%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaSobrouMesPassadoSqlSelect);
        String buscaSobrouMesPassadoStr = "";
        while (resultSet.next()) {
            buscaSobrouMesPassadoStr = resultSet.getString("sobrou_mes_passado");
        }

        return buscaSobrouMesPassadoStr;
    }

    public void insertSobrouMesPassado(String dataAtualStr, String sobrouMesPassadoStr) {
        String saldoSqlInsert = "insert into saldo (data,sobrou_mes_passado,saldo) values ('"+dataAtualStr+"','"+sobrouMesPassadoStr+"','"+sobrouMesPassadoStr+"')";
        new FabricaConexao().insert(saldoSqlInsert);
    }
}
