package ��������;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server extends Thread{
	int port = 9898; 	//����˿�
	InetAddress group;	//����㲥���ַ
	MulticastSocket socket;	//�ಥ���ݰ��׽���
	
	public Server() {
		// TODO Auto-generated constructor stub
		//�㲥���ַ��Χ��224.0.0.0~239.255.255.255
		try {
			group = InetAddress.getByName("224.255.10.0");	//ָ���㲥��ĵ�ַ
			socket = new MulticastSocket(port);	//ʵ�����ಥ���ݰ����׽���
			socket.joinGroup(group);	//����㲥��
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
		DatagramPacket packet;		//����һ�����ݰ�����
		Date date = new Date();		//ʵ����ʱ�������
		SimpleDateFormat sFormat = new SimpleDateFormat("HH:mm:ss");	//�淶��ʱ���ʽ
		String massage = "[" + sFormat.format(date) + "]����Ԥ������ǰ��������";	//��������Ϣ�������
		byte data[]= massage.getBytes();	
		packet = new DatagramPacket(data, data.length, group, port);	//�������ݰ�
		System.out.println(massage);
		try {
			socket.send(packet);	//����Ϣд�����ݰ�
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
		server.start();		//���õײ㷽�������߳�
	}

}
