package com.lemelo.sobrou_mes_passado;

import com.lemelo.entrada.TotalEntradaDao;
import com.lemelo.saida.TotalSaidaDao;
import com.lemelo.saldo.SaldoDao;
import com.lemelo.util.FabricaConexao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SobrouMesPassadoDao {
    public String atualizaSobrouMesPassado(String dataControleMesAno) throws SQLException, ParseException {
        TotalEntradaDao totalEntradaDao = new TotalEntradaDao();
        String totalEntradaStr = totalEntradaDao.buscaTotalEntrada(dataControleMesAno);
        if (totalEntradaStr.equals("")) {
            totalEntradaStr = "R$ 0,00";
        }
        String totalEntradaNf = NumberFormat.getCurrencyInstance().parse(totalEntradaStr).toString();
        BigDecimal totalEntradaBdc = new BigDecimal(totalEntradaNf);

        TotalSaidaDao totalSaidaDao = new TotalSaidaDao();
        String totalSaidaStr = totalSaidaDao.buscaTotalSaida(dataControleMesAno);
        if (totalSaidaStr.equals("")) {
            totalSaidaStr = "R$ 0,00";
        }
        String totalSaidaNf = NumberFormat.getCurrencyInstance().parse(totalSaidaStr).toString();
        BigDecimal totalSaidaBdc = new BigDecimal(totalSaidaNf);

        BigDecimal sobrouMesPassadoBdc = totalEntradaBdc.subtract(totalSaidaBdc);
        String sobrouMesPassadoStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(sobrouMesPassadoBdc);

        SobrouMesPassado sobrouMesPassado = new SobrouMesPassado();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
        sobrouMesPassado.setData(dataAtualStr);

        sobrouMesPassado.setSobrouMesPassado(sobrouMesPassadoStr);

        insert(sobrouMesPassado);

        return sobrouMesPassadoStr;
    }

    public void insert(SobrouMesPassado sobrouMesPassado) {
        String dataAtualStr = sobrouMesPassado.getData();
        String sobrouMesPassadoStr = sobrouMesPassado.getSobrouMesPassado();

        String sqlInsert = "insert into sobrou_mes_passado (data, sobrou_mes_passado) values ('"+dataAtualStr+"','"+sobrouMesPassadoStr+"')";
        new FabricaConexao().insert(sqlInsert);
    }

    public String buscaSobrouMesPassado() throws SQLException {
        String buscaSobrouMesPassadoSqlSelect = "select sobrou_mes_passado from sobrou_mes_passado order by id desc limit 1";
        ResultSet resultSet = new FabricaConexao().getResultSet(buscaSobrouMesPassadoSqlSelect);
        String buscaSobrouMesPassadoStr = "";
        while (resultSet.next()) {
            buscaSobrouMesPassadoStr = resultSet.getString("sobrou_mes_passado");
        }

        return buscaSobrouMesPassadoStr;
    }
}
