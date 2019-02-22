package robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;


public class SocketRobotino implements Runnable {

	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;
	int port=50007;
	//String ipServer="192.168.56.1";//iplocal
	String ipServer="193.48.125.70";
	Robot robot;
	public SocketRobotino(String ipServer,int  port,Robot robot){
		this.robot=robot;
		this.port=port;
		this.ipServer=ipServer;
		try {
			clientSocket = new Socket(ipServer, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("{\"type\":\"init\",\"infoInit\":\"Client-->Server  demande de connexion\", \"clientName\": \""+""+"\", \"clientType\":\"Robotino\"}");//ajouter le nom du robot
		out.println("{\"type\":\"message\",\"message\":\"testRobotino\"}");
		
	}
	public void envoyerMessage(String m){
		out.println(m);
	}
	public void run() {
		String inLine="";
		while(inLine!=null){
			try {
				inLine = in.readLine();
				decodeurJson(inLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("client\tgetIntputStreamServer: "+inLine);
		}
	}
	public void decodeurJson(String j) {
		try{
			JSONObject JSON = new JSONObject(j);
			String type = JSON.getString("type");
			System.out.println("CoRobo\ttype:"+type);
			
			if(type.equals("init")){
				String info = JSON.getString("infoInit");
				System.out.println("CoRobo\tinfo: "+info);
				
			}else if(type.equals("message")){
				String message = JSON.getString("message");
				System.out.println("CoRobo\tMessage: "+message);
				//exmple pour le décodage de JSON
				//String dName = JSON.getJSONObject("infoCommande").getJSONObject("destinataire").getString("name");
			}else if(type.equals("commande")){
				this.robot.dataProcessing(JSON);				
			}
		}catch(org.json.JSONException e){
			System.out.println("CoRobo\terreur decodage JSON: "+e);
			System.out.println("CoRobo\tJSON: "+j);
		}
	}
}