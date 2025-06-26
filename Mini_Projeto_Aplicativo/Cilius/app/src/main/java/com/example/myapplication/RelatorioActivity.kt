package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RelatorioActivity : AppCompatActivity() {

    companion object {
        /** Chave comum para Intent extra */
        const val EXTRA_CONDICAO = "condicao_planta"
    }
    /** Dicionário: chave = condição   valor = texto detalhado */
    private val relatorios: Map<String, String> by lazy {
        mapOf(
            "Crítica – Com risco iminente de queda" to """
                **Crítica – Risco iminente de queda**
                A árvore apresenta estruturas comprometidas, como tronco oco ou raiz exposta.
                ➡️ Risco elevado de acidentes.
                ✅ Recomenda-se remoção ou contenção emergencial.
            """.trimIndent(),

            "Urgente – Retirada, afetando gravemente a localidade" to """
                **Urgente – Retirada necessária**
                A árvore causa bloqueio de vias ou danos graves a construções.
                ➡️ Impacto severo na localidade.
                ✅ Retirada imediata e substituição planejada.
            """.trimIndent(),

            "Grave – Está afetando a rede elétrica e causando dano à rua e casas" to """
                **Grave – Atingindo rede elétrica e imóveis**
                Galhos tocam fiação; raízes danificam calçadas e casas.
                ➡️ Risco de curto‑circuito e danos estruturais.
                ✅ Poda/remoção urgente.
            """.trimIndent(),

            "Alerta – Está próxima da rede elétrica e começando a danificar a rua" to """
                **Alerta – Próxima da rede elétrica**
                Galhos e raízes iniciam contato com infraestrutura.
                ➡️ Situação pode evoluir.
                ✅ Agendar poda preventiva e monitorar.
            """.trimIndent(),

            "Instável – Apresenta inclinação acentuada ou raízes expostas" to """
                **Instável – Inclinação acentuada / raízes expostas**
                Possibilidade de queda em ventos fortes.
                ✅ Avaliar reforço ou remoção preventiva.
            """.trimIndent(),

            "Poda Imediata- Arvore de porte adequado para retirada" to """
                **Poda Imediata – Porte adequado para intervenção**
                A árvore interfere em visibilidade ou circulação.
                ✅ Realizar poda ou remoção parcial o quanto antes.
            """.trimIndent(),

            "Estável – Planta saudável, mas necessita poda preventiva" to """
                **Estável – Saudável, mas exige poda preventiva**
                Galhos extensos podem futuramente tocar estruturas.
                ✅ Manutenção programada.
            """.trimIndent(),

            "Doente – Apresenta sinais de doenças foliares ou no tronco" to """
                **Doente – Sinais de pragas ou fungos**
                Manchas foliares, casca solta ou galhos secos.
                ✅ Diagnóstico fitossanitário e tratamento.
            """.trimIndent(),

            "Regular – Condição regular, acompanhar crescimento" to """
                **Regular – Condição intermediária**
                Alguns sinais de desgaste; sem risco imediato.
                ✅ Reavaliar em 6 meses.
            """.trimIndent(),

            "Saudavel – Planta em boas condições, sem necessidade de intervenções" to """
                **Saudável – Sem necessidade de intervenção**
                Crescimento equilibrado e sem doenças aparentes.
            """.trimIndent(),

            "Em Crescimento – Desenvolvendo-se normalmente" to """
                **Em Crescimento – Desenvolvimento normal**
                Planta jovem; manter irrigação e monitoramento.
            """.trimIndent(),

            "Recém-plantada - ainda em fase de adaptação" to """
                **Recém‑plantada – Fase de adaptação**
                Primeiro trimestre crítico: irrigar e proteger.
            """.trimIndent(),

            "Removida – Arvore removida da localidade" to """
                **Removida – Árvore retirada**
                Área livre de riscos imediatos.
                ✅ Registrar e planejar reposição arbórea.
            """.trimIndent()
        )
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_relatorio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val condicaoRecebida = intent.getStringExtra(EXTRA_CONDICAO)
        val textoRelatorio = relatorios[condicaoRecebida] ?: "Condicão Desconhecida. Na verdade nem era pra chegar aqui."
        findViewById<TextView>(R.id.textViewRelatorio).setText(textoRelatorio)

    }
}