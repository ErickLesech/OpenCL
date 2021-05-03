package OpenCLSampleKernel;

import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static java.lang.System.out;
import static org.jocl.CL.CL_DEVICE_NAME;


import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clGetDeviceInfo;

import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLDevice;

import static org.jocl.CL.clGetDeviceIDs;


public class ListDevice {

	public static void main(String[] args) {
		char[] info = null;
		
    	long []infoSize = new long[1];
    	int []deviceCompteur = new int[1];
    	
    	int []platformCompteur = new int[1];
    	    	
    	cl_platform_id[] platform = new cl_platform_id[1];
    	cl_device_id[] device = new cl_device_id[1];
    	
    	//récupérer l'id de la platforme
    	clGetPlatformIDs(0,null,platformCompteur);
    	platform = new cl_platform_id[platformCompteur[0]];
    	clGetPlatformIDs(platformCompteur[0],platform,null);
    	
    	//Devices 
    	
    	clGetDeviceIDs(platform[0],CL_DEVICE_TYPE_ALL,0,null,deviceCompteur);
    	device =  new cl_device_id[deviceCompteur[0]];
    	clGetDeviceIDs(platform[0],CL_DEVICE_TYPE_ALL,deviceCompteur[0],device,null);

    	
    	for(int i = 0; i < deviceCompteur[0]; i++) {
    		infoSize = new long[1];
    		clGetDeviceInfo(device[0],CL_DEVICE_NAME,0,null,infoSize);
    		int taille = (int)infoSize[0];
        	System.out.println("taille de info = " + infoSize[0]);

        	info = new char[taille];
        	System.out.println("taille de info = " + info.length);
        	Pointer ptr_info = Pointer.to(info);
        	clGetDeviceInfo(device[0],CL_DEVICE_NAME,taille,ptr_info,null);
        	
        	System.out.println("valeur de info "+ new String(info));
        	
    	}
    	
    	 CLContext context = CLContext.create();
         System.out.println("created "+context);
    	
    	 CLDevice device1 = context.getMaxFlopsDevice();
         System.out.println("using "+device1);
    	
	}

}
