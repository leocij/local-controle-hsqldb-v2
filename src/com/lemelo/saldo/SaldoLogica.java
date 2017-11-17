package com.lemelo.saldo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class SaldoLogica {
    public void calcularEntrada(String mesAno, String valorStr) throws SQLException, ParseException {
        SaldoDao saldoDao = new SaldoDao();

        String totalEntradaStr = saldoDao.buscaTotalEntrada(mesAno);

        if(totalEntradaStr.equals("")) {
            saldoDao.insertTotalEntrada(valorStr);
        } else {
            String totalEntradaNf = NumberFormat.getCurrencyInstance().parse(totalEntradaStr).toString();
            BigDecimal totalEntradaBdc = new BigDecimal(totalEntradaNf);

            String valorNf = NumberFormat.getCurrencyInstance().parse(valorStr).toString();
            BigDecimal valorBdc = new BigDecimal(valorNf);

            totalEntradaBdc = totalEntradaBdc.add(valorBdc);

            String novoTotalEntradaStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalEntradaBdc);

            saldoDao.updateTotalEntrada(mesAno, novoTotalEntradaStr);
        }
    }


    public String buscaTotalEntrada(String mesAnoStr) throws SQLException, ParseException {
        SaldoDao saldoDao = new SaldoDao();
        String totalEntradaStr = saldoDao.buscaTotalEntrada(mesAnoStr);

        return totalEntradaStr;
    }
}
