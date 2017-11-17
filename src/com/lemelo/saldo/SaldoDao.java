package com.lemelo.saldo;

import com.lemelo.util.FabricaConexao;

import java.sql.ResultSet;
import java.sql.SQLException;

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
}
