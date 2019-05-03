package camera;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;

import org.json.JSONException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import echange.Robot;
import echange.SocketRaspberry;

public class GestionCamera implements Runnable{
	private CameraGrabber cam;
	private boolean camIsRunning;
	public boolean findQRCode = false;
	private Mat matImg;
	private Mat grayFrame;
	public String zone;
	Robot robot;
	private SocketRaspberry sr;
	
	public GestionCamera(Robot robot,SocketRaspberry sr){	
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		System.out.println("Open CV Processor Loaded");
		this.robot=robot;
		this.sr= sr;
	}
	
	public void imageProcessing() throws InterruptedException, JSONException{
		if(camIsRunning){
			camIsRunning=false;
			cam.Close();
		}
		this.cam=new CameraGrabber(0,60);
		
		this.camIsRunning=true;
		if(!this.robot.isManual()){
			while (true) {
				this.matImg = cam.Capture();
				grayFrame= new Mat();
				Imgproc.cvtColor(matImg, grayFrame, Imgproc.COLOR_BGR2GRAY);
				// equalize the frame histogram to improve the result
				Imgproc.equalizeHist(grayFrame, grayFrame);
								
				BufferedImage bufImg = new BufferedImage(this.matImg.cols(), this.matImg.rows(), BufferedImage.TYPE_3BYTE_BGR);
				this.matImg.get(0, 0, ((DataBufferByte)bufImg.getRaster().getDataBuffer()).getData());
				
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImg)));
				
				Result qrCodeResult;
				try {
					qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
					ResultPoint[] points = qrCodeResult.getResultPoints();
					float hauteurQrCode = points[0].getY() - points[1].getY();
					//12 = taille en cm du qrCode, 480 resolution en pixel pour l'hauteur de l'image de la camÃ©ra
					float distanceRob = 12* 480/hauteurQrCode;
					this.findQRCode=true;
					recalibrage(points[0].getX(),distanceRob);
					//System.out.println("Le robot est à  " + distanceRob + " cm du QRCode");
					//System.out.println(qrCodeResult.getText());
					this.zone = qrCodeResult.getText();
					Thread.sleep(100);
					
				} catch (NotFoundException e1) {
					//e1.printStackTrace();
				}			
				//System.out.println("FPS: "+cam.getFps());	
				try {
					Thread.sleep(cam.CameraGetFps());
				} catch (Exception e) {			
			}
			}
		}
	}
	
	
	public void run() {
		while(true){
			try {
				imageProcessing();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void start(){
		System.out.println("Initialisation camera");
	}
	
    //QRCode
    public void recalibrage(float valueX, float distance) throws InterruptedException, JSONException{
    	if(!this.robot.demiTourOk && !this.robot.RobotIsOk){
			if(valueX < 180){
				this.sr.EnvoiDriveRobot(0,10,5);
			}else if(valueX > 370){
				this.sr.EnvoiDriveRobot(0,-10,5);
			}else if(distance>70){
				this.sr.EnvoiDriveRobot(10,0,5);
			}else {
				this.sr.EnvoiDriveRobot(0,0,0);
				this.robot.RobotIsOk = true;
			}
    	}
	}
}