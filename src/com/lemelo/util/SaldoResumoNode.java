package com.lemelo.util;

import com.lemelo.entrada.Entrada;
import com.lemelo.entrada.EntradaDao;
import com.lemelo.saida.Saida;
import com.lemelo.saida.SaidaDao;
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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaldoResumoNode{

    public Node executar(Tab saldoResumoTab) throws SQLException {

        GridPane cabecalhoGridPane = geraCabecalhoGridPane();

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

    private GridPane geraCabecalhoGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataAtualLabel = new Text("Data Atual: ");
        dataAtualLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataAtualLabel, 0, 0);
        TextField dataAtualTextField = new TextField();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataAtualTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        dataAtualTextField.setFocusTraversable(false);
        dataAtualTextField.setEditable(false);
        gridPane.add(dataAtualTextField, 0, 1);

        Text saldoLabel = new Text("Saldo: ");
        saldoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(saldoLabel, 0, 2);
        TextField saldoTextField = new TextField();
        gridPane.add(saldoTextField,0,3);

        Text ganhoRealLabel = new Text("Ganho real: ");
        ganhoRealLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(ganhoRealLabel, 1, 2);
        TextField ganhoRealTextField = new TextField();
        gridPane.add(ganhoRealTextField, 1, 3);

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
        entradaBuscarPorDataLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        TextField entradaBuscarPorDataTextField = new TextField();
        gridPane.add(entradaBuscarPorDataLabel,0,0);
        gridPane.add(entradaBuscarPorDataTextField,0,1);

        Text entradaBuscarPorDescricaoLabel = new Text("Buscar Entrada por descrição: ");
        entradaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
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
        saidaBuscarPorDataLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        TextField saidaBuscarPorDataTextField = new TextField();
        gridPane.add(saidaBuscarPorDataLabel,0,0);
        gridPane.add(saidaBuscarPorDataTextField,0,1);

        Text saidaBuscarPorDescricaoLabel = new Text("Buscar Saída por descrição: ");
        saidaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        TextField saidaBuscarPorDescricaoTextField = new TextField();
        saidaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(saidaBuscarPorDescricaoLabel,0,2);
        gridPane.add(saidaBuscarPorDescricaoTextField,0,3);

        gridPane.add(saidaTableView, 0, 5);

        return gridPane;
    }
}
