package echange;
import java.util.ArrayList;

import picocli.CommandLine.Option;

public class Main implements Runnable {
	@Option(names = {"-p","--port"}, description = "Port of the server to connect to (default: ${DEFAULT-VALUE})")
	int port=50008;
	@Option(names = {"-a","--addr"}, description = "Address of the server to connect to (default: ${DEFAULT-VALUE})")
	String ipServer="193.48.125.70";
	@Option(names = {"-ipr","--iprobotino"}, description = "ip of the robotino we want to control (default: ${DEFAULT-VALUE})")
	String robotinoip = "193.48.125.37";
	
    //public static void main(String[] args)
	public void run() {

    	ArrayList<String> hostname=new ArrayList<String>();
    	hostname.add(robotinoip);
    	//hostname.add("193.48.125.38");
    	for(String host:hostname) {
    		Robot r=new Robot(System.getProperty("hostname", host));
    		SocketRaspberry socketRasp = new SocketRaspberry(ipServer, port,r);
    		new Thread(socketRasp).start();	
    		
    		GestionRobot robotTopUn = new GestionRobot(r, socketRasp);
    		new Thread(robotTopUn).start();
    		robotTopUn.start();
    	}
	}
}