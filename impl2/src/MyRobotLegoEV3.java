import java.util.ArrayDeque;
import java.util.Queue;

import interpretador.InterpretadorEV3;

public class MyRobotLegoEV3 {

    InterpretadorEV3 robot;

    public boolean connected = false;

    final long tempoDeComunicacao = 20;
    final int  powerPercentage    = 40;
    final double det              = 0.09;
    final double raioDaRoda       = 0.0273;
    final boolean preditor        = true;

    public Queue<Passo> movement_queue;
    public boolean is_stopping = false;

    // ── Estado da reta ──────────────────────────────────────────────────────────

    public enum EstadoReta { IDLE, CALCULAR, VALIDAR, ESPERAR, PARAR }

    public EstadoReta estadoReta = EstadoReta.IDLE;
    public long   waitStartTime;
    public double wheelAngleRequired;
    public double wheelAngleStart;
    public double deslocamentoAnterior;
    public double distanceReta;

    // ── Estado da curva ─────────────────────────────────────────────────────────

    public enum EstadoCurva { IDLE, CALCULAR, VALIDAR, ESPERAR, PARAR }

    public EstadoCurva estadoCurva = EstadoCurva.IDLE;
    public long   waitStartTimeCurva;
    public double curveWheelRAngleRequired;
    public double curveWheelLAngleRequired;
    public double curveDeslocamentoAnterior;
    public double curveWheelAngleStart;
    public double StartDireita;
    public double StartEsquerda;
    public double curveRaio;
    public double curveAngulo;

    // ── Construtor ──────────────────────────────────────────────────────────────

    public MyRobotLegoEV3() {
        robot          = new InterpretadorEV3();
        movement_queue = new ArrayDeque<>();
    }

    // ── Ligação ─────────────────────────────────────────────────────────────────

    public boolean OpenEV3(String robotName) {
        if (!connected) connected = robot.OpenEV3(robotName);
        return connected;
    }

    public void CloseEV3() {
        if (connected) {
            robot.CloseEV3();
            connected = false;
        }
    }

    // ── Comandos de movimento (não bloqueantes) ─────────────────────────────────

    void Reta(int distancia) {
        distanceReta = distancia / 100.0;
        estadoReta   = EstadoReta.CALCULAR;
        System.out.println("Calcular Reta");
    }

    void CurvarEsquerda(double raio, int angulo) {
        curveAngulo = Math.toRadians(angulo);
        curveRaio   = raio / 100.0;
        estadoCurva = EstadoCurva.CALCULAR;
        System.out.println("Calcular Curva Esquerda");
    }

    void CurvarDireita(double raio, int angulo) {
        curveAngulo = Math.toRadians(angulo);
        curveRaio   = -(raio / 100.0);
        estadoCurva = EstadoCurva.CALCULAR;
        System.out.println("Calcular Curva Direita");
    }

    void Parar(boolean assincrono) {
        if (assincrono) {
            estadoReta  = EstadoReta.PARAR;
            estadoCurva = EstadoCurva.PARAR;
            is_stopping = true;
        } else {
            PararSincrono();
        }
    }

    void PararSincrono()   { is_stopping = true; }

    void PararAssincrono() {
        estadoReta  = EstadoReta.IDLE;
        estadoCurva = EstadoCurva.IDLE;
        robot.Off(robot.OUT_BC);
        is_stopping = false;
    }

    // ── Máquinas de estado ───────────────────────────────────────────────────────

    void RetaState() {
        switch (estadoReta) {
        case CALCULAR:
            wheelAngleRequired = distanceReta / raioDaRoda;
            wheelAngleStart    = Math.toRadians(robot.RotationCount(robot.OUT_B));
            deslocamentoAnterior = 0;
            if (wheelAngleRequired > 0) MotorRun(powerPercentage, powerPercentage);
            else                        MotorRun(-powerPercentage, -powerPercentage);
            estadoReta = EstadoReta.VALIDAR;
            break;

        case VALIDAR:
            double rotation = Math.toRadians(robot.RotationCount(robot.OUT_B)) - wheelAngleStart;
            double delta    = rotation - deslocamentoAnterior;
            deslocamentoAnterior = rotation;
            if (preditor) rotation += delta;
            if (wheelAngleRequired > 0) {
                if (rotation > wheelAngleRequired) estadoReta = EstadoReta.PARAR;
                else { estadoReta = EstadoReta.ESPERAR; waitStartTime = System.currentTimeMillis(); }
            } else {
                if (rotation < wheelAngleRequired) estadoReta = EstadoReta.PARAR;
                else { estadoReta = EstadoReta.ESPERAR; waitStartTime = System.currentTimeMillis(); }
            }
            break;

        case ESPERAR:
            if (System.currentTimeMillis() - waitStartTime >= tempoDeComunicacao)
                estadoReta = EstadoReta.VALIDAR;
            break;

        case PARAR:
            if (is_stopping) PararAssincrono();
            estadoReta = EstadoReta.IDLE;
            break;

        default: break;
        }
    }

    void CurvaState() {
        switch (estadoCurva) {
        case CALCULAR:
            wheelAngleRequired   = (Math.abs(curveRaio) * curveAngulo) / raioDaRoda;
            StartDireita         = Math.toRadians(robot.RotationCount(robot.OUT_C));
            StartEsquerda        = Math.toRadians(robot.RotationCount(robot.OUT_B));
            curveDeslocamentoAnterior = 0;

            if (curveRaio > 0) {
                double relacao = (curveRaio + det / 2) / (curveRaio - det / 2);
                MotorRun((int)(powerPercentage / relacao), powerPercentage);
            } else {
                double r = -curveRaio;
                double relacao = (r + det / 2) / (r - det / 2);
                MotorRun(powerPercentage, (int)(powerPercentage / relacao));
            }
            estadoCurva = EstadoCurva.VALIDAR;
            break;

        case VALIDAR:
            double rotB   = Math.toRadians(robot.RotationCount(robot.OUT_C)) - StartDireita;
            double rotC   = Math.toRadians(robot.RotationCount(robot.OUT_B)) - StartEsquerda;
            double rot    = (rotB + rotC) / 2.0;
            double dlt    = rot - curveDeslocamentoAnterior;
            curveDeslocamentoAnterior = rot;
            if (preditor) rot += dlt;
            if (rot > wheelAngleRequired) estadoCurva = EstadoCurva.PARAR;
            else { estadoCurva = EstadoCurva.ESPERAR; waitStartTimeCurva = System.currentTimeMillis(); }
            break;

        case ESPERAR:
            if (System.currentTimeMillis() - waitStartTimeCurva >= tempoDeComunicacao)
                estadoCurva = EstadoCurva.VALIDAR;
            break;

        case PARAR:
            if (is_stopping) PararAssincrono();
            estadoCurva = EstadoCurva.IDLE;
            break;

        default: break;
        }
    }

    void MotorRun(int esq, int dir) {
        robot.OnFwd(robot.OUT_B, esq, robot.OUT_C, dir);
    }

    // ── Fila de passos ──────────────────────────────────────────────────────────

    public void executeStep(Passo step) {
        if (step.tipo == Passo.Tipo.ARCO && step.valor < 0)
            CurvarDireita(step.raio, (int) -step.valor);
        if (step.tipo == Passo.Tipo.ARCO && step.valor > 0)
            CurvarEsquerda(step.raio, (int) step.valor);
        if (step.tipo == Passo.Tipo.RETA)
            Reta((int) step.valor);
    }

    public void add_queue(Passo step) {
        movement_queue.add(step);
        is_stopping = false;
    }

    public Boolean runStatesBusy() {
        return !(estadoCurva == EstadoCurva.IDLE && estadoReta == EstadoReta.IDLE);
    }

    public void runStates() {
        if (!movement_queue.isEmpty() && estadoCurva == EstadoCurva.IDLE && estadoReta == EstadoReta.IDLE) {
            executeStep(movement_queue.poll());
            if (movement_queue.isEmpty()) PararSincrono();
        }
        RetaState();
        CurvaState();
        try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
