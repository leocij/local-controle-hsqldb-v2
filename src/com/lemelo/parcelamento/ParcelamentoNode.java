package com.lemelo.parcelamento;

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

public class ParcelamentoNode {
    private TextField parcelamentoBuscarPorDescricaoTextField;
    private TextField descricaoTextField;
    private TextField vencimentoTextField;
    private TextField valorTotalTextField;
    private TextField totalParcelaTextField;
    private String numeroDigitado;
    private String digitoNaParcela;
    private Flag flag;
    private TableView<Parcelamento> tableView;
    private TextField parcelamentoBuscarPorDataTextField;
    private TextField totalPagarTextField;
    private Tab parcelamentoTab;
    private TextField parcelamentoBuscarPorDescricaoSemPagarTextField;

    public Node executar(Tab parcelamentoTab) throws SQLException, ParseException {

        this.parcelamentoTab = parcelamentoTab;

        GridPane descricaoGridPane = geraDescricaoGridPane();

        GridPane formularioGridPane = geraFormularioGridPane();

        GridPane botoesGridPane = geraBotoesGridPane();

        tableView = new TableView<>();
        TableView<Parcelamento> parcelamentoTableView = geraParcelamentoTableView();
        GridPane parcelamentoTableViewGridPane = geraParcelamentoTableViewGridPane(parcelamentoTableView);

        manipulaTableView(tableView);

        GridPane principalGridPane = geraPrincipal(descricaoGridPane, formularioGridPane, botoesGridPane, parcelamentoTableViewGridPane);

        parcelamentoTab.setOnSelectionChanged(e->{
            if(parcelamentoTab.isSelected()==true) {
                Platform.runLater(()->descricaoTextField.requestFocus());
            }
        });

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(0, 2, 0, 2));
        vBox.getChildren().addAll(principalGridPane);

        return vBox;
    }

    private void manipulaTableView(TableView<Parcelamento> tableView) {
        tableView.setOnMousePressed(event->{
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Parcelamento parcelamento = tableView.getSelectionModel().getSelectedItem();
                if (parcelamento != null) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    ButtonType pagarButtonType = new ButtonType("Mudar Status");
                    ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert1.setHeaderText("O que deseja fazer com?\n\n" + parcelamento.toString());
                    alert1.getButtonTypes().setAll(pagarButtonType, cancelarButtonType);
                    alert1.showAndWait().ifPresent(escolha1->{
                        if(escolha1 == pagarButtonType) {
                            ParcelamentoDao parcelamentoDao = new ParcelamentoDao();
                            parcelamentoDao.update(parcelamento);

                            try {
                                geraParcelamentoTableView();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private GridPane geraPrincipal(GridPane descricaoGridPane, GridPane formularioGridPane, GridPane botoesGridPane, GridPane parcelamentoTableViewGridPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        gridPane.add(descricaoGridPane,0,0);
        gridPane.add(formularioGridPane, 0,1);
        gridPane.add(botoesGridPane, 0,2);
        gridPane.add(parcelamentoTableViewGridPane,0,3);

        return gridPane;
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

        Text valorTotalLabel = new Text("Valor Total: ");
        valorTotalLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(valorTotalLabel,1,0);
        valorTotalTextField = new TextField();
        gridPane.add(valorTotalTextField,1,1);
        valorTotalTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0")));

        //Posiciona o cursor no fim da linha
        valorTotalTextField.setOnMousePressed(event -> {
            Platform.runLater(()->valorTotalTextField.positionCaret(valorTotalTextField.getText().length()));
        });

        //Formatação em moeda corrente
        valorTotalTextField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            String numeroAntigoStr = valorTotalTextField.getText();
            if(e.getCode().isDigitKey()) {
                if(numeroDigitado==null) {
                    numeroDigitado = e.getText();
                } else {
                    numeroDigitado += e.getText();
                }
                Platform.runLater(()->valorTotalTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if(numeroDigitado==null){
                    Platform.runLater(()->valorTotalTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                } else {
                    numeroDigitado = removeUltimoDigito(numeroDigitado);
                    if(numeroDigitado.equals("") || numeroDigitado == null) {
                        Platform.runLater(()->valorTotalTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal("0"))));
                    } else {
                        Platform.runLater(()->valorTotalTextField.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).format(new BigDecimal(numeroDigitado).divide(new BigDecimal("100")))));
                    }
                }
            }
            else {
                //System.out.println(e);
                Platform.runLater(()->valorTotalTextField.setText(numeroAntigoStr));
            }

            //Coloca o cursor no fim da string
            Platform.runLater(()->valorTotalTextField.positionCaret(valorTotalTextField.getText().length()));
        });

        Text totalParcelaLabel = new Text("Total de Parcelas: ");
        totalParcelaLabel.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(totalParcelaLabel,2,0);
        totalParcelaTextField = new TextField();
        gridPane.add(totalParcelaTextField,2,1);
        totalParcelaTextField.setText("0");

        //Somente Digito
        totalParcelaTextField.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            String numeroAntigoStr = totalParcelaTextField.getText();
            if(e.getCode().isDigitKey()) {
                if(digitoNaParcela == null) {
                    digitoNaParcela = e.getText();
                } else {
                    digitoNaParcela += e.getText();
                }
                Platform.runLater(()->totalParcelaTextField.setText(digitoNaParcela));
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
                if(digitoNaParcela == null) {
                    Platform.runLater(()->totalParcelaTextField.setText("0"));
                } else {
                    digitoNaParcela = removeUltimoDigito(digitoNaParcela);
                    if(digitoNaParcela.equals("") || digitoNaParcela == null) {
                        Platform.runLater(()->totalParcelaTextField.setText("0"));
                    } else {
                        Platform.runLater(()->totalParcelaTextField.setText(digitoNaParcela));
                    }
                }
            } else {
                Platform.runLater(()->totalParcelaTextField.setText(numeroAntigoStr));
            }

            //Coloca o cursor no fim da string
            Platform.runLater(()->totalParcelaTextField.positionCaret(totalParcelaTextField.getText().length()));
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

        ParcelamentoDao parcelamentoDao = new ParcelamentoDao();

        salvarButton.defaultButtonProperty().bind(salvarButton.focusedProperty());

        salvarButton.setOnAction(event -> {
            String descricaoStr = descricaoTextField.getText();
            String vencimentoStr = vencimentoTextField.getText();
            String valorTotalStr = valorTotalTextField.getText();
            String totalParcelaStr = totalParcelaTextField.getText();

            Parcelamento parcelamento = new Parcelamento();
            parcelamento.setDescricao(descricaoStr);
            parcelamento.setVencimento(vencimentoStr);
            parcelamento.setValorTotal(valorTotalStr);
            parcelamento.setTotalParcela(totalParcelaStr);

            String valorTotalNf = null;
            try {
                valorTotalNf = NumberFormat.getCurrencyInstance().parse(valorTotalStr).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BigDecimal valorTotalBdc = new BigDecimal(valorTotalNf);

            BigDecimal totalParcelaBdc = new BigDecimal(totalParcelaStr);

            BigDecimal valorParcela;
            if(totalParcelaBdc.compareTo(BigDecimal.ZERO) > 0) {
                valorParcela = valorTotalBdc.divide(totalParcelaBdc, 2, RoundingMode.HALF_EVEN);
            } else {
                valorParcela = new BigDecimal(0);
            }
            String valorParcelaStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(valorParcela);
            parcelamento.setValorParcela(valorParcelaStr);

            parcelamento.setStatus("-");
            parcelamento.setDataAlteracao("-");

            if (flag == Flag.EDITAR) {
                //TODO
            } else {
                try {
                    parcelamentoDao.insert(parcelamento);
                    limpaFormulario();
                    Platform.runLater(() -> {
                        try {
                            geraParcelamentoTableView();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        });

        Button novoButton = new Button("Novo");
        novoButton.setStyle("-fx-font: normal bold 15px 'verdana' ");
        gridPane.add(novoButton,1,1);

        return gridPane;
    }

    private void limpaFormulario() {
        descricaoTextField.setText("");
        descricaoTextField.requestFocus();


        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        vencimentoTextField.setText(sdf1.format(Calendar.getInstance().getTime()));

        valorTotalTextField.setText("");
        totalParcelaTextField.setText("");

        numeroDigitado = "";
    }

    private TableView<Parcelamento> geraParcelamentoTableView() throws SQLException, ParseException {

        TableColumn<Parcelamento, String> descricaoColuna = new TableColumn<>("Descrição");
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Parcelamento, String> vencimentoColuna = new TableColumn<>("Vencimento");
        vencimentoColuna.setCellValueFactory(new PropertyValueFactory<>("vencimento"));

        TableColumn<Parcelamento, String> valorParcelaColuna = new TableColumn<>("Vlr Parcela");
        valorParcelaColuna.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));

        TableColumn<Parcelamento, String> valorTotalColuna = new TableColumn<>("Vlr Total");
        valorTotalColuna.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        TableColumn<Parcelamento, String> numeroParcelaColuna = new TableColumn<>("Nro Parcela");
        numeroParcelaColuna.setCellValueFactory(new PropertyValueFactory<>("numeroParcela"));

        TableColumn<Parcelamento, String> totalParcelaColuna = new TableColumn<>("Total Parcela");
        totalParcelaColuna.setCellValueFactory(new PropertyValueFactory<>("totalParcela"));

        TableColumn<Parcelamento, String> statusColuna = new TableColumn<>("Status");
        statusColuna.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Parcelamento, String> dataAlteracaoColuna = new TableColumn<>("Dt. Alteração");
        dataAlteracaoColuna.setCellValueFactory(new PropertyValueFactory<>("dataAlteracao"));

        ParcelamentoDao parcelamentoDao = new ParcelamentoDao();

        ObservableList<Parcelamento> list = parcelamentoDao.buscaPorMesAno();
        BigDecimal somaBdc = BigDecimal.ZERO;
        for(int i=0; i<list.size(); i++){
            String valorNf = NumberFormat.getCurrencyInstance().parse(list.get(i).getValorParcela()).toString();
            BigDecimal valorBdc = new BigDecimal(valorNf);
            somaBdc = somaBdc.add(valorBdc);
            String valorStr = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(somaBdc);
            Platform.runLater(()->totalPagarTextField.setText(valorStr));
        }

        tableView.getColumns().clear();
        tableView.setItems(list);
        tableView.getColumns().addAll(descricaoColuna, vencimentoColuna, valorParcelaColuna, valorTotalColuna, numeroParcelaColuna, totalParcelaColuna, statusColuna, dataAlteracaoColuna);

        Platform.runLater(()->parcelamentoBuscarPorDataTextField.textProperty().addListener((observable, oldValue, newValue)->{
            if(newValue.equals("")) {
                try {
                    geraParcelamentoTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ObservableList<Parcelamento> list1 = null;
                try {
                    list1 = parcelamentoDao.buscaPorVencimento(newValue);
                    BigDecimal somaBdc1 = BigDecimal.ZERO;
                    for(int i=0; i<list1.size(); i++){
                        String valorNf1 = NumberFormat.getCurrencyInstance().parse(list1.get(i).getValorParcela()).toString();
                        BigDecimal valorBdc1 = new BigDecimal(valorNf1);
                        somaBdc1 = somaBdc1.add(valorBdc1);
                        String valorStr1 = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(somaBdc1);
                        totalPagarTextField.setText(valorStr1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tableView.getColumns().clear();
                tableView.setItems(list1);
                tableView.getColumns().addAll(descricaoColuna, vencimentoColuna, valorParcelaColuna, valorTotalColuna, numeroParcelaColuna, totalParcelaColuna, statusColuna, dataAlteracaoColuna);
            }
        }));

        Platform.runLater(()->parcelamentoBuscarPorDescricaoTextField.textProperty().addListener((observable, oldValue, newValue)->{
            if(newValue.equals("")) {
                try {
                    geraParcelamentoTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ObservableList<Parcelamento> list1 = null;
                try {
                    list1 = parcelamentoDao.buscaPorDescricao(newValue);
                    BigDecimal somaBdc1 = BigDecimal.ZERO;
                    for (int i = 0; i < list1.size(); i++) {
                        String valorNf1 = NumberFormat.getCurrencyInstance().parse(list1.get(i).getValorParcela()).toString();
                        BigDecimal valorBdc1 = new BigDecimal(valorNf1);
                        somaBdc1 = somaBdc1.add(valorBdc1);
                        String valorStr1 = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(somaBdc1);
                        totalPagarTextField.setText(valorStr1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tableView.getColumns().clear();
                tableView.setItems(list1);
                tableView.getColumns().addAll(descricaoColuna, vencimentoColuna, valorParcelaColuna, valorTotalColuna, numeroParcelaColuna, totalParcelaColuna, statusColuna, dataAlteracaoColuna);
            }
        }));

        Platform.runLater(()->parcelamentoBuscarPorDescricaoSemPagarTextField.textProperty().addListener((observable, oldValue, newValue)->{
            if(newValue.equals("")) {
                try {
                    geraParcelamentoTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ObservableList<Parcelamento> list1 = null;
                try {
                    list1 = parcelamentoDao.buscaPorDescricaoSemPagar(newValue);
                    BigDecimal somaBdc1 = BigDecimal.ZERO;
                    for (int i = 0; i < list1.size(); i++) {
                        String valorNf1 = NumberFormat.getCurrencyInstance().parse(list1.get(i).getValorParcela()).toString();
                        BigDecimal valorBdc1 = new BigDecimal(valorNf1);
                        somaBdc1 = somaBdc1.add(valorBdc1);
                        String valorStr1 = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(somaBdc1);
                        totalPagarTextField.setText(valorStr1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tableView.getColumns().clear();
                tableView.setItems(list1);
                tableView.getColumns().addAll(descricaoColuna, vencimentoColuna, valorParcelaColuna, valorTotalColuna, numeroParcelaColuna, totalParcelaColuna, statusColuna, dataAlteracaoColuna);
            }
        }));

        return tableView;
    }

    private GridPane geraParcelamentoTableViewGridPane(TableView<Parcelamento> parcelamentoTableView) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 2, 0, 2));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.TOP_LEFT);

        Text parcelamentoBuscarPorDataLabel = new Text("Buscar Parcelamento por data: ");
        parcelamentoBuscarPorDataLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(parcelamentoBuscarPorDataLabel,0,1);
        parcelamentoBuscarPorDataTextField = new TextField();
        parcelamentoBuscarPorDataTextField.setPrefWidth(5000);
        gridPane.add(parcelamentoBuscarPorDataTextField,0,2);

        Text parcelamentoBuscarPorDescricaoLabel = new Text("Buscar Parcelamento por descrição: ");
        parcelamentoBuscarPorDescricaoLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(parcelamentoBuscarPorDescricaoLabel,0,3);
        parcelamentoBuscarPorDescricaoTextField = new TextField();
        parcelamentoBuscarPorDescricaoTextField.setPrefWidth(5000);
        gridPane.add(parcelamentoBuscarPorDescricaoTextField,0,4);

        Text parcelamentoBuscarPorDescricaoSemPagarLabel = new Text("Buscar Parcelamento sem pagar por descrição: ");
        parcelamentoBuscarPorDescricaoSemPagarLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(parcelamentoBuscarPorDescricaoSemPagarLabel,0,5);
        parcelamentoBuscarPorDescricaoSemPagarTextField = new TextField();
        parcelamentoBuscarPorDescricaoSemPagarTextField.setPrefWidth(5000);
        gridPane.add(parcelamentoBuscarPorDescricaoSemPagarTextField,0,6);

        Text totalPagarLabel = new Text("Total a pagar mediante a busca: ");
        totalPagarLabel.setStyle("-fx-font: normal bold 14px 'verdana' ");
        gridPane.add(totalPagarLabel,0,7);
        totalPagarTextField = new TextField();
        totalPagarTextField.setEditable(false);
        totalPagarTextField.setMaxWidth(100);
        gridPane.add(totalPagarTextField,0,8);

        gridPane.add(parcelamentoTableView,0,9);

        return gridPane;
    }

    private String removeUltimoDigito(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
