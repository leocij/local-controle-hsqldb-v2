package com.lemelo;

import com.lemelo.cliente.ClienteNode;
import com.lemelo.data_controle.DataControleDao;
import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
import com.lemelo.entrada.EntradaNode;
import com.lemelo.fixa.FixaDao;
import com.lemelo.fixa.FixaNode;
import com.lemelo.ganho.GanhoNode;
import com.lemelo.parcelamento.ParcelamentoNode;
import com.lemelo.saida.SaidaNode;
import com.lemelo.saldo.SaldoDao;
import com.lemelo.saldo.SaldoLogica;
import com.lemelo.sobrou_mes_passado.SobrouMesPassadoDao;
import com.lemelo.util.FabricaConexao;
import com.lemelo.saldo.SaldoResumoNode;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Principal extends Application {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        Class.forName("org.hsqldb.jdbcDriver");
        new FabricaConexao().createTables();

        new FixaDao().atualizaFixa();

        new DataControleDao().inicializa();

        atualizaViradaMes();

        launch(args);
    }

    private static void atualizaViradaMes() throws SQLException, ParseException {
        DataControleDao dataControleDao = new DataControleDao();
        String dataControleStr = dataControleDao.buscaDataControle();
        String dataControleMesAno = dataControleStr.substring(3,10);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
        String dataAtualMesAno = dataAtualStr.substring(3,10);

        if (!dataControleMesAno.equals(dataAtualMesAno)) {

            SobrouMesPassadoDao sobrouMesPassadoDao = new SobrouMesPassadoDao();
            String sobrouMesPassadoStr = sobrouMesPassadoDao.atualizaSobrouMesPassado(dataControleMesAno);

            insereApuradoMesPassado(dataControleStr, sobrouMesPassadoStr);

            new DataControleDao().insert(dataAtualStr);
        }
    }

    private static void insereApuradoMesPassado(String dataControleStr, String sobrouMesPassadoStr) throws ParseException, SQLException {
        Entrada entrada = new Entrada();

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dataHoraStr = sdf2.format(Calendar.getInstance().getTime());
        entrada.setDataHora(dataHoraStr);

        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf3.parse(dataControleStr);
        SimpleDateFormat sdf4 = new SimpleDateFormat("MMMM");
        String mesPorExtensoDataControle = sdf4.format(date1);
        entrada.setDescricao("Apurado do mês de " + mesPorExtensoDataControle);

        entrada.setValor(sobrouMesPassadoStr);

        entrada.setUltimaEdicao("-");

        //Atualiza total da entrada
        SaldoLogica saldoLogica = new SaldoLogica();
        saldoLogica.calcularEntrada(dataHoraStr, sobrouMesPassadoStr);

        new EntradaDao().insert(entrada);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);

        Tab saidaTab = new Tab();
        saidaTab.setText("Saída de Valores");
        saidaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        saidaTab.setClosable(false);
        saidaTab.setContent(new SaidaNode().executar(saidaTab));
        tabPane.getTabs().add(saidaTab);

        Tab entradaTab = new Tab();
        entradaTab.setText("Entrada de Valores");
        entradaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        entradaTab.setClosable(false);
        entradaTab.setContent(new EntradaNode().executar(entradaTab));
        tabPane.getTabs().add(entradaTab);

        Tab saldoResumoTab = new Tab();
        saldoResumoTab.setText("Saldo e Resumo");
        saldoResumoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        saldoResumoTab.setClosable(false);
        saldoResumoTab.setContent(new SaldoResumoNode().executar(saldoResumoTab));
        tabPane.getTabs().add(saldoResumoTab);

        Tab parcelamentoTab = new Tab();
        parcelamentoTab.setText("Parcelamento");
        parcelamentoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        parcelamentoTab.setClosable(false);
        parcelamentoTab.setContent(new ParcelamentoNode().executar(parcelamentoTab));
        tabPane.getTabs().add(parcelamentoTab);

        Tab fixaTab = new Tab();
        fixaTab.setText("Fixa");
        fixaTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        fixaTab.setClosable(false);
        fixaTab.setContent(new FixaNode().executar(fixaTab));
        tabPane.getTabs().add(fixaTab);

        Tab ganhoTab = new Tab();
        ganhoTab.setText("Ganhos");
        ganhoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        ganhoTab.setClosable(false);
        ganhoTab.setContent(new GanhoNode().executar(ganhoTab));
        tabPane.getTabs().add(ganhoTab);

        Tab clienteResumoTab =new Tab();
        clienteResumoTab.setText("Clientes");
        clienteResumoTab.setStyle("-fx-font: normal bold 15px 'verdana' ");
        clienteResumoTab.setClosable(false);
        clienteResumoTab.setContent(new ClienteNode().executar(clienteResumoTab));
        tabPane.getTabs().add(clienteResumoTab);

        Integer WIDTH_TAM = 847;
        Integer HEIGHT_TAM = 680;
        Scene scene = new Scene(tabPane, WIDTH_TAM, HEIGHT_TAM, Color.GRAY);
        primaryStage.setMinWidth(WIDTH_TAM);
        primaryStage.setMinHeight(HEIGHT_TAM);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Controle local de gastos");

        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
