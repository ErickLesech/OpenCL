package OpenCLSampleKernel;

import java.util.Random;

import static org.jocl.CL.CL_DEVICE_MAX_CLOCK_FREQUENCY;
import static org.jocl.CL.CL_DEVICE_MAX_COMPUTE_UNITS;
import static org.jocl.CL.CL_DEVICE_MAX_WORK_GROUP_SIZE;
import static org.jocl.CL.CL_DEVICE_MAX_WORK_ITEM_SIZES;
import static org.jocl.CL.CL_DEVICE_NAME;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_PLATFORM_NAME;
import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.CL_CONTEXT_DEVICES;

import static org.jocl.CL.*;

import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetDeviceInfo;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clGetPlatformInfo;
import static org.jocl.CL.clGetContextInfo;
import static org.jocl.CL.clCreateCommandQueue;

import static org.jocl.CL.clCreateContextFromType;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;



public class main {	
	
	private static String programSource = 
			"__kernel void "+
				    "sampleKernel(__global const float *a,"+
				    "             __global const float *b,"+
				    "             __global float *c)"+
				    "{"+
				    "    int gid = get_global_id(0);"+
				    "    c[gid] = a[gid] + b[gid];"+
				    "}";
	

	

	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		char[] info = null;
		
		Random rand = new Random();
		
		float numBytes[] = new float[8];
		int n = 1000;
		float arrayA[] = new float[n];
		float arrayB[] = new float[n];
		float arrayC[] = new float[n];

		for(int i = 0; i < n; i++) {
			arrayA[i]= i;
			arrayB[i]= i;
		}
		
		System.out.println("Result: "+java.util.Arrays.toString(arrayC));

		
		Pointer srcA = Pointer.to(arrayA);
		Pointer srcB = Pointer.to(arrayB);
		Pointer dst = Pointer.to(arrayC);
		
		final int platFormIndex = 0;
		final long deviceType= CL_DEVICE_TYPE_ALL;
		final int deviceIndex = 0;
		
		CL.setExceptionsEnabled(true);
		
        // Obtain the number of platforms

		int numPlatFormsArray[] = new int[1];
		clGetPlatformIDs(0,null,numPlatFormsArray);
		int numPLatforms = numPlatFormsArray[0];
		
		System.out.println("nombre de plateforme : "+ numPLatforms);
	
		//obtenir l'id de la plateforme
		cl_platform_id platforms[] = new cl_platform_id[numPLatforms];
		clGetPlatformIDs(platforms.length,platforms,null);
		cl_platform_id platform = platforms[platFormIndex];
		
		System.out.println("taille du tableau : " + platforms.length);
		
		System.out.println("ID de la plateforme : "+ platform);
				
		long infoSize[] = new long[1];
		//Pointer ptr_infoSize = Pointer.to(infoSize);

		clGetPlatformInfo(platform, CL_PLATFORM_NAME,0,null,infoSize);
		
        info = new char[(int)infoSize[0]];
        
        Pointer ptr_info = Pointer.to(info);
		
		clGetPlatformInfo(platform, CL_PLATFORM_NAME,infoSize[0],ptr_info,null);
		
		System.out.println("resultat");
	    System.out.println(new String(info));

		
		//obtenir le nombre de device dans la plateforme.
		int numDevicesArray[] = new int[1];
		clGetDeviceIDs(platform,deviceType,0,null,numDevicesArray);
		int numDevices = numDevicesArray[0];
		
		System.out.println("nombre de devices = "+ numDevices);
		
		//obtenir l'id du device
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetDeviceIDs(platform,deviceType,numDevices,devices,null);
		cl_device_id device = devices[deviceIndex];
		
		System.out.println("ID du devices = "+ device);
		
		//initialiser les propriétés du contexte
		
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
		
		
		//création d'un context 
		cl_context context =  clCreateContext(contextProperties,1,new cl_device_id[] {device},null,null,null);
		cl_command_queue commandQueue = clCreateCommandQueue(context,device,0,null);
		
		//les variables locales sont stocker en mémoire.
		cl_mem memObjects[] = new cl_mem[3];
		memObjects[0] = clCreateBuffer(context, 
		    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
		    Sizeof.cl_float * n,srcA , null);
		memObjects[1] = clCreateBuffer(context, 
		    CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
		    Sizeof.cl_float * n, srcB, null);
		memObjects[2] = clCreateBuffer(context, 
		    CL_MEM_READ_WRITE, 
		    Sizeof.cl_float * n, null, null);
		
		//on récupére le programme source 
		cl_program program = clCreateProgramWithSource(context,
	            1, new String[]{ programSource }, null, null);
		
		//on builde le programme
		clBuildProgram(program, 0, null, null, null, null);
		
		//kernel avec le nom de la fonction dans le programme source
		cl_kernel kernel = clCreateKernel(program, "sampleKernel", null);
		
		//setteur des arguments contenu dans la fonction 'sampleKernel' en mémoire
		clSetKernelArg(kernel, 0, 
			    Sizeof.cl_mem, Pointer.to(memObjects[0]));
		clSetKernelArg(kernel, 1, 
			    Sizeof.cl_mem, Pointer.to(memObjects[1]));
		clSetKernelArg(kernel, 2, 
			    Sizeof.cl_mem, Pointer.to(memObjects[2]));
			
			long global_work_size[] = new long[]{n};
			long local_work_size[] = new long[]{1};
			
			clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
				    global_work_size, local_work_size, 0, null, null);
			
			clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
				    n * Sizeof.cl_float, dst, 0, null, null);
			
			
			clReleaseMemObject(memObjects[0]);
			clReleaseMemObject(memObjects[1]);
			clReleaseMemObject(memObjects[2]);
			clReleaseKernel(kernel);
			clReleaseProgram(program);
			clReleaseCommandQueue(commandQueue);
			clReleaseContext(context);
			
			//verify the result
			
			//boolean passed = true;
			//final float epsilon = 1e-7f;
			
			//for(int i = 0 ; i <n; i++) {
			//	float x = arrayC[i];
			//	float y = arrayA[i] + arrayB[i];
				
			//	boolean epsilonEqual = Math.abs(x-y) <= epsilon * Math.abs(x);
				
			//	if(!epsilonEqual) {
			//		passed =false;
			//		break;
			//	}
			//}
			
			//System.out.println("Test " + (passed?"PASSED":"FAILED"));
			
			//if(n <= 10)
			//{
				System.out.println("Result: "+java.util.Arrays.toString(arrayC));
			//}
			
			
			
		
		
		
		
		
		
	}

}
