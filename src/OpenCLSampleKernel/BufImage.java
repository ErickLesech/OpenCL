package OpenCLSampleKernel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufImage{
	
    public static void main(String[] args) {

    	BufferedImage img = null;
    	try {
    	    img = ImageIO.read(new File("test.png"));
    	    
    	    int w = img.getWidth();
    	    int h = img.getHeight();
    	    
    	    System.out.println("val w : "+ w + " val h : "+h);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}