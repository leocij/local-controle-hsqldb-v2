package com.lemelo.entrada;

public class Entrada {
    private Integer id;
    private String dataHora;
    private String descricao;
    private String valor;
    private String ultimaEdicao;

    //@Atenção: Não transformar os métodos abaixo em PRIVATE.

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getUltimaEdicao() {
        return ultimaEdicao;
    }

    public void setUltimaEdicao(String ultimaEdicao) {
        this.ultimaEdicao = ultimaEdicao;
    }

    @Override
    public String toString() {
        return "Dt./Hr.: " + dataHora +
                "\nDescrição: " + descricao +
                "\nValor: " + valor +
                "\nÚltima edição: " + ultimaEdicao;
    }
}
