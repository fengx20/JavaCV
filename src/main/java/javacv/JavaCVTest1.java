package javacv;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacv.*;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;

public class JavaCVTest1 {
	
	 public void showFramesWithFace(String winTitle, FrameGrabber grabber) throws FrameGrabber.Exception, InterruptedException {
        OpenCVFrameConverter.ToMat convertToMat = new OpenCVFrameConverter.ToMat();
        File fileAbsolutePath = new File(ClassLoader.getSystemClassLoader().getResource("lbpcascade_frontalface_improved.xml").getFile());
        //opencv_objdetect.CvHaarClassifierCascade face_cascade=opencv_objdetect.cvLoadHaarClassifierCascade(fileAbsolutePath.getAbsolutePath(),new opencv_core.CvSize(0,0));
        @SuppressWarnings("resource")
		opencv_objdetect.CascadeClassifier face_cascade = new opencv_objdetect.CascadeClassifier(fileAbsolutePath.getAbsolutePath());
        opencv_core.RectVector faces = new opencv_core.RectVector();
        CanvasFrame canvas = new CanvasFrame(winTitle,1);//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isVisible()) {
                break;
            }
            Frame frame = grabber.grab();  //获取摄像头图像的一帧
            Mat mat = convertToMat.convert(frame);  //将获取的frame转化成mat数据类型
            if (mat.empty())
                continue;
            Mat videoMatGray = new Mat();
            opencv_imgproc.cvtColor(mat, videoMatGray, COLOR_BGRA2GRAY);
            opencv_imgproc.equalizeHist(videoMatGray, videoMatGray);
            //int[] rejectLevels = new int[0];
            //double[] levelWeights = new double[0];
            face_cascade.detectMultiScale(videoMatGray, faces); //人脸检测
            if (faces.size() > 0) {
    			opencv_imgcodecs.imwrite("face.jpg", mat); 
	            for (int i = 0; i < faces.size(); i++) {
	                opencv_core.Rect face = faces.get(i);
	                opencv_imgproc.rectangle(mat, face, opencv_core.Scalar.RED, 4, 8, 0);
	            }
            }else {
				/* System.out.println("未检测到人脸！"); */
            }

            //opencv_highgui.imshow(winTitle, mat);
            //opencv_highgui.waitKey(30);
            canvas.showImage(convertToMat.convert(mat));
            Thread.sleep(50);//50毫秒刷新一次图像
        }
    }
	 
	 @Test
	    public void testFaceRecognize() throws FrameGrabber.Exception, InterruptedException, MalformedURLException, FrameRecorder.Exception {
	        OpenCVFrameGrabber grabber = OpenCVFrameGrabber.createDefault(0);
	        grabber.start();
	        showFramesWithFace("Video", grabber);
	        grabber.stop();
	        grabber.close();
	    }
	 
	 


}
