package 天气播报;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends Thread{
	int port = 9898; 	//定义端口
	InetAddress group;	//定义广播组地址
	MulticastSocket socket;	//多播数据包套接字
	
	public Server() {
		// TODO Auto-generated constructor stub
		//广播组地址范围：224.0.0.0~239.255.255.255
		try {
			group = InetAddress.getByName("224.255.10.0");	//指定广播组的地址
			socket = new MulticastSocket(port);	//实例化多播数据包的套接字
			socket.joinGroup(group);	//加入广播组
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		while (true) {
		// TODO Auto-generated method stub
		DatagramPacket packet;		//创建一个数据包对象
		Date date = new Date();		//实例化时间类对象
		SimpleDateFormat sFormat = new SimpleDateFormat("HH:mm:ss");	//规范化时间格式
		String massage = "[" + sFormat.format(date) + "]天气预报，当前天气：晴";	//将数据信息进行输出
		byte data[]= massage.getBytes();	
		packet = new DatagramPacket(data, data.length, group, port);	//创建数据包
		System.out.println(massage);
		try {
			socket.send(packet);	//将信息写入数据包
			Thread.sleep(1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.start();		//调用底层方法开启线程
	}

}
