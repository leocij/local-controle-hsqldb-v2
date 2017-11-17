package com.lemelo.saida;

import com.lemelo.util.FabricaConexao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TotalSaidaDao {
    public String buscaTotalSaida(String mesAno) throws SQLException {
        String buscaTotalSaidaSqlSelect = "select valor from total_saida where data like '%" + mesAno + "%'";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaTotalSaidaSqlSelect);
        String totalSaidaStr = "";
        while (resultSet.next()) {
            totalSaidaStr = resultSet.getString("valor");
        }
        return totalSaidaStr;
    }

    public void insertTotalSaida(String dataStr, String valorStr) {
        String totalSaidaInsert = "insert into total_saida (data, valor) values ('"+dataStr+"','" + valorStr + "')";
        new FabricaConexao().insert(totalSaidaInsert);
    }

    public void updateTotalSaida(String mesAno, String novoTotalSaidaStr) {
        String totalSaidaUpdate = "update total_saida set valor = '" + novoTotalSaidaStr + "' where data like '%" + mesAno + "%'";
        new FabricaConexao().update(totalSaidaUpdate);
    }
}
