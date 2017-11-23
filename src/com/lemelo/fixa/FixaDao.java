package com.lemelo.fixa;

import com.lemelo.entrada.Entrada;
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

public class FixaDao {
    public ObservableList<Fixa> buscarPorMesAno() throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAno = dataAtual.substring(3,10);

        ObservableList<Fixa> fixas = FXCollections.observableArrayList();
        String fixaSqlSelect = "select * from fixa where vencimento like '%"+mesAno+"%' order by vencimento asc";
        ResultSet resultSet = new FabricaConexao().getResultSet(fixaSqlSelect);
        while (resultSet.next()) {
            Fixa fixa = new Fixa();
            fixa.setId(resultSet.getInt("id"));
            fixa.setDescricao(resultSet.getString("descricao"));
            fixa.setVencimento(resultSet.getString("vencimento"));
            fixa.setValor(resultSet.getString("valor"));

            fixas.add(fixa);
        }
        resultSet.close();
        return fixas;
    }

    public void insert(Fixa fixa) {
        String fixaSqlInsert = "insert into fixa (descricao,vencimento,valor) values ('"+fixa.getDescricao()+"','"+fixa.getVencimento()+"','"+fixa.getValor()+"')";
        new FabricaConexao().insert(fixaSqlInsert);

//        String fixaTesteSqlInsert = "insert into fixa (descricao,vencimento,valor) values ('teste1','05/10/2017','R$ 200,00')";
//        String fixaTesteSqlInsert2 = "insert into fixa (descricao,vencimento,valor) values ('teste2','25/10/2017','R$ 100,00')";
//        new FabricaConexao().insert(fixaTesteSqlInsert);
//        new FabricaConexao().insert(fixaTesteSqlInsert2);
    }

    public void atualizaFixa() throws SQLException, ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAno = dataAtual.substring(3,10);
        //String mesAno = "10/2017";

        String fixaSeExisteRegistroSqlSelect = "select * from fixa where vencimento like '%"+mesAno+"%' order by id limit 1";
        ResultSet resultSet1 = new FabricaConexao().getResultSet(fixaSeExisteRegistroSqlSelect);
        if(!resultSet1.next()) {
            String fixaSqlSelect = "select * from fixa";
            ResultSet resultSet2 = new FabricaConexao().getResultSet(fixaSqlSelect);
            while (resultSet2.next()) {
                String vencimentoStr = resultSet2.getString("vencimento");
                String pegaDiaStr = vencimentoStr.substring(0,3);
                vencimentoStr = pegaDiaStr + mesAno;

                String fixaSqlInsert = "insert into fixa (descricao,vencimento,valor) values ('"+resultSet2.getString("descricao")+"','"+vencimentoStr+"','"+resultSet2.getString("valor")+"')";
                new FabricaConexao().insert(fixaSqlInsert);
            }
            resultSet2.close();
        }
        resultSet1.close();
    }
}
