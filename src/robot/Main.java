package robot;
import java.util.ArrayList;

import camera.GestionCamera;

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
    		SocketRobotino socketRobotino = new SocketRobotino(ipServer, port,r);
    		new Thread(socketRobotino).start();
    		
    		new Thread(r).start();
    		r.start();
    		//A voir si on laisse		
    		
    		RobotinoTopUn robotTopUn = new RobotinoTopUn(r);
    		new Thread(robotTopUn).start();
    	}
    }	
}