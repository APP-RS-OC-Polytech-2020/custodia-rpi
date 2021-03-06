package camera;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class CameraGrabber {
	private	VideoCapture Camera;
	private int fps;

	public int CameraGetFps() { return fps; }
	public CameraGrabber() { }
	public CameraGrabber(int whichCamera, int fps) {
		CameraStart(whichCamera, fps);
	}
	public void CameraStart(int whichCamera, int fps) {
		Camera = new VideoCapture(0);
		Camera.open(whichCamera);
		this.fps = 1000/fps;
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			
		}
	}
	public boolean isOpen() {
		return (Camera.isOpened());
	}
	public Mat Capture() {
		Mat frame = new Mat();
		Camera.read(frame);
		return frame;
	}
	public void Close() {
		Camera.release();
	}
	public double getFps(){
		try{
			double fps=Camera.get(Videoio.CAP_PROP_FPS);
			return fps;
		}catch(Exception e){
			return 0;
		}
	}
}