package camera;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

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
	
	public int imageProcessing() throws InterruptedException{
		if(camIsRunning){
			camIsRunning=false;
			cam.Close();
		}
		this.cam=new CameraGrabber(0,60);
		
		this.camIsRunning=true;
		//int absoluteBodySize = 0;
		//Object bodyCascade;
		while (true) {
			this.matImg = cam.Capture();
			//MatOfRect bodies = new MatOfRect();
			
			// convert the frame in gray scale
			Imgproc.cvtColor(matImg, grayFrame, Imgproc.COLOR_BGR2GRAY);
			// equalize the frame histogram to improve the result
			Imgproc.equalizeHist(grayFrame, grayFrame);
			
			// compute minimum body size (20% of the frame height, in our case)
			/*if (absoluteBodySize == 0)
			{
				int height = grayFrame.rows();
				if (Math.round(height * 0.2f) > 0)
				{
					absoluteBodySize = Math.round(height * 0.2f);
				}
			}*/
			/*
			// detect full bodies
			bodyCascade=new CascadeClassifier("/home/pi/opencv/opencv-4.0.0/data/haarcascades/haarcascade_lowerbody.xml");
			((CascadeClassifier) bodyCascade).detectMultiScale(grayFrame, bodies, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteBodySize, absoluteBodySize), new Size());
					
			// draw rectangles
			Rect[] bodiesArray = bodies.toArray();
			for (int i = 0; i < bodiesArray.length; i++)
				Imgproc.rectangle(matImg, bodiesArray[i].tl(), bodiesArray[i].br(), new Scalar(0, 255, 0), 3);
			
			//possibility to detect other parts of the body : upperbody, lowerbody, face
			*/
			
			BufferedImage bufImg = new BufferedImage(this.matImg.cols(), this.matImg.rows(), BufferedImage.TYPE_3BYTE_BGR);
			this.matImg.get(0, 0, ((DataBufferByte)bufImg.getRaster().getDataBuffer()).getData());
			
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufImg)));
			
			Result qrCodeResult;
			try {
				qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
				
				//Phase test voir ce que donne points
				//float scaleFactor=2;
				ResultPoint[] points = qrCodeResult.getResultPoints();
				System.out.println("Info Image totale:  Longueur : "+ bufImg.getWidth() + " Hauteur : " + bufImg.getHeight());
				for (int i = 0; i < points.length; i++) {
					System.out.println(points[i]);
				}
				/*if(points.length>3){
					Point topleft = new Point(points[0].getX(), points[0].getY());
					Point bottomRight = new Point(points[1].getX(), points[1].getY());
					Imgproc.rectangle(matImg, topleft, bottomRight, new Scalar(255, 0, 0), 3);
					bufImg = new BufferedImage(matImg.cols(), matImg.rows(), BufferedImage.TYPE_3BYTE_BGR);
					matImg.get(0, 0, ((DataBufferByte)bufImg.getRaster().getDataBuffer()).getData());
				}*/
				float hauteurQrCode = points[0].getY() - points[1].getY();
				//12 = taille en cm du qrCode, 480 resolution en pixel pour l'hauteur de l'image de la camÃ©ra
				float distanceRob = 12* 480/hauteurQrCode;
				this.findQRCode=true;
				int valuereturn =recalibrage(points[0].getX(),distanceRob);
				
				System.out.println("Le robot est à  " + distanceRob + " cm du QRCode");
				System.out.println(qrCodeResult.getText());
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
	
	
	public void run() {
		while(true){
			try {
				imageProcessing();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void start(){
		System.out.println("Initialisation camera");
	}
	
    //QRCode
    public int recalibrage(float valueX, float distance) throws InterruptedException{
		if(valueX < 180){
			this.sr.EnvoiDriveRobot(10,-5,5);
		}else if(valueX > 370){
			this.sr.EnvoiDriveRobot(10,5,5);
		}else if(distance <30){
			this.robot.RobotIsOk = true;
			return 1;
		}
		return 0;
	}
}