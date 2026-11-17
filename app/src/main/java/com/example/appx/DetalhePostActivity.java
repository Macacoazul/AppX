package com.example.appx;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

/**
 * Tela de detalhes de um post. Recebe o id do post por meio de um extra do Intent.
 */
public class DetalhePostActivity extends AppCompatActivity {

    public static final String EXTRA_ID_POST = "id_post";
    public static final String EXTRA_FOCAR_RESPOSTA = "focar_resposta";

    private Post post;
    private ImageView acaoRepostar;
    private ImageView acaoCurtir;
    private ImageView acaoSalvar;
    private TextView estatisticas;
    private LinearLayout containerRespostas;
    private TextView semRespostas;
    private EditText campoResposta;
    private MaterialButton btnResponder;
    private ScrollView rolagem;

    private int corTexto;
    private int corCinza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT), SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_detalhe_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        // Recupera o id enviado pela tela anterior
        int id = getIntent().getIntExtra(EXTRA_ID_POST, -1);
        post = DadosApp.buscarPorId(id);
        if (post == null) {
            finish();
            return;
        }

        corTexto = ContextCompat.getColor(this, R.color.x_texto);
        corCinza = ContextCompat.getColor(this, R.color.x_cinza);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rolagem = findViewById(R.id.rolagem);
        estatisticas = findViewById(R.id.estatisticas);
        containerRespostas = findViewById(R.id.containerRespostas);
        semRespostas = findViewById(R.id.semRespostas);
        campoResposta = findViewById(R.id.campoResposta);
        btnResponder = findViewById(R.id.btnResponder);
        acaoRepostar = findViewById(R.id.acaoRepostar);
        acaoCurtir = findViewById(R.id.acaoCurtir);
        acaoSalvar = findViewById(R.id.acaoSalvar);

        preencherCabecalho();
        configurarAcoes();
        configurarResposta();
        atualizarAcoes();
        atualizarEstatisticas();
        mostrarRespostas();

        if (getIntent().getBooleanExtra(EXTRA_FOCAR_RESPOSTA, false)) {
            focarResposta();
        }
    }

    private void preencherCabecalho() {
        Formatador.aplicarAvatar(findViewById(R.id.avatar), post.getIniciais(), post.getCorAvatar());
        Formatador.aplicarAvatar(findViewById(R.id.avatarResposta), DadosApp.MINHAS_INICIAIS, DadosApp.MINHA_COR);

        ((TextView) findViewById(R.id.nome)).setText(post.getNome());
        ((TextView) findViewById(R.id.usuario)).setText(post.getUsuario());
        findViewById(R.id.verificado).setVisibility(post.isVerificado() ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.texto)).setText(
                Formatador.destacar(post.getTexto(), ContextCompat.getColor(this, R.color.x_azul)));

        ImageView imagem = findViewById(R.id.imagem);
        if (post.getImagemRes() != 0) {
            imagem.setImageResource(post.getImagemRes());
            imagem.setVisibility(View.VISIBLE);
        } else {
            imagem.setVisibility(View.GONE);
        }

        SpannableStringBuilder data = new SpannableStringBuilder();
        data.append(Formatador.dataCompleta(post.getCriadoEm())).append(" · ");
        adicionarNegrito(data, Formatador.contarSempre(post.getVisualizacoes()));
        data.append(" Visualizações");
        ((TextView) findViewById(R.id.dataHora)).setText(data);

        findViewById(R.id.btnMais).setOnClickListener(v ->
                Toast.makeText(this, R.string.mais_opcoes, Toast.LENGTH_SHORT).show());
    }

    private void configurarAcoes() {
        findViewById(R.id.acaoResponder).setOnClickListener(v -> focarResposta());

        acaoRepostar.setOnClickListener(v -> {
            post.alternarRepost();
            atualizarAcoes();
            atualizarEstatisticas();
            if (post.isRepostado()) Formatador.pular(acaoRepostar);
        });

        acaoCurtir.setOnClickListener(v -> {
            post.alternarCurtida();
            atualizarAcoes();
            atualizarEstatisticas();
            if (post.isCurtido()) Formatador.pular(acaoCurtir);
        });

        acaoSalvar.setOnClickListener(v -> {
            post.alternarSalvo();
            atualizarAcoes();
            atualizarEstatisticas();
            Toast.makeText(this, post.isSalvo() ? R.string.adicionado_salvos : R.string.removido_salvos,
                    Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.acaoCompartilhar).setOnClickListener(v -> Formatador.compartilhar(this, post));
    }

    private void configurarResposta() {
        campoResposta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean temTexto = !s.toString().trim().isEmpty();
                btnResponder.setEnabled(temTexto);
                btnResponder.setAlpha(temTexto ? 1f : 0.5f);
            }
        });

        btnResponder.setOnClickListener(v -> responder());
    }

    private void responder() {
        String texto = campoResposta.getText().toString().trim();
        if (texto.isEmpty()) return;

        post.adicionarResposta(DadosApp.criarResposta(texto));
        campoResposta.setText("");
        campoResposta.clearFocus();
        WindowCompat.getInsetsController(getWindow(), campoResposta).hide(WindowInsetsCompat.Type.ime());

        mostrarRespostas();
        Toast.makeText(this, R.string.resposta_enviada, Toast.LENGTH_SHORT).show();
        rolagem.postDelayed(() -> rolagem.smoothScrollTo(0, containerRespostas.getBottom()), 300);
    }

    private void focarResposta() {
        campoResposta.requestFocus();
        campoResposta.post(() -> WindowCompat.getInsetsController(getWindow(), campoResposta)
                .show(WindowInsetsCompat.Type.ime()));
    }

    /** Reaproveita o PostAdapter para desenhar cada resposta dentro do LinearLayout. */
    private void mostrarRespostas() {
        containerRespostas.removeAllViews();
        PostAdapter adapterRespostas = new PostAdapter(this, post.getListaRespostas());
        for (int i = 0; i < adapterRespostas.getCount(); i++) {
            containerRespostas.addView(adapterRespostas.getView(i, null, containerRespostas));
            View divisor = new View(this);
            divisor.setBackgroundColor(ContextCompat.getColor(this, R.color.x_divisor));
            containerRespostas.addView(divisor, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1));
        }
        semRespostas.setVisibility(post.getListaRespostas().isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void atualizarAcoes() {
        acaoCurtir.setImageResource(post.isCurtido() ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
        acaoCurtir.setImageTintList(ColorStateList.valueOf(post.isCurtido()
                ? ContextCompat.getColor(this, R.color.x_rosa) : corCinza));

        acaoRepostar.setImageTintList(ColorStateList.valueOf(post.isRepostado()
                ? ContextCompat.getColor(this, R.color.x_verde) : corCinza));

        acaoSalvar.setImageResource(post.isSalvo() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        acaoSalvar.setImageTintList(ColorStateList.valueOf(post.isSalvo()
                ? ContextCompat.getColor(this, R.color.x_azul) : corCinza));
    }

    /** Ex.: "312 Reposts   2,1 mil Curtidas   107 Itens salvos" com números em negrito. */
    private void atualizarEstatisticas() {
        SpannableStringBuilder texto = new SpannableStringBuilder();
        adicionarNegrito(texto, Formatador.contarSempre(post.getReposts()));
        texto.append(" Reposts    ");
        adicionarNegrito(texto, Formatador.contarSempre(post.getCurtidas()));
        texto.append(" Curtidas    ");
        adicionarNegrito(texto, Formatador.contarSempre(post.getSalvos()));
        texto.append(" Itens salvos");
        estatisticas.setText(texto);
    }

    private void adicionarNegrito(SpannableStringBuilder builder, String valor) {
        int inicio = builder.length();
        builder.append(valor);
        builder.setSpan(new StyleSpan(Typeface.BOLD), inicio, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(corTexto), inicio, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
