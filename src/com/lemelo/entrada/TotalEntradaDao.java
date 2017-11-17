package com.lemelo.entrada;

import com.lemelo.util.FabricaConexao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TotalEntradaDao {
    public String buscaTotalEntrada(String mesAno) throws SQLException {
        String buscaTotalEntradaSqlSelect = "select valor from total_entrada where data like '%" + mesAno + "%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaTotalEntradaSqlSelect);
        String totalEntradaStr = "";
        while (resultSet.next()) {
            totalEntradaStr = resultSet.getString("valor");
        }

        return totalEntradaStr;
    }

    public void insertTotalEntrada(String dataStr, String valorStr) {
        String totalEntradaInsert = "insert into total_entrada (data, valor) values ('"+dataStr+"','" + valorStr + "')";
        new FabricaConexao().insert(totalEntradaInsert);
    }

    public void updateTotalEntrada(String mesAno, String novoTotalEntradaStr) {
        String totalEntradaUpdate = "update total_entrada set valor = '" + novoTotalEntradaStr + "' where data like '%" + mesAno + "%'";
        new FabricaConexao().update(totalEntradaUpdate);
    }
}
