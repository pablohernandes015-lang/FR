public class Passo {

    public enum Tipo { ARCO, RETA }

    // ARCO: graus positivos = curva esquerda, negativos = curva direita; raio em cm
    // RETA: distância em cm
    public final Tipo tipo;
    public final double valor;
    public final double raio;

    private Passo(Tipo tipo, double valor, double raio) {
        this.tipo  = tipo;
        this.valor = valor;
        this.raio  = raio;
    }

    public static Passo curvarEsquerda(double anguloGraus, double raioCm) {
        return new Passo(Tipo.ARCO, anguloGraus, raioCm);
    }

    public static Passo curvarDireita(double anguloGraus, double raioCm) {
        return new Passo(Tipo.ARCO, -anguloGraus, raioCm);
    }

    public static Passo reta(double distanciaCm) {
        return new Passo(Tipo.RETA, distanciaCm, 0);
    }

    /** Devolve o passo espelhado (esquerda↔direita). Reta fica igual. */
    public Passo espelhar() {
        if (tipo == Tipo.RETA) return this;
        return valor > 0
            ? curvarDireita( valor, raio)   // era esquerda → fica direita
            : curvarEsquerda(-valor, raio); // era direita  → fica esquerda
    }

    @Override
    public String toString() {
        if (tipo == Tipo.ARCO) {
            String dir = valor >= 0 ? "Esquerda" : "Direita";
            return String.format("Curvar%s(raio=%.2fcm, angulo=%.2f°)", dir, raio, Math.abs(valor));
        }
        return String.format("Reta(%.2fcm)", valor);
    }
}
