package com.lemelo.relatorio;

import com.lemelo.cliente.Cliente;
import com.lemelo.saida.Saida;
import com.lemelo.saida.SaidaDao;
import com.lemelo.util.FabricaConexao;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RelatorioNode {
    private TextField saidaBuscarPorDescricaoTextField;

    public Node executar(Tab relatorioTab) {

        TableView<Saida> saidaTableView = geraSaidaTableView();
        GridPane saidaGridPane = geraSaidaGridPane(saidaTableView);

        GridPane principalGridPane = geraPrincipal(saidaGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraSaidaGridPane(TableView<Saida> saidaTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text saidaBuscarPorDescricaoLabel = new Text("Buscar Saída por descrição: ");
        saidaBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        saidaBuscarPorDescricaoTextField = new TextField();
        saidaBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(saidaBuscarPorDescricaoLabel,0,2);
        gridPane.add(saidaBuscarPorDescricaoTextField,0,3);

        gridPane.add(saidaTableView, 0, 5);

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

    private GridPane geraPrincipal(GridPane saidaGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(saidaGridPane,0,0);

        return gridPane;
    }

}
