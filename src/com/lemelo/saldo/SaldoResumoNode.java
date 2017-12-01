package com.lemelo.saldo;

import com.lemelo.data_controle.DataControleDao;
import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
import com.lemelo.saida.Saida;
import com.lemelo.saida.SaidaDao;
import com.lemelo.sobrou_mes_passado.SobrouMesPassadoDao;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaldoResumoNode{

    private TextField totalEntradaTextField;
    private TextField totalSaidaTextField;
    private TextField saldoAtualTextField;
    private TextField saidaBuscarPorDataTextField;
    private TextField saidaBuscarPorDescricaoTextField;
    private TextField entradaBuscarPorDataTextField;
    private TextField entradaBuscarPorDescricaoTextField;
    private TextField sobrouMesPassadoTextField;
    private TextField ganhoRealTextField;
    private TextField dataAtualTextField;
    private TextField dataControleTextField;

    public Node executar(Tab saldoResumoTab) {

        GridPane cabecalhoGridPane = geraCabecalhoGridPane();

        saldoResumoTab.setOnSelectionChanged((Event e) ->{
            if(saldoResumoTab.isSelected()) {

                //Coloca data atual
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
                String dataAtualMesAno = dataAtualStr.substring(3, 10);
                dataAtualTextField.setText(dataAtualStr);
                dataAtualTextField.setFocusTraversable(false);
                dataAtualTextField.setEditable(false);

                //Busca data de controle
                String dataControleStr = null;
                DataControleDao dataControleDao = new DataControleDao();
                try {
                    dataControleStr = dataControleDao.buscaDataControle();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                dataControleTextField.setText(dataControleStr);

                //Busca Total de Entrada
                SaldoLogica saldoLogica = new SaldoLogica();
                String totalEntradaStr = null;
                try {
                    totalEntradaStr = saldoLogica.buscaTotalEntrada(dataAtualMesAno);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if (totalEntradaStr==null || totalEntradaStr.equals("")) {
                    totalEntradaStr = "R$ 0,00";
                }
                totalEntradaTextField.setText(totalEntradaStr);

                //Busca Total de Saída
                String totalSaidaStr = null;
                try {
                    totalSaidaStr = saldoLogica.buscaTotalSaida(dataAtualMesAno);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if (totalSaidaStr==null || totalSaidaStr.equals("")) {
                    totalSaidaStr = "R$ 0,00";
                }
                totalSaidaTextField.setText(totalSaidaStr);

                SobrouMesPassadoDao sobrouMesPassadoDao = new SobrouMesPassadoDao();
                String sobrouMesPassadoStr = null;
                try {
                    sobrouMesPassadoStr = sobrouMesPassadoDao.buscaSobrouMesPassado();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if (sobrouMesPassadoStr==null || sobrouMesPassadoStr.equals("")) {
                    sobrouMesPassadoStr = "R$ 0,00";
                }
                sobrouMesPassadoTextField.setText(sobrouMesPassadoStr);

                //Preenche Saldo Atual
                String totalEntradaNf = null;
                String totalSaidaNf = null;
                try {
                    totalEntradaNf = NumberFormat.getCurrencyInstance().parse(totalEntradaStr).toString();
                    totalSaidaNf = NumberFormat.getCurrencyInstance().parse(totalSaidaStr).toString();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                assert totalEntradaNf != null;
                BigDecimal totalEntradaBdc = new BigDecimal(totalEntradaNf);
                assert totalSaidaNf != null;
                BigDecimal totalSaidaBdc = new BigDecimal(totalSaidaNf);
                BigDecimal saldoMensalAtualBdc = totalEntradaBdc.subtract(totalSaidaBdc);
                saldoAtualTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(saldoMensalAtualBdc));

                //Preenche Ganho Real
                String sobrouMesPassadoNf = null;
                try {
                    sobrouMesPassadoNf = NumberFormat.getCurrencyInstance().parse(sobrouMesPassadoStr).toString();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                assert sobrouMesPassadoNf != null;
                BigDecimal sobrouMesPassadoBdc = new BigDecimal(sobrouMesPassadoNf);
                BigDecimal ganhoRealBdc = totalEntradaBdc.subtract(sobrouMesPassadoBdc);
                String ganhoRealStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(ganhoRealBdc);
                ganhoRealTextField.setText(ganhoRealStr);
            }
        });

        TableView<Saida> saidaTableView = geraSaidaTableView();
        GridPane saidaGridPane = geraSaidaGridPane(saidaTableView);

        TableView<Entrada> entradaTableView = geraEntradaTableView();
        GridPane entradaGridPane = geraEntradaGridPane(entradaTableView);

        GridPane principalGridPane = geraPrincipalGridPane(cabecalhoGridPane, entradaGridPane, saidaGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraPrincipalGridPane(GridPane cabecalhoGridPane, GridPane entradaGridPane, GridPane saidaGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(cabecalhoGridPane, 0, 0);
        gridPane.add(saidaGridPane, 0, 1);
        gridPane.add(entradaGridPane, 0, 2);

        return gridPane;
    }

    private GridPane geraCabecalhoGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataAtualLabel = new Text("Data Atual: ");
        dataAtualLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(dataAtualLabel, 0, 0);
        dataAtualTextField = new TextField();
        gridPane.add(dataAtualTextField, 0, 1);

        Text dataControleLabel = new Text("Data Controle:: ");
        dataControleLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(dataControleLabel,1,0);
        dataControleTextField = new TextField();
        gridPane.add(dataControleTextField,1,1);

        Text totalEntradaLabel = new Text("Total da Entrada: ");
        totalEntradaLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(totalEntradaLabel,0,2);
        totalEntradaTextField = new TextField();
        gridPane.add(totalEntradaTextField,0,3);

        Text totalSaidaLabel = new Text("Total da Saída: ");
        totalSaidaLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(totalSaidaLabel,1,2);
        totalSaidaTextField = new TextField();
        gridPane.add(totalSaidaTextField,1,3);

        Text sobrouMesPassadoLabel = new Text("Sobrou do mês passado: ");
        sobrouMesPassadoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(sobrouMesPassadoLabel, 0, 4);
        sobrouMesPassadoTextField = new TextField();
        gridPane.add(sobrouMesPassadoTextField,0,5);

        Text saldoAtualLabel = new Text("Saldo mês atual: ");
        saldoAtualLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(saldoAtualLabel, 1, 4);
        saldoAtualTextField = new TextField();
        gridPane.add(saldoAtualTextField, 1, 5);

        Text ganhoRealLabel = new Text("Ganho real: ");
        ganhoRealLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(ganhoRealLabel, 2,4);
        ganhoRealTextField = new TextField();
        gridPane.add(ganhoRealTextField,2, 5);

        return gridPane;
    }

    private TableView<Saida> geraSaidaTableView() {

        TableColumn<Saida, String> dataHoraColuna = new TableColumn<>("Dt./Hr.");
        dataHoraColuna.setMinWidth(100);
        dataHoraColuna.setMaxWidth(100);
        dataHoraColuna.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        TableColumn<Saida, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Saida, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn<Saida, String> ultimaEdicaoColuna = new TableColumn<>("Última edição");
        ultimaEdicaoColuna.setMinWidth(100);
        ultimaEdicaoColuna.setCellValueFactory(new PropertyValueFactory<>("ultimaEdicao"));

        SaidaDao saidaDao = new SaidaDao();

        TableView<Saida> tableView = new TableView<>();

        Platform.runLater(()->saidaBuscarPorDataTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            ObservableList<Saida> list = null;
            try {
                list = saidaDao.buscaSaidaPorData(newValue);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list);
            tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
        }));

        Platform.runLater(()->saidaBuscarPorDescricaoTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            ObservableList<Saida> list = null;
            try {
                list = saidaDao.buscaSaidaPorDescricao(newValue);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list);
            tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
        }));

        return tableView;
    }

    private GridPane geraSaidaGridPane(TableView<Saida> saidaTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text saidaBuscarPorDataLabel = new Text("Buscar Saída por data: ");
        saidaBuscarPorDataLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        saidaBuscarPorDataTextField = new TextField();
        gridPane.add(saidaBuscarPorDataLabel,0,0);
        gridPane.add(saidaBuscarPorDataTextField,0,1);

        Text saidaBuscarPorDescricaoLabel = new Text("Buscar Saída por descrição: ");
        saidaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        saidaBuscarPorDescricaoTextField = new TextField();
        saidaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(saidaBuscarPorDescricaoLabel,0,2);
        gridPane.add(saidaBuscarPorDescricaoTextField,0,3);

        gridPane.add(saidaTableView, 0, 5);

        return gridPane;
    }

    private TableView<Entrada> geraEntradaTableView() {

        TableColumn<Entrada, String> dataHoraColuna = new TableColumn<>("Dt./Hr.");
        dataHoraColuna.setMinWidth(100);
        dataHoraColuna.setMaxWidth(100);
        dataHoraColuna.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        TableColumn<Entrada, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Entrada, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        TableColumn<Entrada, String> ultimaEdicaoColuna = new TableColumn<>("Última edição");
        ultimaEdicaoColuna.setMinWidth(100);
        ultimaEdicaoColuna.setCellValueFactory(new PropertyValueFactory<>("ultimaEdicao"));

        EntradaDao entradaDao = new EntradaDao();

        TableView<Entrada> tableView = new TableView<>();

        Platform.runLater(()->entradaBuscarPorDataTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            ObservableList<Entrada> list = null;
            try {
                list = entradaDao.buscaEntradaPorData(newValue);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list);
            tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
        }));

        Platform.runLater(()->entradaBuscarPorDescricaoTextField.textProperty().addListener((observable, oldValue, newValue) ->{
            ObservableList<Entrada> list = null;
            try {
                list = entradaDao.buscaEntradaPorDescricao(newValue);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            tableView.getColumns().clear();
            tableView.setItems(list);
            tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);
        }));


        return tableView;
    }

    private GridPane geraEntradaGridPane(TableView<Entrada> entradaTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text entradaBuscarPorDataLabel = new Text("Buscar Entrada por data: ");
        entradaBuscarPorDataLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        entradaBuscarPorDataTextField = new TextField();
        gridPane.add(entradaBuscarPorDataLabel,0,0);
        gridPane.add(entradaBuscarPorDataTextField,0,1);

        Text entradaBuscarPorDescricaoLabel = new Text("Buscar Entrada por descrição: ");
        entradaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        entradaBuscarPorDescricaoTextField = new TextField();
        entradaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(entradaBuscarPorDescricaoLabel,0,2);
        gridPane.add(entradaBuscarPorDescricaoTextField,0,3);

        gridPane.add(entradaTableView,0,5);

        return gridPane;
    }
}
