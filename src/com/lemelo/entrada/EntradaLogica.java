package com.lemelo.entrada;

import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class EntradaLogica {
    public String calculaTotalEntrada(String dataAtualMesAno) throws SQLException, ParseException {
        EntradaDao entradaDao = new EntradaDao();
        ObservableList<Entrada> entradas = entradaDao.buscaEntradaPorData(dataAtualMesAno);

        BigDecimal totalEntrada = BigDecimal.ZERO;
        String valorStr = "";
        for (int i=0;i<entradas.size();i++) {
            entradas.get(i).getValor();
            valorStr = NumberFormat.getCurrencyInstance().parse(entradas.get(i).getValor()).toString();
            totalEntrada = totalEntrada.add(new BigDecimal(valorStr));
            valorStr = "";
        }

        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalEntrada);
    }
}
