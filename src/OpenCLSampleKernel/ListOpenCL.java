package OpenCLSampleKernel;

import static org.jocl.CL.*;
import org.jocl.*;

public class ListOpenCL{
	
	private static String programSource = 
			"__kernel void "+
				    "sampleKernel(__global const float *a,"+
				    "             __global const float *b,"+
				    "             __global float *c)"+
				    "{"+
				    "    int gid = get_global_id(0);"+
				    "    c[gid] = a[gid] + b[gid];"+
				    "}";
	
    public static void main(String[] args) {
    
    	char[] info = null;
    	long []infoSize = new long[1];
    	int []platformCompteur = new int[1];
    	
    	int error;
    	
    	cl_platform_id[] platform = new cl_platform_id[1];
    	clGetPlatformIDs(0,null,platformCompteur);
    	System.out.println(platformCompteur[0]);
    	platform = new cl_platform_id[platformCompteur[0]];
    	clGetPlatformIDs(platformCompteur[0],platform,null);
    	
    	error = clGetPlatformInfo(platform[0],CL_PLATFORM_NAME,0,null,infoSize);
    	
    	if(error == 0)System.out.println("SUCCESS");
    	    	
    	int taille = (int)infoSize[0];
    	info = new char[taille];
    	
    	Pointer ptr_info = Pointer.to(info);
    	
    	error = clGetPlatformInfo(platform[0],CL_PLATFORM_NAME,taille,ptr_info,null);
    	
    	if(error == 0)System.out.println("SUCCESS");

    	
    	System.out.println("valeur de info "+ new String(info));
    	
    	
    
    }
}