package com.lemelo.parcelamento;

import com.lemelo.util.FabricaConexao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ParcelamentoDao {
    public void insert(Parcelamento parcelamento) throws ParseException {

        String descricaoStr = parcelamento.getDescricao();
        String vencimentoStr = parcelamento.getVencimento();
        String valorParcelaStr = parcelamento.getValorParcela();
        String valorTotalStr = parcelamento.getValorTotal();
        String totalParcelaStr = parcelamento.getTotalParcela();
        String statusStr = parcelamento.getStatus();

        Integer totalParcelaInt = Integer.parseInt(totalParcelaStr);
        for (int i=0; i<totalParcelaInt; i++) {

            String parcelamentoSqlInsert = "insert into parcelamento (descricao, vencimento, valor_parcela, valor_total, numero_parcela, total_parcela, status)"
                    +" values ('"+descricaoStr+"','"+vencimentoStr+"','"+valorParcelaStr+"','"+valorTotalStr+"','"+Integer.toString(i + 1)+"','"+totalParcelaStr+"','"+statusStr+"')";

            new FabricaConexao().insert(parcelamentoSqlInsert);

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf1.parse(vencimentoStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.MONTH, 1);
            Date date2 = cal.getTime();
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            vencimentoStr = sdf2.format(date2);
        }
    }
}
