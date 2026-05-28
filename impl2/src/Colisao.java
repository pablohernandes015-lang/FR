public class Colisao {

    public static double anguloParede(double dist0, double dist1, double drobot1) {
        return Math.atan(Math.abs(dist0 - dist1)/drobot1);
    }

    public static double distanciaPerpendicularParede(double dist0, double dist1, double drobot1) {
        double a = anguloParede(dist0, dist1, drobot1);
        return dist1 * Math.cos(a);
    }
}