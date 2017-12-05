package com.lemelo.saida;

import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class SaidaLogica {
    public String calculaTotalSaida(String dataAtualMesAno) throws SQLException, ParseException {
        SaidaDao saidaDao = new SaidaDao();
        ObservableList<Saida> saidas = saidaDao.buscaSaidaPorData(dataAtualMesAno);

        BigDecimal totalSaida = BigDecimal.ZERO;
        String valorStr = "";
        for (int i=0;i<saidas.size();i++) {
            saidas.get(i).getValor();
            valorStr = NumberFormat.getCurrencyInstance().parse(saidas.get(i).getValor()).toString();
            totalSaida = totalSaida.add(new BigDecimal(valorStr));
            valorStr = "";
        }

        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalSaida);
    }
}
