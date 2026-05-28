import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class GUI_TP1 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel     contentPane;
    private JTextField robotTextField, raioTextField, anguloTextField;
    private JTextField distanciaTextfield, XftextField, YftextField, OftextField;
    JLabel    robotlabel, raiolabel, angulolabel, distancialabel, consolalabel;
    JRadioButton onOffButton;
    JButton   butaofrente, butaoparar, butaoretaguarda, butaodireita, butaoesquerda;
    JCheckBox debugCheckBox;
    JScrollPane consolascrollPane;
    JTextArea consolatextArea;

    DadosGUI_TP1 dadosGUI;

    public GUI_TP1(App app) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { dadosGUI.getApp().mandarTerminar(); }
        });

        dadosGUI = new DadosGUI_TP1();
        dadosGUI.setApp(app);

        setForeground(new Color(0, 0, 0));
        setFont(new Font("Dialog", Font.PLAIN, 14));
        setTitle("Trabalho 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 452, 421);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        robotlabel = new JLabel("Robot");
        robotlabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        robotlabel.setBounds(120, 10, 44, 16);
        contentPane.add(robotlabel);

        robotTextField = new JTextField(dadosGUI.getNomeRobot());
        robotTextField.addActionListener(e -> {
            dadosGUI.setNomeRobot(robotTextField.getText());
            myPrint("Nome do Robot : " + dadosGUI.getNomeRobot());
        });
        robotTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        robotTextField.setBounds(161, 9, 96, 18);
        contentPane.add(robotTextField);
        robotTextField.setColumns(10);

        onOffButton = new JRadioButton("On/Off");
        onOffButton.addActionListener(e -> {
            if (!dadosGUI.isOnOff()) {
                boolean ok = app.connectRobot(dadosGUI.getNomeRobot());
                onOffButton.setSelected(ok);
                dadosGUI.setOnOff(ok);
            } else {
                app.disconnectRobot();
                onOffButton.setSelected(false);
                dadosGUI.setOnOff(false);
            }
        });
        onOffButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        onOffButton.setBounds(278, 6, 102, 20);
        onOffButton.setSelected(dadosGUI.isOnOff());
        contentPane.add(onOffButton);

        raiolabel = new JLabel("Raio");
        raiolabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        raiolabel.setBounds(29, 37, 27, 16);
        contentPane.add(raiolabel);

        raioTextField = new JTextField(String.valueOf(dadosGUI.getRaio()));
        raioTextField.addActionListener(e -> {
            try   { dadosGUI.setRaio(Integer.parseInt(raioTextField.getText())); }
            catch (NumberFormatException ex) { raioTextField.setText(String.valueOf(dadosGUI.getRaio())); }
            myPrint("Raio : " + dadosGUI.getRaio());
        });
        raioTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        raioTextField.setColumns(10);
        raioTextField.setBounds(66, 37, 37, 18);
        contentPane.add(raioTextField);

        angulolabel = new JLabel("Ângulo");
        angulolabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        angulolabel.setBounds(139, 37, 44, 16);
        contentPane.add(angulolabel);

        anguloTextField = new JTextField(String.valueOf(dadosGUI.getAngulo()));
        anguloTextField.addActionListener(e -> {
            try   { dadosGUI.setAngulo(Integer.parseInt(anguloTextField.getText())); }
            catch (NumberFormatException ex) { anguloTextField.setText(String.valueOf(dadosGUI.getAngulo())); }
            myPrint("Ângulo : " + dadosGUI.getAngulo());
        });
        anguloTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        anguloTextField.setColumns(10);
        anguloTextField.setBounds(189, 37, 44, 18);
        contentPane.add(anguloTextField);

        distancialabel = new JLabel("Distância");
        distancialabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        distancialabel.setBounds(283, 37, 58, 16);
        contentPane.add(distancialabel);

        distanciaTextfield = new JTextField(String.valueOf(dadosGUI.getDistancia()));
        distanciaTextfield.addActionListener(e -> {
            try   { dadosGUI.setDistancia(Integer.parseInt(distanciaTextfield.getText())); }
            catch (NumberFormatException ex) { distanciaTextfield.setText(String.valueOf(dadosGUI.getDistancia())); }
            myPrint("Distância : " + dadosGUI.getDistancia());
        });
        distanciaTextfield.setFont(new Font("Tahoma", Font.PLAIN, 14));
        distanciaTextfield.setColumns(10);
        distanciaTextfield.setBounds(351, 37, 49, 18);
        contentPane.add(distanciaTextfield);

        butaofrente = new JButton("Frente");
        butaofrente.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butaofrente.addActionListener(e -> { myPrint("Frente : " + dadosGUI.getDistancia()); app.reta(dadosGUI.getDistancia()); });
        butaofrente.setBackground(new Color(0, 255, 0));
        butaofrente.setBounds(153, 101, 125, 50);
        contentPane.add(butaofrente);

        butaoparar = new JButton("Parar");
        butaoparar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butaoparar.setBackground(new Color(255, 0, 0));
        butaoparar.addActionListener(e -> { myPrint("Parar"); app.parar(); });
        butaoparar.setBounds(153, 150, 125, 50);
        contentPane.add(butaoparar);

        butaoretaguarda = new JButton("Retaguarda");
        butaoretaguarda.addActionListener(e -> { myPrint("Retaguarda : " + dadosGUI.getDistancia()); app.retaguarda(dadosGUI.getDistancia()); });
        butaoretaguarda.setBackground(new Color(255, 0, 255));
        butaoretaguarda.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butaoretaguarda.setBounds(153, 200, 125, 50);
        contentPane.add(butaoretaguarda);

        butaodireita = new JButton("Direita");
        butaodireita.addActionListener(e -> { myPrint("Direita"); app.direita(dadosGUI.getRaio(), dadosGUI.getAngulo()); });
        butaodireita.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butaodireita.setBackground(new Color(255, 255, 0));
        butaodireita.setBounds(276, 150, 125, 50);
        contentPane.add(butaodireita);

        butaoesquerda = new JButton("Esquerda");
        butaoesquerda.addActionListener(e -> { myPrint("Esquerda"); app.esquerda(dadosGUI.getRaio(), dadosGUI.getAngulo()); });
        butaoesquerda.setBackground(new Color(0, 0, 255));
        butaoesquerda.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butaoesquerda.setBounds(29, 150, 125, 50);
        contentPane.add(butaoesquerda);

        debugCheckBox = new JCheckBox("Debug");
        debugCheckBox.addActionListener(e -> {
            dadosGUI.setDebug(!dadosGUI.isDebug());
            debugCheckBox.setSelected(dadosGUI.isDebug());
            myPrint("Debug : " + dadosGUI.isDebug());
        });
        debugCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        debugCheckBox.setBounds(26, 229, 92, 20);
        debugCheckBox.setSelected(dadosGUI.isDebug());
        contentPane.add(debugCheckBox);

        consolalabel = new JLabel("Consola");
        consolalabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        consolalabel.setBounds(189, 260, 68, 12);
        contentPane.add(consolalabel);

        consolascrollPane = new JScrollPane();
        consolascrollPane.setBounds(21, 284, 407, 72);
        contentPane.add(consolascrollPane);

        consolatextArea = new JTextArea();
        consolascrollPane.setViewportView(consolatextArea);

        JLabel xfLabel = new JLabel("Xf");
        xfLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        xfLabel.setBounds(56, 65, 27, 16);
        contentPane.add(xfLabel);

        JLabel yfLabel = new JLabel("Yf");
        yfLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        yfLabel.setBounds(183, 65, 27, 16);
        contentPane.add(yfLabel);

        JLabel ofLabel = new JLabel("Of");
        ofLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        ofLabel.setBounds(330, 65, 27, 16);
        contentPane.add(ofLabel);

        XftextField = new JTextField(String.valueOf(dadosGUI.getXf()));
        XftextField.addActionListener(e -> {
            try   { dadosGUI.setXf(Integer.parseInt(XftextField.getText())); }
            catch (NumberFormatException ex) { XftextField.setText(String.valueOf(dadosGUI.getXf())); }
            myPrint("Xf : " + dadosGUI.getXf());
        });
        XftextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        XftextField.setColumns(10);
        XftextField.setBounds(76, 65, 37, 18);
        contentPane.add(XftextField);

        YftextField = new JTextField(String.valueOf(dadosGUI.getYf()));
        YftextField.addActionListener(e -> {
            try   { dadosGUI.setYf(Integer.parseInt(YftextField.getText())); }
            catch (NumberFormatException ex) { YftextField.setText(String.valueOf(dadosGUI.getYf())); }
            myPrint("Yf : " + dadosGUI.getYf());
        });
        YftextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        YftextField.setColumns(10);
        YftextField.setBounds(199, 65, 37, 18);
        contentPane.add(YftextField);

        OftextField = new JTextField(String.valueOf(dadosGUI.getOf()));
        OftextField.addActionListener(e -> {
            try   { dadosGUI.setOf(Integer.parseInt(OftextField.getText())); }
            catch (NumberFormatException ex) { OftextField.setText(String.valueOf(dadosGUI.getOf())); }
            myPrint("Of : " + dadosGUI.getOf());
        });
        OftextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        OftextField.setColumns(10);
        OftextField.setBounds(351, 66, 37, 18);
        contentPane.add(OftextField);

        JButton btnGo = new JButton("GO");
        btnGo.setBackground(new Color(0, 255, 0));
        btnGo.addActionListener(e -> {
            int xf = dadosGUI.getXf(), yf = dadosGUI.getYf(), of = dadosGUI.getOf();
            myPrint(app.toPoint(xf, yf, of).toString());
        });
        btnGo.setBounds(330, 101, 84, 20);
        contentPane.add(btnGo);

        // ── Teclado ────────────────────────────────────────────────────────────
        InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = contentPane.getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"),    "frente");
        im.put(KeyStroke.getKeyStroke("DOWN"),  "tras");
        im.put(KeyStroke.getKeyStroke("LEFT"),  "esquerda");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "direita");

        am.put("frente",   new AbstractAction() { public void actionPerformed(ActionEvent e) { myPrint("Frente");    dadosGUI.getApp().reta(dadosGUI.getDistancia()); } });
        am.put("tras",     new AbstractAction() { public void actionPerformed(ActionEvent e) { myPrint("Retaguarda"); dadosGUI.getApp().retaguarda(dadosGUI.getDistancia()); } });
        am.put("esquerda", new AbstractAction() { public void actionPerformed(ActionEvent e) { myPrint("Esquerda");  dadosGUI.getApp().esquerda(dadosGUI.getRaio(), dadosGUI.getAngulo()); } });
        am.put("direita",  new AbstractAction() { public void actionPerformed(ActionEvent e) { myPrint("Direita");   dadosGUI.getApp().direita(dadosGUI.getRaio(), dadosGUI.getAngulo()); } });

        setVisible(true);
    }

    private void myPrint(String s) {
        dadosGUI.addRelatorio(s);
        if (dadosGUI.isDebug()) consolatextArea.append(s + "\n");
    }
}
