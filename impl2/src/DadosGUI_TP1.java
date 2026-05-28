import java.util.ArrayList;

public class DadosGUI_TP1 {
	private String nomeRobot;
	private boolean OnOff,debug;
	private int raio,angulo,distancia,Xf,Yf,Of;
	private ArrayList<String> relatorio;
	App app;
	
	public DadosGUI_TP1() {
		nomeRobot = "EV5";
		OnOff = false;
		raio = 10;
		angulo = 90;
		distancia = 10;
		Xf=1;
		Yf=2;
		Of=3;
		debug = false;
		relatorio = new ArrayList<String>();
	}
	
	public App getApp() {
		return app;
	}
	
	public void setApp(App app) {
		this.app = app;
	}
	
	public String getNomeRobot() {
		return nomeRobot;
	}
	
	public void setNomeRobot(String nomeRobot) {
		this.nomeRobot = nomeRobot;
	}
	
	public boolean isOnOff() {
		return OnOff;
	}
	
	public void setOnOff(boolean onOff) {
		OnOff = onOff;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public int getRaio() {
		return raio;
	}
	
	public void setRaio(int raio) {
		this.raio = raio;
	}
	
	public int getAngulo() {
		return angulo;
	}
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}
	public int getDistancia() {
		return distancia;
	}
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	
	public int getXf() {
		return Xf;
	}
	public void setXf(int Xf) {
		this.Xf = Xf;
	}
	
	public int getYf() {
		return Yf;
	}
	public void setYf(int Yf) {
		this.Yf = Yf;
	}
	
	public int getOf() {
		return Of;
	}
	public void setOf(int Of) {
		this.Of = Of;
	}
	
	
	
	
	
	
	
	public ArrayList<String> getRelatorio() {
		return relatorio;
	}
	public void addRelatorio(String s) {
		this.relatorio.add(s);
	}
	
	

}
