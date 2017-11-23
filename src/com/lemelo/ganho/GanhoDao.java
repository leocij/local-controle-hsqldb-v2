package com.lemelo.ganho;

import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GanhoDao {
    public void insert(Ganho ganho) {
        String ganhoSqlInsert = "insert into ganho (data,dia_semana) values ('"+ganho.getData()+"','"+ganho.getDiaSemana()+"')";
        new FabricaConexao().insert(ganhoSqlInsert);
    }

    public ObservableList<Ganho> buscaPorMesAno() throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAno = dataAtual.substring(3,10);

        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        String ganhoSqlSelect = "select * from ganho where data like '%"+mesAno+"%' order by data asc";
        ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
        while (resultSet.next()) {
            Ganho ganho = new Ganho();
            ganho.setId(resultSet.getInt("id"));
            ganho.setData(resultSet.getString("data"));
            ganho.setDiaSemana(resultSet.getString("dia_semana"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }
}
