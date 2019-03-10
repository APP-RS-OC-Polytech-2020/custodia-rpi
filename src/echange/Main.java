package echange;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
    	int port=50008;
		String ipServer="193.48.125.70";
    	ArrayList<String> hostname=new ArrayList<String>();
    	//hostname.add("193.48.125.37");
    	hostname.add("193.48.125.38");
    	for(String host:hostname) {
    		Robot r=new Robot(System.getProperty("hostname", host));
    		SocketRaspberry socketRasp = new SocketRaspberry(ipServer, port,r);
    		new Thread(socketRasp).start();	
    		
    		GestionRobot robotTopUn = new GestionRobot(r, socketRasp);
    		new Thread(robotTopUn).start();
    	}
    }	
}