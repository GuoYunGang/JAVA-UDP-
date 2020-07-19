package 客户端;

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
	JButton startReceive = new JButton("开始接收");	//定义开始接收按钮
	JButton stopReceive = new JButton("停止接收");	//定义停止接收按钮
	JTextArea startTextArea = new JTextArea(10,10);		//定义开始接收后显示的文本框
	JTextArea stopTestArea = new JTextArea(10,10);		//定义显示接收到的信息
	Font font = new Font("楷体", 20, 20);	//定义显示字体风格
	Thread thread;		//创建线程对象
	boolean getMessage = true;	//是否接收广播
	
	
	int port = 9898;	//创建端口
	InetAddress group;		//创建广播组地址
	MulticastSocket socket;		//创建多播数据包套接字
	
	//构造方法
	public Client() {
		super("数据报接收");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//设置窗体关闭方式
		
		//设置两个按钮的字体风格
		startReceive.setFont(font);		
		stopReceive.setFont(font);
		JPanel upJPanel = new JPanel();		//新建一个面板放置按钮
		upJPanel.add(startReceive);
		upJPanel.add(stopReceive);
		add(upJPanel, BorderLayout.NORTH);	//将放置按钮的面板添加到窗体中,并且放置在窗体的上部
		
		thread = new Thread(this);	//构造函数中建立线程
		startReceive.addActionListener(this);	//为开始接收按钮添加监听
		stopReceive.addActionListener(this);	//为停止接收按钮添加监听
		
		
		JPanel textJPanel = new JPanel();	//新建一个面板放置显示接收信息
		textJPanel.setLayout(new GridLayout(1,2));	//设置面板布局为一行两列
		startTextArea.setForeground(Color.red);		//设置显示的文字颜色
		stopTestArea.setForeground(Color.blue);	
		textJPanel.add(startTextArea);		//将显示文本框添加至面板
		textJPanel.add(stopTestArea);	//将接收信息的文本框添加到面板
		
		final JScrollPane scrollPane = new JScrollPane();		//设置滚动条	final表示设置为不可变的，内部调用
		textJPanel.add(scrollPane);
		
		
		
		scrollPane.setViewportView(stopTestArea);	//为文本框添加滚动条
		add(textJPanel, BorderLayout.CENTER);	//将放置文本框的面板添加到窗体	并置于中间部分
				
		setBounds(100, 100, 500, 450);	//设置窗口布局
		setVisible(true);	//设置窗口可见
		
		
		try {
			group = Inet4Address.getByName("224.255.10.0");		//指定广播组地址
			socket = new MulticastSocket(port);		//实例化多播数据包套接字
			socket.joinGroup(group);	//将地址加入广播组
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
		
		//如果当前点击的按钮是开始接收按钮
		if (e.getSource() == startReceive) {
			startReceive.setBackground(Color.yellow);	//设置开始接收按钮的颜色为黄色
			stopReceive.setBackground(Color.red);	//设置停止接受按钮的颜色为红色
			//如果当前线程不是一个开启状态
			if (!thread.isAlive()) {
				thread = new Thread(this);	//新建一个线程对象
				getMessage = true;
			}
			thread.start();
		}
		
		//如果点击的是停止接受的按钮
		if (e.getSource() == stopReceive) {
			startReceive.setBackground(Color.red);	//设置开始接收按钮的颜色为红色
			stopReceive.setBackground(Color.yellow);	//设置停止接受按钮的颜色为黄色
			getMessage = false;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (getMessage) {
			DatagramPacket packet;		//创建接收数据包
			byte data[] = new byte[1024];
			packet = new DatagramPacket(data, data.length, group, port);	//获取接收到的信息
			
			try {
				socket.receive(packet);		//读取数据包
				String message = new String(packet.getData(),0,packet.getLength()); 	//将数据包中的内容转化为字符串
				
				startTextArea.setText("正在接收内容：" + message);
				stopTestArea.append(message + "\n");	//将接收到的信息添加到接收框
				
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
