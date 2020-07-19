package �ͻ���;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;




public class Client extends JFrame implements Runnable,ActionListener{
	JButton startReceive = new JButton("��ʼ����");	//���忪ʼ���հ�ť
	JButton stopReceive = new JButton("ֹͣ����");	//����ֹͣ���հ�ť
	JTextArea startTextArea = new JTextArea(10,10);		//���忪ʼ���պ���ʾ���ı���
	JTextArea stopTestArea = new JTextArea(10,10);		//������ʾ���յ�����Ϣ
	Font font = new Font("����", 20, 20);	//������ʾ������
	Thread thread;		//�����̶߳���
	boolean getMessage = true;	//�Ƿ���չ㲥
	
	
	int port = 9898;	//�����˿�
	InetAddress group;		//�����㲥���ַ
	MulticastSocket socket;		//�����ಥ���ݰ��׽���
	
	//���췽��
	public Client() {
		super("���ݱ�����");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//���ô���رշ�ʽ
		
		//����������ť��������
		startReceive.setFont(font);		
		stopReceive.setFont(font);
		JPanel upJPanel = new JPanel();		//�½�һ�������ð�ť
		upJPanel.add(startReceive);
		upJPanel.add(stopReceive);
		add(upJPanel, BorderLayout.NORTH);	//�����ð�ť�������ӵ�������,���ҷ����ڴ�����ϲ�
		
		thread = new Thread(this);	//���캯���н����߳�
		startReceive.addActionListener(this);	//Ϊ��ʼ���հ�ť��Ӽ���
		stopReceive.addActionListener(this);	//Ϊֹͣ���հ�ť��Ӽ���
		
		
		JPanel textJPanel = new JPanel();	//�½�һ����������ʾ������Ϣ
		textJPanel.setLayout(new GridLayout(1,2));	//������岼��Ϊһ������
		startTextArea.setForeground(Color.red);		//������ʾ��������ɫ
		stopTestArea.setForeground(Color.blue);	
		textJPanel.add(startTextArea);		//����ʾ�ı�����������
		textJPanel.add(stopTestArea);	//��������Ϣ���ı�����ӵ����
		
		final JScrollPane scrollPane = new JScrollPane();		//���ù�����	final��ʾ����Ϊ���ɱ�ģ��ڲ�����
		textJPanel.add(scrollPane);
		
		
		
		scrollPane.setViewportView(stopTestArea);	//Ϊ�ı�����ӹ�����
		add(textJPanel, BorderLayout.CENTER);	//�������ı���������ӵ�����	�������м䲿��
				
		setBounds(100, 100, 500, 450);	//���ô��ڲ���
		setVisible(true);	//���ô��ڿɼ�
		
		
		try {
			group = Inet4Address.getByName("224.255.10.0");		//ָ���㲥���ַ
			socket = new MulticastSocket(port);		//ʵ�����ಥ���ݰ��׽���
			socket.joinGroup(group);	//����ַ����㲥��
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//�����ǰ����İ�ť�ǿ�ʼ���հ�ť
		if (e.getSource() == startReceive) {
			startReceive.setBackground(Color.yellow);	//���ÿ�ʼ���հ�ť����ɫΪ��ɫ
			stopReceive.setBackground(Color.red);	//����ֹͣ���ܰ�ť����ɫΪ��ɫ
			//�����ǰ�̲߳���һ������״̬
			if (!thread.isAlive()) {
				thread = new Thread(this);	//�½�һ���̶߳���
				getMessage = true;
			}
			thread.start();
		}
		
		//����������ֹͣ���ܵİ�ť
		if (e.getSource() == stopReceive) {
			startReceive.setBackground(Color.red);	//���ÿ�ʼ���հ�ť����ɫΪ��ɫ
			stopReceive.setBackground(Color.yellow);	//����ֹͣ���ܰ�ť����ɫΪ��ɫ
			getMessage = false;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (getMessage) {
			DatagramPacket packet;		//�����������ݰ�
			byte data[] = new byte[1024];
			packet = new DatagramPacket(data, data.length, group, port);	//��ȡ���յ�����Ϣ
			
			try {
				socket.receive(packet);		//��ȡ���ݰ�
				String message = new String(packet.getData(),0,packet.getLength()); 	//�����ݰ��е�����ת��Ϊ�ַ���
				
				startTextArea.setText("���ڽ������ݣ�" + message);
				stopTestArea.append(message + "\n");	//�����յ�����Ϣ��ӵ����տ�
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		Client client = new Client();
	}
}
