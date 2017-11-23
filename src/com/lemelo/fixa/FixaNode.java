package com.lemelo.fixa;

import com.lemelo.parcelamento.Parcelamento;
import com.lemelo.parcelamento.ParcelamentoDao;
import com.lemelo.util.Flag;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FixaNode {
    private TextField descricaoTextField;
    private TextField vencimentoTextField;
    private String numeroDigitado;
    private TextField valorTextField;
    private Flag flag;
    private TableView<Fixa> tableView;

    public Node executar(Tab fixaTab) throws SQLException {

        GridPane descricaoGridPane = geraDescricaoGridPane();

        GridPane formularioGridPane = geraFormularioGridPane();

        GridPane botoesGridPane = geraBotoesGridPane();

        tableView = new TableView<>();
        TableView<Fixa> fixaTableView = geraFixaTableView();
        GridPane fixaTableViewGridPane = geraFixaTableViewGridPane(fixaTableView);

        GridPane principalGridPane = geraPrincipal(descricaoGridPane, formularioGridPane, botoesGridPane, fixaTableViewGridPane);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private GridPane geraDescricaoGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text descricaoLabel = new Text("Descrição: ");
        descricaoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(descricaoLabel,0,0);
        descricaoTextField = new TextField();
        descricaoTextField.setPrefWidth(5000);
        gridPane.add(descricaoTextField,0,1);

        return gridPane;
    }

    private GridPane geraFormularioGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text vencimentoLabel = new Text("Vencimento: ");
        vencimentoLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(vencimentoLabel,0,0);
        vencimentoTextField = new TextField();
        gridPane.add(vencimentoTextField,0,1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        vencimentoTextField.setText(sdf1.format(Calendar.getInstance().getTime()));

        Text valorLabel = new Text("Valor: ");
        valorLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorLabel, 1, 0);
        valorTextField = new TextField();
        gridPane.add(valorTextField, 1, 1);
        valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0")));

        //Posiciona o cursor no fim da linha
        valorTextField.setOnMousePressed(event -> {
            Platform.runLater(()->valorTextField.positionCaret(valorTextField.getText().length()));
        });

        //Formatação em moeda corrente
        valorTextField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            String numeroAntigoStr = valorTextField.getText();
            if(e.getCode().isDigitKey()) {
                if(numeroDigitado==null) {
                    numeroDigitado = e.getText();
                } else {
                    numeroDigitado += e.getText();
                }
                Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if(numeroDigitado==null){
                    Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                } else {
                    numeroDigitado = removeUltimoDigito(numeroDigitado);
                    if(numeroDigitado.equals("") || numeroDigitado == null) {
                        Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                    } else {
                        Platform.runLater(()->valorTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
                    }
                }
            }
            else {
                //System.out.println(e);
                Platform.runLater(()->valorTextField.setText(numeroAntigoStr));
            }

            //Coloca o cursor no fim da string
            Platform.runLater(()->valorTextField.positionCaret(valorTextField.getText().length()));
        });

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

        FixaDao fixaDao = new FixaDao();

        salvarButton.setOnAction(event -> {
            String descricaoStr = descricaoTextField.getText();
            String vencimentoStr = vencimentoTextField.getText();
            String valorStr = valorTextField.getText();

            Fixa fixa = new Fixa();
            fixa.setDescricao(descricaoStr);
            fixa.setVencimento(vencimentoStr);
            fixa.setValor(valorStr);

            if (flag == Flag.EDITAR) {
                //TODO
            } else {
                fixaDao.insert(fixa);
                limpaFormulario();
                Platform.runLater(() -> {
                    try {
                        geraFixaTableView();
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
        descricaoTextField.setText("");
        vencimentoTextField.setText("");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        vencimentoTextField.setText(sdf1.format(Calendar.getInstance().getTime()));
        valorTextField.setText("");
    }

    private TableView<Fixa> geraFixaTableView() throws SQLException {
        TableColumn<Fixa, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Fixa, String> vencimentoColuna = new TableColumn<>("Vencimento");
        vencimentoColuna.setCellValueFactory(new PropertyValueFactory<>("vencimento"));

        TableColumn<Fixa, String> valorColuna = new TableColumn<>("Valor");
        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));

        FixaDao fixaDao = new FixaDao();
        ObservableList<Fixa> list = fixaDao.buscarPorMesAno();

        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(descricaoColuna, vencimentoColuna, valorColuna);

        return tableView;
    }

    private GridPane geraFixaTableViewGridPane(TableView<Fixa> fixaTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        fixaTableView.setPrefWidth(5000);
        gridPane.add(fixaTableView,0,0);

        return gridPane;
    }

    private GridPane geraPrincipal(GridPane descricaoGridPane, GridPane formularioGridPane, GridPane botoesGridPane, GridPane fixaTableViewGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(descricaoGridPane,0,0);
        gridPane.add(formularioGridPane, 0,1);
        gridPane.add(botoesGridPane, 0,2);
        gridPane.add(fixaTableViewGridPane,0,3);

        return gridPane;
    }

    private String removeUltimoDigito(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
