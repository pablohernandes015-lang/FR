import java.util.List;
import java.util.concurrent.Semaphore;

public class App extends Thread {

    GUI_TP1 gui;

    GestorTrajetoria gestor;

    private final int TERMINA   = 0;
    private final int ESPERA    = 1;
    private final int FUNCIONAR = 2;

    private int estado = FUNCIONAR;

    Semaphore haTrabalho;

    private MyRobotLegoEV3 robot;

    public boolean connectRobot(String robotName) { return robot.OpenEV3(robotName); }
    public void disconnectRobot()                 { robot.CloseEV3(); }

    public App() {
        robot  = new MyRobotLegoEV3();
        gestor = new GestorTrajetoria(robot);
        gui    = new GUI_TP1(this);

        haTrabalho = new Semaphore(0);
        estado     = FUNCIONAR;
    }

    public void reta(int dist) {
        robot.add_queue(Passo.reta(dist));
    }

    public void retaguarda(int dist) {
        robot.add_queue(Passo.reta(-dist));
    }

    public void direita(double raio, int angulo) {
        robot.add_queue(Passo.curvarDireita(angulo, raio));
    }

    public void esquerda(double raio, int angulo) {
        robot.add_queue(Passo.curvarEsquerda(angulo, raio));
    }

    public List<Passo> toPoint(int xf, int yf, int of) {
        List<Passo> passos = gestor.calcular(xf, yf, of);
        gestor.executar(passos);
        return passos;
    }

    public void parar() {
        robot.Parar(true);
    }

    public void mandarTerminar() {
        estado = TERMINA;
        haTrabalho.release();
    }

    @Override
    public void run() {
        while (estado != TERMINA) {
            switch (estado) {
            case ESPERA:
                try { haTrabalho.acquire(); } catch (InterruptedException e) { e.printStackTrace(); }
                break;
            case FUNCIONAR:
                robot.runStates();
                break;
            default:
                break;
            }
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
        System.out.println("Thread começada");
    }
}
