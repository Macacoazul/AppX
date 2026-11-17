package com.example.appx;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda os dados do aplicativo em memória (posts e assuntos do momento).
 * Todas as contas e conteúdos são fictícios, criados apenas para a atividade.
 */
public final class DadosApp {

    // Perfil do usuário logado (altere aqui para o seu nome)
    public static final String MEU_NOME = "Estudante Android";
    public static final String MEU_USUARIO = "@estudante_dev";
    public static final String MINHAS_INICIAIS = "EA";
    public static final int MINHA_COR = 0xFF1D9BF0;

    public static final String CATEGORIA_PARA_VOCE = "Para você";
    public static final String CATEGORIA_EM_ALTA = "Assuntos do Momento";
    public static final String[] CATEGORIAS = {
            CATEGORIA_PARA_VOCE, CATEGORIA_EM_ALTA, "Tecnologia", "Notícias", "Esportes", "Entretenimento"
    };

    private static final List<Post> POSTS = new ArrayList<>();
    private static final List<Assunto> ASSUNTOS = new ArrayList<>();
    private static int proximoId = 1;

    static {
        criarPosts();
        criarAssuntos();
    }

    private DadosApp() {
    }

    private static long minutosAtras(int minutos) {
        return System.currentTimeMillis() - minutos * 60_000L;
    }

    private static Post novoPost(String nome, String usuario, String iniciais, int cor, boolean verificado,
                                 int minutos, String texto, int imagem, boolean sigo,
                                 int respostas, int reposts, int curtidas, int visualizacoes) {
        return new Post(proximoId++, nome, usuario, iniciais, cor, verificado, minutosAtras(minutos),
                texto, imagem, sigo, respostas, reposts, curtidas, visualizacoes);
    }

    private static void criarPosts() {
        POSTS.add(novoPost("Dev Android BR", "@devandroidbr", "DA", 0xFF00BA7C, true, 25,
                "Dica do dia: use ConstraintLayout com Guidelines para posicionar os elementos de forma "
                        + "responsiva em telas de tamanhos diferentes. 📱 #AndroidStudio #Java",
                R.drawable.img_codigo, true, 48, 312, 2140, 45300));

        Post postAna = novoPost("Ana Souza", "@anasouza", "AS", 0xFFF91880, false, 62,
                "Finalmente terminei meu primeiro app no Android Studio! Duas Activities, Intent passando "
                        + "dados de uma tela para outra... me sentindo uma hacker 😂",
                0, true, 12, 8, 245, 3120);
        postAna.getListaRespostas().add(novoPost("Pedro Lima", "@pedrolima", "PL", 0xFFFF7A00, false, 50,
                "Parabéns!! Agora é só publicar na Play Store 🚀", 0, true, 0, 1, 14, 210));
        postAna.getListaRespostas().add(novoPost("Dev Android BR", "@devandroidbr", "DA", 0xFF00BA7C, true, 41,
                "Mandou bem, Ana! Próximo passo: RecyclerView 😉", 0, true, 1, 3, 37, 540));
        POSTS.add(postAna);

        POSTS.add(novoPost("Tech News Brasil", "@technewsbr", "TN", 0xFF7856FF, true, 130,
                "O interesse por desenvolvimento mobile segue em alta 📈 Quais linguagens você mais usa "
                        + "nos seus apps? Java, Kotlin, Flutter? Conta aqui 👇 #DesenvolvimentoMobile",
                R.drawable.img_grafico, false, 230, 1400, 8700, 512000));

        POSTS.add(novoPost("Lucas Ferreira", "@lucasf_dev", "LF", 0xFFFFAD1F, false, 185,
                "Quem nunca esqueceu de declarar a Activity no AndroidManifest.xml e ficou 30 minutos "
                        + "procurando o erro? 🙋‍♂️",
                0, false, 34, 120, 1800, 22000));

        POSTS.add(novoPost("Marina Costa", "@marinacosta", "MC", 0xFFE0245E, false, 300,
                "Pôr do sol de hoje visto da janela da faculdade. Às vezes a gente precisa parar de "
                        + "programar e olhar pro céu 🌅",
                R.drawable.img_por_do_sol, true, 21, 45, 980, 14200));

        POSTS.add(novoPost("Café com Código", "@cafecomcodigo", "CC", 0xFF8B5E3C, true, 420,
                "Lembrete: Intent explícita = você diz exatamente qual Activity abrir. Intent implícita = "
                        + "você diz a ação e o Android escolhe quem resolve. ☕",
                0, false, 15, 210, 1300, 19800));

        POSTS.add(novoPost("Pedro Lima", "@pedrolima", "PL", 0xFFFF7A00, false, 540,
                "Emulador ou celular com scrcpy? Comecei a usar o espelhamento de tela e meu PC agradeceu 😅",
                0, true, 40, 22, 410, 6500));

        POSTS.add(novoPost("Universidade Digital", "@unidigital", "UD", 0xFF1D9BF0, true, 720,
                "Inscrições abertas para a maratona de programação mobile! Monte seu time e participe. 🏆 "
                        + "#Maratona2026",
                0, false, 8, 95, 600, 25000));
    }

    private static void criarAssuntos() {
        ASSUNTOS.add(new Assunto("Tecnologia", "#AndroidStudio", 12400));
        ASSUNTOS.add(new Assunto("Notícias", "Black Friday", 98700));
        ASSUNTOS.add(new Assunto("Esportes", "Brasileirão", 76500));
        ASSUNTOS.add(new Assunto("Tecnologia", "Kotlin vs Java", 8900));
        ASSUNTOS.add(new Assunto("Entretenimento", "Show da Virada", 15300));
        ASSUNTOS.add(new Assunto("Notícias", "Enem 2026", 154000));
        ASSUNTOS.add(new Assunto("Esportes", "GP de São Paulo", 45200));
        ASSUNTOS.add(new Assunto("Tecnologia", "#DesenvolvimentoMobile", 5200));
        ASSUNTOS.add(new Assunto("Entretenimento", "Novela das Nove", 18100));
        ASSUNTOS.add(new Assunto("Notícias", "Previsão do tempo", 23000));
        ASSUNTOS.add(new Assunto("Esportes", "Seleção Brasileira", 132000));
        ASSUNTOS.add(new Assunto("Tecnologia", "ConstraintLayout", 2100));
        ASSUNTOS.add(new Assunto("Entretenimento", "#SextaDeFilme", 9800));
    }

    /** Todos os posts (aba "Para você"). Retorna uma cópia da lista. */
    public static List<Post> getPostsParaVoce() {
        return new ArrayList<>(POSTS);
    }

    /** Apenas os posts de contas que o usuário segue (aba "Seguindo"). */
    public static List<Post> getPostsSeguindo() {
        List<Post> resultado = new ArrayList<>();
        for (Post post : POSTS) {
            if (post.isDeQuemSigo()) {
                resultado.add(post);
            }
        }
        return resultado;
    }

    /** Procura um post (ou resposta) pelo id. */
    public static Post buscarPorId(int id) {
        for (Post post : POSTS) {
            if (post.getId() == id) {
                return post;
            }
            for (Post resposta : post.getListaRespostas()) {
                if (resposta.getId() == id) {
                    return resposta;
                }
            }
        }
        return null;
    }

    /** Publica um novo post do usuário no topo da linha do tempo. */
    public static Post publicar(String texto) {
        Post post = new Post(proximoId++, MEU_NOME, MEU_USUARIO, MINHAS_INICIAIS, MINHA_COR, false,
                System.currentTimeMillis(), texto, 0, true, 0, 0, 0, 0);
        POSTS.add(0, post);
        return post;
    }

    /** Cria uma resposta do usuário (não aparece na linha do tempo principal). */
    public static Post criarResposta(String texto) {
        return new Post(proximoId++, MEU_NOME, MEU_USUARIO, MINHAS_INICIAIS, MINHA_COR, false,
                System.currentTimeMillis(), texto, 0, true, 0, 0, 0, 0);
    }

    public static List<Assunto> getAssuntos() {
        return ASSUNTOS;
    }
}
