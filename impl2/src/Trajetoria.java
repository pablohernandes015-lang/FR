import java.util.List;

public abstract class Trajetoria {

    protected double dbw;    // distância entre rodas (cm)
    protected double vrobot; // velocidade base do robô
    protected double vmin;
    protected double vmax;

    public Trajetoria(double dbw, double vrobot, double vmin, double vmax) {
        this.dbw    = dbw;
        this.vrobot = vrobot;
        this.vmin   = vmin;
        this.vmax   = vmax;
    }

    // Retorna se esta trajetória é aplicável ao ponto destino (xf,yf,af)
    public abstract boolean aplica(double xf, double yf, double af);

    // Trajetória teórica (raio exato)
    public abstract List<Passo> calcularTeorica(double xf, double yf, double af);

    // Trajetória prática (raio ajustado às velocidades inteiras do robô)
    public abstract List<Passo> calcularPratica(double xf, double yf, double af);

    // Fórmula resolvente — devolve a raiz positiva de a*r² + b*r + c = 0
    protected double resolvent(double a, double b, double c) {
        double disc = b * b - 4 * a * c;
        if (disc < 0) throw new ArithmeticException("Discriminante negativo: sem solução real");
        return (-b + Math.sqrt(disc)) / (2 * a);
    }

    // Calcula o raio prático a partir do raio teórico,
    // garantindo vmin <= v2 e v1 <= vmax.
    protected double calcularRaioPratico(double raioTeorico) {
        double halfDbw = dbw / 2.0;
        double fTeo = (raioTeorico + halfDbw) / (raioTeorico - halfDbw);
        double v2Teo = 2.0 / (fTeo + 1.0) * vrobot;

        int v2p = (int) v2Teo;                      // truncar (roda interior)

        // v2 não pode ficar abaixo de vmin
        if (v2p < (int) vmin) v2p = (int) vmin;

        int v1p = (int) (2.0 * vrobot) - v2p;       // roda exterior

        // v1 não pode ultrapassar vmax
        if (v1p > (int) vmax) {
            v1p = (int) vmax;
            v2p = (int) (2.0 * vrobot) - v1p;
        }

        double fp = (double) v1p / v2p;
        return halfDbw * ((fp + 1.0) / (fp - 1.0));
    }
}
