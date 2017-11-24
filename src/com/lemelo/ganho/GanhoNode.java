package com.lemelo.ganho;

import com.lemelo.cliente.Cliente;
import com.lemelo.cliente.ClienteDao;
import com.lemelo.util.Flag;
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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GanhoNode {
    private TextField dataTextField;
    private TableView<Ganho> tableView;
    private Flag flag;
    private ComboBox clienteComboBox;

    public Node executar(Tab ganhoTab) throws ParseException, SQLException {

        GridPane formularioGridPane = geraFormularioGridPane();

        GridPane botoesGridPane = geraBotoesGridPane();

        tableView = new TableView<>();
        TableView<Ganho> ganhoTableView = geraGanhoTableView();
        GridPane ganhoTableViewGridPane = geraGanhoTableViewGridPane(ganhoTableView);

        GridPane principalGridPane = geraPrincipalGridPane(formularioGridPane, botoesGridPane, ganhoTableViewGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0,2,0,2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraFormularioGridPane() throws ParseException, SQLException {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text dataLabel = new Text("Data: ");
        dataLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(dataLabel,0,0);
        dataTextField = new TextField();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        gridPane.add(dataTextField,0,1);

        Text clienteLabel = new Text("Cliente: ");
        clienteLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(clienteLabel,1,0);
        clienteComboBox = new ComboBox();
        clienteComboBox.setEditable(true);
        ClienteDao clienteDao = new ClienteDao();
        ObservableList<Cliente> clienteList = clienteDao.buscaClientes();
        clienteComboBox.setItems(clienteList);
        gridPane.add(clienteComboBox,1,1);

        return gridPane;
    }

    private GridPane geraBotoesGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Button salvarButton = new Button("Salvar");
        salvarButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(salvarButton,0,1);

        GanhoDao ganhoDao = new GanhoDao();

        salvarButton.setOnAction(event -> {
            System.out.println("Passei aqui: " + clienteComboBox.getValue());

            String dataStr = dataTextField.getText();

            //Pega dia da semana por extenso
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dataUtil = null;
            try {
                dataUtil = sdf1.parse(dataStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
            String diaSemanaStr = sdf2.format(dataUtil);

            Ganho ganho = new Ganho();
            ganho.setData(dataStr);
            ganho.setDiaSemana(diaSemanaStr);



            if(flag == Flag.EDITAR) {
                //TODO
            } {
                ganhoDao.insert(ganho);
                limpaFormulario();
                Platform.runLater(()->{
                    try {
                        geraGanhoTableView();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);

        return gridPane;
    }

    private void limpaFormulario() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
    }

    private TableView<Ganho> geraGanhoTableView() throws SQLException {
        TableColumn<Ganho, String> dataColuna = new TableColumn<>("Data");
        dataColuna.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Ganho, String> diaSemanaColuna = new TableColumn<>("Dia da Semana");
        diaSemanaColuna.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));

        GanhoDao ganhoDao = new GanhoDao();
        ObservableList<Ganho> list = ganhoDao.buscaPorMesAno();

        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(dataColuna,diaSemanaColuna);
        return tableView;
    }

    private GridPane geraGanhoTableViewGridPane(TableView<Ganho> ganhoTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        ganhoTableView.setPrefWidth(5000);
        gridPane.add(ganhoTableView,0,0);

        return gridPane;
    }

    private GridPane geraPrincipalGridPane(GridPane formularioGridPane, GridPane botoesGridPane, GridPane ganhoTableViewGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5,2,0,2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(formularioGridPane, 0,0);
        gridPane.add(botoesGridPane,0,1);
        gridPane.add(ganhoTableViewGridPane,0,2);

        return gridPane;
    }


}
