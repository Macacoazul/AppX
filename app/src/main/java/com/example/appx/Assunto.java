package com.example.appx;

/**
 * Modelo de um "Assunto do Momento" exibido na tela Explorar.
 */
public class Assunto {

    private final String categoria;
    private final String titulo;
    private final int quantidadePosts;

    public Assunto(String categoria, String titulo, int quantidadePosts) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.quantidadePosts = quantidadePosts;
    }

    public String getCategoria() { return categoria; }
    public String getTitulo() { return titulo; }
    public int getQuantidadePosts() { return quantidadePosts; }
}
