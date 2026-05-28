import java.util.List;
import java.util.stream.Collectors;

/**
 * Seleciona e executa a trajetória correta para um ponto destino (xf, yf, af).
 *
 * Prioridade de seleção:
 *   1. Traj1 — arco-esq + reta + arco-esq  (af grande, linha de chegada corta Xi positivo)
 *   2. Traj2 — arco-esq + reta + arco-dir  (af pequeno/positivo, yc2 < yc1)
 *   3. Traj3 — arco-esq + reta + arco-dir  (af negativo, yc2 > yc1)
 */
public class GestorTrajetoria {

    // Parâmetros físicos do robô (valores por defeito dos enunciados)
    private static final double DBW    = 9.5;  // cm
    private static final double VMAX   = 80;

    private final MyRobotLegoEV3 robot;
    private final Traj1 traj1;
    private final Traj2 traj2;
    private final Traj3 traj3;

    public GestorTrajetoria(MyRobotLegoEV3 robot) {
        this.robot = robot;
        // Trajetória 1 usa vrobot=40, vmin=20 (PDF enunciado 1)
        this.traj1 = new Traj1(DBW, 40, 20, VMAX);
        // Trajetórias 2 e 3 usam vrobot=30, vmin=15 (PDF enunciados 2/3)
        this.traj2 = new Traj2(DBW, 30, 15, VMAX);
        this.traj3 = new Traj3(DBW, 30, 15, VMAX);
    }

    /**
     * Calcula os passos práticos para atingir (xf, yf, af).
     * Tenta primeiro a trajetória direta; se não for aplicável, tenta a versão
     * espelhada (reflexão sobre o eixo Xi: yf→-yf, af→-af, curvas trocadas).
     * Devolve lista vazia se nenhuma opção for aplicável.
     */
    public List<Passo> calcular(double xf, double yf, double af) {
        // --- trajetória direta ---
        if (traj1.aplica(xf, yf, af)) {
            traj1.calcularTeorica(xf, yf, af);
            return traj1.calcularPratica(xf, yf, af);
        }
        if (traj2.aplica(xf, yf, af)) {
            traj2.calcularTeorica(xf, yf, af);
            return traj2.calcularPratica(xf, yf, af);
        }
        if (traj3.aplica(xf, yf, af)) {
            traj3.calcularTeorica(xf, yf, af);
            return traj3.calcularPratica(xf, yf, af);
        }
        // --- trajetória espelhada: reflexão sobre Xi (yf→-yf, af→-af) ---
        double xfM = xf, yfM = -yf, afM = af;
        if (traj1.aplica(xfM, yfM, afM)) {
            System.out.printf("(espelhada: calculado para (%.1f, %.1f, %.1f))%n", xfM, yfM, afM);
            traj1.calcularTeorica(xfM, yfM, afM);
            return espelharPassos(traj1.calcularPratica(xfM, yfM, afM));
        }
        if (traj2.aplica(xfM, yfM, afM)) {
            System.out.printf("(espelhada: calculado para (%.1f, %.1f, %.1f))%n", xfM, yfM, afM);
            traj2.calcularTeorica(xfM, yfM, afM);
            return espelharPassos(traj2.calcularPratica(xfM, yfM, afM));
        }
        if (traj3.aplica(xfM, yfM, afM)) {
            System.out.printf("(espelhada: calculado para (%.1f, %.1f, %.1f))%n", xfM, yfM, afM);
            traj3.calcularTeorica(xfM, yfM, afM);
            return espelharPassos(traj3.calcularPratica(xfM, yfM, afM));
        }
        System.out.printf("GestorTrajetoria: nenhuma trajetória aplicável para (%.1f, %.1f, %.1f)%n", xf, yf, af);
        return List.of();
    }

    /** Inverte o sentido de todas as curvas de uma trajetória (esquerda↔direita). */
    public List<Passo> espelharPassos(List<Passo> passos) {
        return passos.stream().map(Passo::espelhar).collect(Collectors.toList());
    }

    /** Enfileira os passos no robô para execução assíncrona. */
    public void executar(List<Passo> passos) {
        for (Passo p : passos) {
            robot.add_queue(p);
        }
    }

    // --- Acesso direto às trajetórias individuais (útil para testes/debug) ---

    public List<Passo> calcularTeorica(double xf, double yf, double af) {
        if (traj1.aplica(xf, yf, af)) return traj1.calcularTeorica(xf, yf, af);
        if (traj2.aplica(xf, yf, af)) return traj2.calcularTeorica(xf, yf, af);
        if (traj3.aplica(xf, yf, af)) return traj3.calcularTeorica(xf, yf, af);
        // tenta espelhada
        double xfM = xf, yfM = -yf, afM = af;
        if (traj1.aplica(xfM, yfM, afM)) return espelharPassos(traj1.calcularTeorica(xfM, yfM, afM));
        if (traj2.aplica(xfM, yfM, afM)) return espelharPassos(traj2.calcularTeorica(xfM, yfM, afM));
        if (traj3.aplica(xfM, yfM, afM)) return espelharPassos(traj3.calcularTeorica(xfM, yfM, afM));
        return List.of();
    }

    public Traj1 getTraj1() { return traj1; }
    public Traj2 getTraj2() { return traj2; }
    public Traj3 getTraj3() { return traj3; }
}
