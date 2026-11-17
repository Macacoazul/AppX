package com.example.appx;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Configura a barra de navegação inferior (incluída com <include> nas telas).
 */
public final class BarraNavegacao {

    public static final int INICIO = 0;
    public static final int EXPLORAR = 1;

    private static boolean notificacoesLidas = false;

    private BarraNavegacao() {
    }

    /**
     * @param activity       tela atual
     * @param abaAtual       INICIO ou EXPLORAR
     * @param aoTocarNaAtual ação executada ao tocar novamente na aba atual (ex.: rolar para o topo)
     */
    public static void configurar(Activity activity, int abaAtual, Runnable aoTocarNaAtual) {
        ImageView iconeInicio = activity.findViewById(R.id.iconeInicio);
        iconeInicio.setImageResource(abaAtual == INICIO ? R.drawable.ic_home_filled : R.drawable.ic_home);

        View badge = activity.findViewById(R.id.badgeNotificacoes);
        badge.setVisibility(notificacoesLidas ? View.GONE : View.VISIBLE);

        activity.findViewById(R.id.navInicio).setOnClickListener(v -> {
            if (abaAtual == INICIO) {
                aoTocarNaAtual.run();
            } else {
                activity.finish(); // volta para a MainActivity
            }
        });

        activity.findViewById(R.id.navBuscar).setOnClickListener(v -> {
            if (abaAtual == EXPLORAR) {
                aoTocarNaAtual.run();
            } else {
                activity.startActivity(new Intent(activity, ExplorarActivity.class));
            }
        });

        activity.findViewById(R.id.navComunidades).setOnClickListener(v -> emBreve(activity, R.string.nav_comunidades));
        activity.findViewById(R.id.navMensagens).setOnClickListener(v -> emBreve(activity, R.string.nav_mensagens));

        activity.findViewById(R.id.navNotificacoes).setOnClickListener(v -> {
            if (!notificacoesLidas) {
                notificacoesLidas = true;
                badge.setVisibility(View.GONE);
                Toast.makeText(activity, R.string.notificacoes_lidas, Toast.LENGTH_SHORT).show();
            } else {
                emBreve(activity, R.string.nav_notificacoes);
            }
        });
    }

    private static void emBreve(Activity activity, int nomeRes) {
        Toast.makeText(activity, activity.getString(R.string.em_breve, activity.getString(nomeRes)),
                Toast.LENGTH_SHORT).show();
    }
}
