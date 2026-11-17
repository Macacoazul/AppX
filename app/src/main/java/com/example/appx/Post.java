package com.example.appx;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa um post (publicação) da linha do tempo.
 */
public class Post {

    private final int id;
    private final String nome;
    private final String usuario;
    private final String iniciais;
    private final int corAvatar;
    private final boolean verificado;
    private final long criadoEm;
    private final String texto;
    private final int imagemRes;
    private final boolean deQuemSigo;

    private int respostas;
    private int reposts;
    private int curtidas;
    private int visualizacoes;
    private int salvos;

    private boolean curtido;
    private boolean repostado;
    private boolean salvo;

    private final List<Post> listaRespostas = new ArrayList<>();

    public Post(int id, String nome, String usuario, String iniciais, int corAvatar,
                boolean verificado, long criadoEm, String texto, int imagemRes,
                boolean deQuemSigo, int respostas, int reposts, int curtidas, int visualizacoes) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.iniciais = iniciais;
        this.corAvatar = corAvatar;
        this.verificado = verificado;
        this.criadoEm = criadoEm;
        this.texto = texto;
        this.imagemRes = imagemRes;
        this.deQuemSigo = deQuemSigo;
        this.respostas = respostas;
        this.reposts = reposts;
        this.curtidas = curtidas;
        this.visualizacoes = visualizacoes;
        this.salvos = curtidas / 20;
    }

    public void alternarCurtida() {
        curtido = !curtido;
        curtidas += curtido ? 1 : -1;
    }

    public void alternarRepost() {
        repostado = !repostado;
        reposts += repostado ? 1 : -1;
    }

    public void alternarSalvo() {
        salvo = !salvo;
        salvos += salvo ? 1 : -1;
    }

    public void adicionarResposta(Post resposta) {
        listaRespostas.add(resposta);
        respostas++;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getUsuario() { return usuario; }
    public String getIniciais() { return iniciais; }
    public int getCorAvatar() { return corAvatar; }
    public boolean isVerificado() { return verificado; }
    public long getCriadoEm() { return criadoEm; }
    public String getTexto() { return texto; }
    public int getImagemRes() { return imagemRes; }
    public boolean isDeQuemSigo() { return deQuemSigo; }
    public int getRespostas() { return respostas; }
    public int getReposts() { return reposts; }
    public int getCurtidas() { return curtidas; }
    public int getVisualizacoes() { return visualizacoes; }
    public int getSalvos() { return salvos; }
    public boolean isCurtido() { return curtido; }
    public boolean isRepostado() { return repostado; }
    public boolean isSalvo() { return salvo; }
    public List<Post> getListaRespostas() { return listaRespostas; }
}
