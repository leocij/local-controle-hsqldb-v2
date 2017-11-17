package com.lemelo.saldo;

import com.lemelo.entrada.TotalEntradaDao;
import com.lemelo.saida.TotalSaidaDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class SaldoLogica {

    public void calcularEntrada(String dataHoraStr, String valorStr) throws SQLException, ParseException {

        String mesAno = dataHoraStr.substring(3,10);
        String dataStr = dataHoraStr.substring(0,10);
        TotalEntradaDao totalEntradaDao = new TotalEntradaDao();

        String totalEntradaStr = totalEntradaDao.buscaTotalEntrada(mesAno);

        if(totalEntradaStr.equals("")) {
            totalEntradaDao.insertTotalEntrada(dataStr, valorStr);
        } else {
            String totalEntradaNf = NumberFormat.getCurrencyInstance().parse(totalEntradaStr).toString();
            BigDecimal totalEntradaBdc = new BigDecimal(totalEntradaNf);

            String valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
            BigDecimal valorBdc = new BigDecimal(valorNf);

            totalEntradaBdc = totalEntradaBdc.add(valorBdc);

            String novoTotalEntradaStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalEntradaBdc);

            totalEntradaDao.updateTotalEntrada(mesAno, novoTotalEntradaStr);
        }
    }


    public String buscaTotalEntrada(String mesAno) throws SQLException, ParseException {
        TotalEntradaDao totalEntradaDao = new TotalEntradaDao();
        String totalEntradaStr = totalEntradaDao.buscaTotalEntrada(mesAno);

        return totalEntradaStr;
    }

    public void calcularSaida(String dataHoraStr, String valorStr) throws SQLException, ParseException {
        String mesAno = dataHoraStr.substring(3,10);
        String dataStr = dataHoraStr.substring(0,10);
        TotalSaidaDao totalSaidaDao = new TotalSaidaDao();
        String totalSaidaStr = totalSaidaDao.buscaTotalSaida(mesAno);
        if(totalSaidaStr.equals("")) {
            totalSaidaDao.insertTotalSaida(dataStr, valorStr);
        } else {
            String totalSaidaNf = NumberFormat.getCurrencyInstance().parse(totalSaidaStr).toString();
            BigDecimal totalSaidaBdc = new BigDecimal(totalSaidaNf);
            String valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
            BigDecimal valorBdc = new BigDecimal(valorNf);
            totalSaidaBdc = totalSaidaBdc.add(valorBdc);
            String novoTotalSaidaStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalSaidaBdc);
            totalSaidaDao.updateTotalSaida(mesAno, novoTotalSaidaStr);
        }
    }

    public String buscaTotalSaida(String mesAno) throws SQLException {
        TotalSaidaDao totalSaidaDao = new TotalSaidaDao();
        String totalSaidaStr = totalSaidaDao.buscaTotalSaida(mesAno);
        return totalSaidaStr;
    }
}
