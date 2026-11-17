package com.example.appx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Tela inicial: linha do tempo com as abas "Para você" e "Seguindo".
 */
public class MainActivity extends AppCompatActivity {

    private static final int ABA_PARA_VOCE = 0;
    private static final int ABA_SEGUINDO = 1;

    private TextView abaParaVoce;
    private TextView abaSeguindo;
    private View indicadorParaVoce;
    private View indicadorSeguindo;
    private ListView listaPosts;
    private FloatingActionButton fabNovoPost;
    private PostAdapter adapter;
    private int abaAtual = ABA_PARA_VOCE;

    // Abre a tela de novo post e recebe o texto digitado de volta (Intent com extra)
    private final ActivityResultLauncher<Intent> novoPostLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::aoVoltarDoNovoPost);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT), SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        abaParaVoce = findViewById(R.id.abaParaVoce);
        abaSeguindo = findViewById(R.id.abaSeguindo);
        indicadorParaVoce = findViewById(R.id.indicadorParaVoce);
        indicadorSeguindo = findViewById(R.id.indicadorSeguindo);
        listaPosts = findViewById(R.id.listaPosts);
        fabNovoPost = findViewById(R.id.fabNovoPost);

        TextView meuAvatar = findViewById(R.id.meuAvatar);
        Formatador.aplicarAvatar(meuAvatar, DadosApp.MINHAS_INICIAIS, DadosApp.MINHA_COR);

        // A ListView recebe um adapter com uma lista própria (cópia dos dados)
        adapter = new PostAdapter(this, new ArrayList<>());
        listaPosts.setAdapter(adapter);
        listaPosts.setOnItemClickListener((parent, view, position, id) -> abrirDetalhes(adapter.getItem(position)));

        abaParaVoce.setOnClickListener(v -> selecionarAba(ABA_PARA_VOCE));
        abaSeguindo.setOnClickListener(v -> selecionarAba(ABA_SEGUINDO));
        fabNovoPost.setOnClickListener(v -> novoPostLauncher.launch(new Intent(this, NovoPostActivity.class)));
        findViewById(R.id.logo).setOnClickListener(v -> rolarParaTopo());
        meuAvatar.setOnClickListener(v -> Toast.makeText(this,
                DadosApp.MEU_NOME + " · " + DadosApp.MEU_USUARIO, Toast.LENGTH_SHORT).show());
        findViewById(R.id.btnConfig).setOnClickListener(v ->
                Toast.makeText(this, R.string.configuracoes, Toast.LENGTH_SHORT).show());

        BarraNavegacao.configurar(this, BarraNavegacao.INICIO, this::rolarParaTopo);
        selecionarAba(ABA_PARA_VOCE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza curtidas/reposts que possam ter sido alterados na tela de detalhes
        adapter.notifyDataSetChanged();
        BarraNavegacao.configurar(this, BarraNavegacao.INICIO, this::rolarParaTopo);
    }

    private void selecionarAba(int aba) {
        abaAtual = aba;
        boolean paraVoce = aba == ABA_PARA_VOCE;
        estilizarAba(abaParaVoce, paraVoce);
        estilizarAba(abaSeguindo, !paraVoce);
        indicadorParaVoce.setVisibility(paraVoce ? View.VISIBLE : View.INVISIBLE);
        indicadorSeguindo.setVisibility(paraVoce ? View.INVISIBLE : View.VISIBLE);

        List<Post> posts = paraVoce ? DadosApp.getPostsParaVoce() : DadosApp.getPostsSeguindo();
        adapter.clear();
        adapter.addAll(posts);
        listaPosts.setSelection(0);
        listaPosts.setAlpha(0f);
        listaPosts.animate().alpha(1f).setDuration(220).start();
    }

    private void estilizarAba(TextView aba, boolean ativa) {
        aba.setTextColor(ContextCompat.getColor(this, ativa ? R.color.x_texto : R.color.x_cinza));
        aba.setTypeface(Typeface.DEFAULT, ativa ? Typeface.BOLD : Typeface.NORMAL);
    }

    private void rolarParaTopo() {
        listaPosts.smoothScrollToPosition(0);
    }

    private void abrirDetalhes(Post post) {
        if (post == null) return;
        Intent intent = new Intent(this, DetalhePostActivity.class);
        intent.putExtra(DetalhePostActivity.EXTRA_ID_POST, post.getId());
        startActivity(intent);
    }

    private void aoVoltarDoNovoPost(ActivityResult resultado) {
        if (resultado.getResultCode() != RESULT_OK || resultado.getData() == null) return;

        String texto = resultado.getData().getStringExtra(NovoPostActivity.EXTRA_TEXTO);
        if (texto == null || texto.trim().isEmpty()) return;

        DadosApp.publicar(texto.trim());
        selecionarAba(abaAtual);
        Snackbar.make(findViewById(R.id.main), R.string.post_enviado, Snackbar.LENGTH_SHORT)
                .setAnchorView(fabNovoPost)
                .show();
    }
}
