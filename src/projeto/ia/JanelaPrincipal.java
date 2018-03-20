package projeto.ia;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JanelaPrincipal {

	private JFrame frmCorretorDoUso;
	private static JFormattedTextField campoEntrada;
	private static JButton btnAnalisar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaPrincipal window = new JanelaPrincipal();
					window.frmCorretorDoUso.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JanelaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCorretorDoUso = new JFrame();
		frmCorretorDoUso.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (!campoEntrada.getText().isEmpty()) btnAnalisar.setEnabled(true);
				else btnAnalisar.setEnabled(false);
			}
		});
		frmCorretorDoUso.setResizable(false);
		frmCorretorDoUso.setTitle("Corretor do uso da crase");
		frmCorretorDoUso.setBounds(100, 100, 476, 337);
		frmCorretorDoUso.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCorretorDoUso.getContentPane().setLayout(null);
		
		campoEntrada = new JFormattedTextField();
		campoEntrada.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (!campoEntrada.getText().isEmpty()) btnAnalisar.setEnabled(true);
				else btnAnalisar.setEnabled(false);
			}
		});
		campoEntrada.setBounds(10, 79, 444, 30);
		frmCorretorDoUso.getContentPane().add(campoEntrada);
		
		JLabel lblTitle = new JLabel("Corretor ortografico de crase");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(41, 11, 374, 27);
		frmCorretorDoUso.getContentPane().add(lblTitle);
		
		JLabel lblCaixaDeTexto = new JLabel("Escreva no campo abaixo a frase com crase que deve ser analisada:");
		lblCaixaDeTexto.setBounds(10, 63, 444, 14);
		frmCorretorDoUso.getContentPane().add(lblCaixaDeTexto);
		
		JLabel lblCrase = new JLabel("\u00E0s");
		lblCrase.setForeground(Color.LIGHT_GRAY);
		lblCrase.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrase.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCrase.setBounds(219, 154, 29, 31);
		frmCorretorDoUso.getContentPane().add(lblCrase);
		
		JLabel lblTextoEsquerda = new JLabel("...");
		lblTextoEsquerda.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTextoEsquerda.setBounds(10, 164, 192, 21);
		frmCorretorDoUso.getContentPane().add(lblTextoEsquerda);
		
		JLabel lblTextoDireita = new JLabel("...");
		lblTextoDireita.setHorizontalAlignment(SwingConstants.LEFT);
		lblTextoDireita.setBounds(263, 164, 191, 21);
		frmCorretorDoUso.getContentPane().add(lblTextoDireita);
		
		btnAnalisar = new JButton("Analisar");
		btnAnalisar.setEnabled(false);
		btnAnalisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					Analise an = new Analise(campoEntrada.getText());
					lblTextoEsquerda.setText(an.getTextoAnterior());
					lblTextoDireita.setText(an.getTextoPosterior());
				} catch (Exception e) {
					
				}
			}
		});
		btnAnalisar.setBounds(173, 120, 125, 23);
		frmCorretorDoUso.getContentPane().add(btnAnalisar);
		
		
	}
}
