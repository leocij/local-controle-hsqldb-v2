package com.lemelo.saldo;

import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
import com.lemelo.saida.Saida;
import com.lemelo.saida.SaidaDao;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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

    public Node executar(Tab saldoResumoTab) throws SQLException, ParseException {

        GridPane cabecalhoGridPane = geraCabecalhoGridPane();

        saldoResumoTab.setOnSelectionChanged(e->{
            if(saldoResumoTab.isSelected()==true) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String dataAtual = sdf1.format(Calendar.getInstance().getTime());
                String mesAno = dataAtual.substring(3,10);
                SaldoLogica saldoLogica = new SaldoLogica();
                String totalEntradaStr = null;
                try {
                    totalEntradaStr = saldoLogica.buscaTotalEntrada(mesAno);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                String finalTotalEntradaStr = totalEntradaStr;
                Platform.runLater(()->totalEntradaTextField.setText(finalTotalEntradaStr));
            }
        });

        TableView<Entrada> entradaTableView = geraEntradaTableView();
        GridPane entradaGridPane = geraEntradaGridPane(entradaTableView);

        TableView<Saida> saidaTableView = geraSaidaTableView();
        GridPane saidaGridPane = geraSaidaGridPane(saidaTableView);

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
        gridPane.add(entradaGridPane, 0, 1);
        gridPane.add(saidaGridPane, 0, 2);

        return gridPane;
    }

    private GridPane geraCabecalhoGridPane() throws SQLException, ParseException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataAtualLabel = new Text("Data Atual: ");
        dataAtualLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(dataAtualLabel, 0, 0);
        TextField dataAtualTextField = new TextField();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataAtualStr = sdf1.format(Calendar.getInstance().getTime());
        dataAtualTextField.setText(dataAtualStr);
        dataAtualTextField.setFocusTraversable(false);
        dataAtualTextField.setEditable(false);
        gridPane.add(dataAtualTextField, 0, 1);

        Text totalEntradaLabel = new Text("Total da Entrada: ");
        totalEntradaLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(totalEntradaLabel,0,2);
        totalEntradaTextField = new TextField();
        gridPane.add(totalEntradaTextField,0,3);


        Text totalSaidaLabel = new Text("Total da Saída: ");
        totalSaidaLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(totalSaidaLabel,1,2);
        TextField totalSaidaTextField = new TextField();
        gridPane.add(totalSaidaTextField,1,3);

        Text saldoPassadoLabel = new Text("Saldo mês passado: ");
        saldoPassadoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(saldoPassadoLabel, 0, 4);
        TextField saldoPassadoTextField = new TextField();
        gridPane.add(saldoPassadoTextField,0,5);

        Text saldoAtualLabel = new Text("Saldo mês atual: ");
        saldoAtualLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(saldoAtualLabel, 1, 4);
        TextField saldoAtualTextField = new TextField();
        gridPane.add(saldoAtualTextField, 1, 5);

        Text ganhoRealLabel = new Text("Ganho real: ");
        ganhoRealLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(ganhoRealLabel, 2,4);
        TextField ganhoRealTextField = new TextField();
        gridPane.add(ganhoRealTextField,2, 5);

        return gridPane;
    }

    private TableView<Entrada> geraEntradaTableView() throws SQLException {

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

        ObservableList<Entrada> list = entradaDao.listAll();
        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);

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
        TextField entradaBuscarPorDataTextField = new TextField();
        gridPane.add(entradaBuscarPorDataLabel,0,0);
        gridPane.add(entradaBuscarPorDataTextField,0,1);

        Text entradaBuscarPorDescricaoLabel = new Text("Buscar Entrada por descrição: ");
        entradaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        TextField entradaBuscarPorDescricaoTextField = new TextField();
        entradaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(entradaBuscarPorDescricaoLabel,0,2);
        gridPane.add(entradaBuscarPorDescricaoTextField,0,3);

        gridPane.add(entradaTableView,0,5);

        return gridPane;
    }

    private TableView<Saida> geraSaidaTableView() throws SQLException {

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

        ObservableList<Saida> list = saidaDao.listAll();
        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataHoraColuna, descricaoColuna, valorColuna, ultimaEdicaoColuna);

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
        TextField saidaBuscarPorDataTextField = new TextField();
        gridPane.add(saidaBuscarPorDataLabel,0,0);
        gridPane.add(saidaBuscarPorDataTextField,0,1);

        Text saidaBuscarPorDescricaoLabel = new Text("Buscar Saída por descrição: ");
        saidaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        TextField saidaBuscarPorDescricaoTextField = new TextField();
        saidaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(saidaBuscarPorDescricaoLabel,0,2);
        gridPane.add(saidaBuscarPorDescricaoTextField,0,3);

        gridPane.add(saidaTableView, 0, 5);

        return gridPane;
    }

}
