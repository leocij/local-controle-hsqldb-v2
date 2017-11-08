package com.lemelo.entrada;

public class Entrada {
    private Integer id;
    private String dataHora;
    private String descricao;
    private String valor;

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

    @Override
    public String toString() {
        return "Entrada{" +
                "id=" + id +
                ", dataHora='" + dataHora + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
