package com.lemelo.parcelamento;

public class Parcelamento {
    private Integer id;
    private String descricao;
    private String vencimento;
    private String valorParcela;
    private String valorTotal;
    private String numeroParcela;
    private String totalParcela;
    private String status;
    private String dataAlteracao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(String valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(String numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public String getTotalParcela() {
        return totalParcela;
    }

    public void setTotalParcela(String totalParcela) {
        this.totalParcela = totalParcela;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(String dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    @Override
    public String toString() {
        return "Descrição: " + descricao +
                "\nVencimento: " + vencimento +
                "\nValor da Parcela: " + valorParcela +
                "\nValor Total: " + valorTotal +
                "\nNúmero da Parcela: " + numeroParcela +
                "\nTotal de Parcelas: " + totalParcela +
                "\nStatus: " + status +
                "\nData de Alteração: " + dataAlteracao;
    }
}
