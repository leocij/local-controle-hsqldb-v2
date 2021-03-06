package com.lemelo.ganho;

import com.lemelo.cliente.Cliente;
import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
import com.lemelo.util.FabricaConexao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GanhoDao {
    public void insert(Ganho ganho) throws ParseException {
        String dataStr = ganho.getData();
        String diaSemanaStr = ganho.getDiaSemana();
        String clienteStr = ganho.getCliente();
        String statusStr = ganho.getStatus();
        String quantidadeStr = ganho.getQuantidade();
        String valorStr = ganho.getValor();

        if (statusStr.equals("pagou")) {
            Integer quantidadeInt = Integer.parseInt(quantidadeStr);
            for (int i=0; i<quantidadeInt; i++) {
                String ganhoSqlInsert = "insert into ganho (data,dia_semana,cliente,status,sessao,quantidade,valor) values ('"+dataStr+"','"+diaSemanaStr+"','"+clienteStr+"','"+statusStr+"','"+(i+1)+"','"+quantidadeStr+"','"+valorStr+"')";
                new FabricaConexao().insert(ganhoSqlInsert);

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                Date date1 = sdf1.parse(dataStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.add(Calendar.DAY_OF_MONTH, 7);
                Date date2 = cal.getTime();
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                dataStr = sdf2.format(date2);
            }
        } else {
            String ganhoSqlInsert = "insert into ganho (data,dia_semana,cliente,status,sessao,quantidade,valor) values ('"+dataStr+"','"+diaSemanaStr+"','"+clienteStr+"','"+statusStr+"','"+quantidadeStr+"','"+quantidadeStr+"','"+valorStr+"')";
            new FabricaConexao().insert(ganhoSqlInsert);
        }
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
            ganho.setCliente(resultSet.getString("cliente"));
            ganho.setStatus(resultSet.getString("status"));
            ganho.setSessao(resultSet.getString("sessao"));
            ganho.setQuantidade(resultSet.getString("quantidade"));
            ganho.setValor(resultSet.getString("valor"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }

    public ObservableList<Ganho> buscaPorCliente(Cliente newValue) throws SQLException {
        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        String ganhoSqlSelect = "select * from ganho where cliente like '%"+newValue+"%' order by sessao asc";
        ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
        while (resultSet.next()) {
            Ganho ganho = new Ganho();
            ganho.setId(resultSet.getInt("id"));
            ganho.setData(resultSet.getString("data"));
            ganho.setDiaSemana(resultSet.getString("dia_semana"));
            ganho.setCliente(resultSet.getString("cliente"));
            ganho.setStatus(resultSet.getString("status"));
            ganho.setSessao(resultSet.getString("sessao"));
            ganho.setQuantidade(resultSet.getString("quantidade"));
            ganho.setValor(resultSet.getString("valor"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }

    public ObservableList<Ganho> buscaPagador() throws SQLException {
        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        String ganhoSqlSelect = "select * from ganho where status like 'pagou' order by cliente asc";
        ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
        while (resultSet.next()) {
            Ganho ganho = new Ganho();
            ganho.setId(resultSet.getInt("id"));
            ganho.setData(resultSet.getString("data"));
            ganho.setDiaSemana(resultSet.getString("dia_semana"));
            ganho.setCliente(resultSet.getString("cliente"));
            ganho.setStatus(resultSet.getString("status"));
            ganho.setSessao(resultSet.getString("sessao"));
            ganho.setQuantidade(resultSet.getString("quantidade"));
            ganho.setValor(resultSet.getString("valor"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }

    public ObservableList<Ganho> buscaDevedor() throws SQLException {
        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        String ganhoSqlSelect = "select * from ganho where status like 'deve' order by cliente asc";
        ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
        while (resultSet.next()) {
            Ganho ganho = new Ganho();
            ganho.setId(resultSet.getInt("id"));
            ganho.setData(resultSet.getString("data"));
            ganho.setDiaSemana(resultSet.getString("dia_semana"));
            ganho.setCliente(resultSet.getString("cliente"));
            ganho.setStatus(resultSet.getString("status"));
            ganho.setSessao(resultSet.getString("sessao"));
            ganho.setQuantidade(resultSet.getString("quantidade"));
            ganho.setValor(resultSet.getString("valor"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }

    public void update(Ganho ganho) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataStr = sdf1.format(Calendar.getInstance().getTime());
        String sqlUpdate = "";
        if (ganho.getStatus().equals("deve")) {
            sqlUpdate = "update ganho set status = 'pagou', data = '"+dataStr+"' where id = " + ganho.getId();
            insertEntrada(ganho);
        } else if (ganho.getStatus().equals("pagou")){
            sqlUpdate = "update ganho set status = 'deve', data = '"+dataStr+"' where id = " + ganho.getId();
        }
        new FabricaConexao().update(sqlUpdate);
    }

    private void insertEntrada(Ganho ganho) {
        Entrada entrada = new Entrada();

        SimpleDateFormat dataHoraSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dataHoraStr = dataHoraSdf.format(Calendar.getInstance().getTime());
        entrada.setDataHora(dataHoraStr);

        entrada.setDescricao(ganho.getCliente() + " pagou no dia " + dataHoraStr);

        BigDecimal quantidadeBdc = new BigDecimal(ganho.getQuantidade());

        String valorStr = ganho.getValor();
        if (valorStr.equals("")) {
            valorStr = "R$ 0,00";
        }
        String valorNf = "";
        try {
            valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal valorBdc = new BigDecimal(valorNf);

        //BigDecimal totalValorBdc = valorBdc.multiply(quantidadeBdc);

        String totalValorStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(valorBdc);

        entrada.setValor(totalValorStr);

        entrada.setUltimaEdicao("-");

        EntradaDao entradaDao = new EntradaDao();
        try {
            entradaDao.insert(entrada);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEditar(Ganho ganho, Integer idEditar) {
        String ganhoSqlUpdate = "update ganho set data = '"+ganho.getData()+"', dia_semana = '"+ganho.getDiaSemana()+"', cliente = '"+ganho.getCliente()+"', status = '"+ganho.getStatus()+"', sessao = '"+ganho.getQuantidade()+"', quantidade = '"+ganho.getQuantidade()+"', valor = '"+ganho.getValor()+"' where id = " + idEditar;
        new FabricaConexao().update(ganhoSqlUpdate);
    }

    public void apagar(Integer id) {
        String ganhoSqlDelete = "delete from ganho where id = " + id;
        new FabricaConexao().delete(ganhoSqlDelete);
    }


    public ObservableList<Ganho> buscarPorQuantidadeDeMeses(String newValue) throws SQLException, ParseException {
        if (newValue.equals("")) {
            newValue = "0";
        }

        String dataUltimoRegistroSelect = "select data from ganho order by id desc limit 1";
        ResultSet resultSet2 = new FabricaConexao().getResultSet(dataUltimoRegistroSelect);
        resultSet2.next();
        String dataUltimoRegistro = resultSet2.getString("data");

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf2.parse(dataUltimoRegistro);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.MONTH, -Integer.parseInt(newValue));
        Date date2 = cal.getTime();
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtras = sdf3.format(date2);
        String dataAtrasMesAno = dataAtras.substring(3,10);

        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        for (int i=0; i<Integer.parseInt(newValue); i++) {
            String ganhoSqlSelect = "select * from ganho where data like '%"+dataAtrasMesAno+"%'";
            ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
            while (resultSet.next()) {
                Ganho ganho = new Ganho();
                ganho.setId(resultSet.getInt("id"));
                ganho.setData(resultSet.getString("data"));
                ganho.setDiaSemana(resultSet.getString("dia_semana"));
                ganho.setCliente(resultSet.getString("cliente"));
                ganho.setStatus(resultSet.getString("status"));
                ganho.setSessao(resultSet.getString("sessao"));
                ganho.setQuantidade(resultSet.getString("quantidade"));
                ganho.setValor(resultSet.getString("valor"));

                ganhos.add(ganho);
            }
            resultSet.close();

            SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy");
            Date date3 = sdf4.parse(dataAtras);
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date3);
            cal1.add(Calendar.MONTH, 1);
            Date date4 = cal1.getTime();
            SimpleDateFormat sdf5 = new SimpleDateFormat("dd/MM/yyyy");
            dataAtras = sdf5.format(date4);
            dataAtrasMesAno = dataAtras.substring(3,10);

            System.out.println();
        }

        return ganhos;
    }

    public Integer buscarQuantidadeMeses() throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtual = sdf1.format(Calendar.getInstance().getTime());
        String mesAnoAtual = dataAtual.substring(3,10);

        Integer quantidadeMeses = 1;
        String sqlSelect = "select * from ganho";
        ResultSet resultSet = new FabricaConexao().getResultSet(sqlSelect);

        int[][] matriz = new int[2300][20];

        for (int i=2000; i<2300; i++) {
            for (int j=0; j<12; j++) {
                matriz[i][j] = 0;
            }
        }

        while (resultSet.next()) {
            String dataStr = resultSet.getString("data");
            int mesInt = Integer.parseInt(dataStr.substring(3,5));
            int anoInt = Integer.parseInt(dataStr.substring(6,10));
            matriz[anoInt][mesInt]++;
        }

        for (int i=2000; i<2300; i++) {
            for (int j=0; j<12; j++) {
                if(matriz[i][j] != 0) {
                    quantidadeMeses++;
                }
            }
        }

        return quantidadeMeses;
    }

    public ObservableList<Ganho> listarTudo() throws SQLException {
        ObservableList<Ganho> ganhos = FXCollections.observableArrayList();
        String ganhoSqlSelect = "select * from ganho";
        ResultSet resultSet = new FabricaConexao().getResultSet(ganhoSqlSelect);
        while (resultSet.next()) {
            Ganho ganho = new Ganho();
            ganho.setId(resultSet.getInt("id"));
            ganho.setData(resultSet.getString("data"));
            ganho.setDiaSemana(resultSet.getString("dia_semana"));
            ganho.setCliente(resultSet.getString("cliente"));
            ganho.setStatus(resultSet.getString("status"));
            ganho.setSessao(resultSet.getString("sessao"));
            ganho.setQuantidade(resultSet.getString("quantidade"));
            ganho.setValor(resultSet.getString("valor"));

            ganhos.add(ganho);
        }
        resultSet.close();

        return ganhos;
    }
}
