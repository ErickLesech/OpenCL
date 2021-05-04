package OpenCLSampleKernel;

import static org.jocl.CL.*;

import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;



public class ListDevice {
	

	public static void main(String[] args) {
		int []deviceInfo = {CL_DEVICE_NAME,CL_DEVICE_VENDOR,CL_DRIVER_VERSION,CL_DEVICE_VERSION,CL_DEVICE_PROFILE};
		
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
    	
    	for(int i = 0; i < platformCompteur[0]; i++) {
    	
    		clGetDeviceIDs(platform[0],CL_DEVICE_TYPE_ALL,0,null,deviceCompteur);
    		device =  new cl_device_id[deviceCompteur[0]];
    		clGetDeviceIDs(platform[0],CL_DEVICE_TYPE_ALL,deviceCompteur[0],device,null);
    		
    		
    		System.out.print("Platform : ");
    		clGetPlatformInfo(platform[0],CL_PLATFORM_NAME,0,null,infoSize);
    		int taillePlatform = (int)infoSize[0];
        	byte[] bufferPlatform = new byte[taillePlatform];
        	Pointer ptr_platform= Pointer.to(bufferPlatform);
        	clGetPlatformInfo(platform[0],CL_PLATFORM_NAME,taillePlatform,ptr_platform,null);
			System.out.println(new String(bufferPlatform));

    	
    			for(int j = 0; j < deviceCompteur[0]; j++) {
    		
    				
    				//affichage des différents infos sur le device
    				for(int k = 0; k < deviceInfo.length; k++) {
    					
    					clGetDeviceInfo(device[0],deviceInfo[k],0,null,infoSize);
    					int taille = (int)infoSize[0];
    					byte[] buffer = new byte[taille];
    					Pointer ptr_device = Pointer.to(buffer);
    					clGetDeviceInfo(device[0],deviceInfo[k],taille,ptr_device,null);
    					System.out.println(new String(buffer));
    					
    				}	
    			}
    		}
		}
	}
