import java.util.ArrayList;
import java.util.List;

/**
 * Trajetória 1: curveLeft(r, 90-α) → straight(2r) → curveLeft(r, φf-(90-α))
 *
 * Condição: xf >= yf e a linha reta que passa em (xf,yf) com ângulo φf
 * intercepta o eixo Xi num ponto positivo menor ou igual a xf (dxif <= xf).
 */
public class Traj1 extends Trajetoria {

    public Traj1(double dbw, double vrobot, double vmin, double vmax) {
        super(dbw, vrobot, vmin, vmax);
    }

    @Override
    public boolean aplica(double xf, double yf, double af) {
        if (yf < 0)   return false;
        if (xf < yf)  return false;
        if (af <= 0)  return false;
        double afRad = Math.toRadians(af);
        double tanAf = Math.tan(afRad);
        if (Math.abs(tanAf) < 1e-9) return false;
        double dxif = xf - yf / tanAf;
        return dxif > 0 && dxif <= xf;
    }

    @Override
    public List<Passo> calcularTeorica(double xf, double yf, double af) {
        double afRad = Math.toRadians(af);
        double a = 2 + 2 * Math.cos(afRad);
        double b = 2 * yf * (1 - Math.cos(afRad)) + 2 * xf * Math.sin(afRad);
        double c = -(xf * xf + yf * yf);
        double r = resolvent(a, b, c);
        double alpha = Math.toDegrees(Math.asin((xf - r * Math.sin(afRad)) / (2 * r)));

        System.out.println("=== Trajetória 1 — Teórica ===");
        System.out.printf("  Ponto destino : (xf=%.2f, yf=%.2f, af=%.2f°)%n", xf, yf, af);
        System.out.printf("  Coeficientes  : a=%.4f  b=%.4f  c=%.4f%n", a, b, c);
        System.out.printf("  Raio teórico  : r=%.2f cm%n", r);
        System.out.printf("  Ângulo α      : %.2f°%n", alpha);
        System.out.printf("  Passos        : CurvaEsq(r=%.2f, %.2f°) → Reta(%.2f) → CurvaEsq(r=%.2f, %.2f°)%n",
                r, 90 - alpha, 2 * r, r, af - (90 - alpha));

        List<Passo> passos = new ArrayList<>();
        passos.add(Passo.curvarEsquerda(90 - alpha, r));
        passos.add(Passo.reta(2 * r));
        passos.add(Passo.curvarEsquerda(af - (90 - alpha), r));
        return passos;
    }

    @Override
    public List<Passo> calcularPratica(double xf, double yf, double af) {
        double afRad = Math.toRadians(af);
        double a = 2 + 2 * Math.cos(afRad);
        double b = 2 * yf * (1 - Math.cos(afRad)) + 2 * xf * Math.sin(afRad);
        double c = -(xf * xf + yf * yf);
        double rt = resolvent(a, b, c);
        double rp = calcularRaioPratico(rt);

        double xc1 = 0,   yc1 = rp;
        double xc2 = xf - rp * Math.sin(afRad);
        double yc2 = yf + rp * Math.cos(afRad);
        double d12 = Math.sqrt((xc2 - xc1) * (xc2 - xc1) + (yc2 - yc1) * (yc2 - yc1));
        double alpha1 = Math.toDegrees(Math.acos(xc2 / d12));
        double alpha2 = af - alpha1;

        System.out.println("=== Trajetória 1 — Prática ===");
        System.out.printf("  Raio teórico  : rt=%.2f cm%n", rt);
        System.out.printf("  Raio prático  : rp=%.2f cm%n", rp);
        System.out.printf("  c1=(%.2f, %.2f)  c2=(%.2f, %.2f)%n", xc1, yc1, xc2, yc2);
        System.out.printf("  d12=%.2f cm%n", d12);
        System.out.printf("  α1=%.2f°  α2=%.2f°%n", alpha1, alpha2);
        System.out.printf("  Passos        : CurvaEsq(rp=%.2f, %.2f°) → Reta(%.2f) → CurvaEsq(rp=%.2f, %.2f°)%n",
                rp, alpha1, d12, rp, alpha2);

        List<Passo> passos = new ArrayList<>();
        passos.add(Passo.curvarEsquerda(alpha1, rp));
        passos.add(Passo.reta(d12));
        passos.add(Passo.curvarEsquerda(alpha2, rp));
        return passos;
    }
}
