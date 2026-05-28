import java.util.ArrayList;
import java.util.List;

/**
 * Trajetória 3: curveLeft(r, a) → curveRight(r, a-af)   [teórica]
 *               curveLeft(rp, a) → straight(d) → curveRight(rp, a-af)  [prática]
 *
 * Condição: xf >= yf  e  45 >= af > -90  e  yc2 > yc1
 */
public class Traj3 extends Trajetoria {

    public Traj3(double dbw, double vrobot, double vmin, double vmax) {
        super(dbw, vrobot, vmin, vmax);
    }

    @Override
    public boolean aplica(double xf, double yf, double af) {
        if (yf < 0)               return false;
        if (xf < yf)              return false;
        if (af > 45 || af <= -90) return false;
        double absAf    = Math.abs(af);
        double afAbsRad = Math.toRadians(absAf);
        double a = 2 - 2 * Math.cos(afAbsRad);
        if (Math.abs(a) < 1e-9)   return false;
        double b = 2 * yf * (1 + Math.cos(afAbsRad)) + 2 * xf * Math.sin(afAbsRad);
        double c = -(xf * xf + yf * yf);
        try {
            double r = resolvent(a, b, c);
            return (yf - r * Math.cos(afAbsRad)) > r;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    public List<Passo> calcularTeorica(double xf, double yf, double af) {
        double absAf    = Math.abs(af);
        double afAbsRad = Math.toRadians(absAf);
        double a = 2 - 2 * Math.cos(afAbsRad);
        double b = 2 * yf * (1 + Math.cos(afAbsRad)) + 2 * xf * Math.sin(afAbsRad);
        double c = -(xf * xf + yf * yf);
        double r = resolvent(a, b, c);
        double d      = Math.toDegrees(Math.asin((xf - r * Math.sin(afAbsRad)) / (2 * r)));
        double aAngle = 180.0 - d;

        System.out.println("=== Trajetória 3 — Teórica ===");
        System.out.printf("  Ponto destino : (xf=%.2f, yf=%.2f, af=%.2f°)%n", xf, yf, af);
        System.out.printf("  Coeficientes  : a=%.4f  b=%.4f  c=%.4f%n", a, b, c);
        System.out.printf("  Raio teórico  : r=%.2f cm%n", r);
        System.out.printf("  Ângulo d=%.2f°  →  ângulo a=%.2f°%n", d, aAngle);
        System.out.printf("  Passos        : CurvaEsq(r=%.2f, %.2f°) → CurvaDir(r=%.2f, %.2f°)%n",
                r, aAngle, r, aAngle - af);

        List<Passo> passos = new ArrayList<>();
        passos.add(Passo.curvarEsquerda(aAngle, r));
        passos.add(Passo.curvarDireita(aAngle - af, r));
        return passos;
    }

    @Override
    public List<Passo> calcularPratica(double xf, double yf, double af) {
        double absAf    = Math.abs(af);
        double afAbsRad = Math.toRadians(absAf);
        double a = 2 - 2 * Math.cos(afAbsRad);
        double b = 2 * yf * (1 + Math.cos(afAbsRad)) + 2 * xf * Math.sin(afAbsRad);
        double c = -(xf * xf + yf * yf);
        double rt = resolvent(a, b, c);
        double rp = calcularRaioPratico(rt);

        double xc1 = 0,   yc1 = rp;
        double xc2 = xf - rp * Math.sin(afAbsRad);
        double yc2 = yf - rp * Math.cos(afAbsRad);
        double d12       = Math.sqrt((xc2 - xc1) * (xc2 - xc1) + (yc2 - yc1) * (yc2 - yc1));
        double dAngle    = Math.toDegrees(Math.acos(rp / (d12 / 2.0)));
        double bAngle    = Math.toDegrees(Math.asin(xc2 / d12));
        double dstraight = d12 * Math.sin(Math.toRadians(dAngle));
        double aAngle    = 180.0 - (bAngle + dAngle);

        System.out.println("=== Trajetória 3 — Prática ===");
        System.out.printf("  Raio teórico  : rt=%.2f cm%n", rt);
        System.out.printf("  Raio prático  : rp=%.2f cm%n", rp);
        System.out.printf("  c1=(%.2f, %.2f)  c2=(%.2f, %.2f)%n", xc1, yc1, xc2, yc2);
        System.out.printf("  d12=%.2f cm%n", d12);
        System.out.printf("  ângulo d=%.2f°  b=%.2f°  a=%.2f°  dstraight=%.2f cm%n",
                dAngle, bAngle, aAngle, dstraight);
        System.out.printf("  Passos        : CurvaEsq(rp=%.2f, %.2f°) → Reta(%.2f) → CurvaDir(rp=%.2f, %.2f°)%n",
                rp, aAngle, dstraight, rp, aAngle - af);

        List<Passo> passos = new ArrayList<>();
        passos.add(Passo.curvarEsquerda(aAngle, rp));
        passos.add(Passo.reta(dstraight));
        passos.add(Passo.curvarDireita(aAngle - af, rp));
        return passos;
    }
}
