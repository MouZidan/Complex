package com.mou.complex;

import android.util.Log;


import java.security.Policy;

public class Complex
{

    private static final String TAG = "Complex";
    private float real, imaginary, radius, theta;
	

	public static int POLAR = 0, CARTESIAN =1;

	public static Complex ZERO = new Complex(0,0,CARTESIAN);
    public static Complex NEGATIVE = new Complex(-1,0,CARTESIAN);
    public static Complex POSITIVE = new Complex(1,0,CARTESIAN);

    public Complex(float a, float b, int form){
		switch(form){
			case 0: 
				this.radius = a;
				this.theta = b;
				calcCartesian();
				break;
			case 1:
				this.real = a;
				this.imaginary= b;
				calcPolar();
				break;
			default: throw new IllegalArgumentException("unknown form of complex number");
		}
	}


	public float getImaginary(){
		return imaginary;
	}
	public float getRadius()
	{
		return radius;
	}
	public float getTheta()
	{
		return theta;
	}
	public float getReal()
	{
		return real;
	}

	private void calcPolar(){

		radius = (float) Math.sqrt(Math.pow(imaginary,2) + Math.pow(real ,2));
		theta =(float) Math.atan2(imaginary, real);

	}
	private void calcCartesian(){
		
		real = (float) Math.cos(theta)* radius;
		imaginary = (float) Math.sin(theta)* radius;
		
	}
	
	public static Complex multiply(Complex a, Complex b){
		float theta = a.theta + b.theta;
		float radius = a.radius * b.radius;
		return new Complex(radius, theta, POLAR);
		
	}
	public static Complex divide(Complex a, Complex b){
	
		float theta  = a.theta - b.theta;
		float radius = a.radius / b.radius;
		return new Complex(radius, theta, POLAR);

	}
	public static Complex add(Complex a, Complex b){
		return new Complex(a.real + b.real, a.imaginary+b.imaginary, CARTESIAN);
	}
	
	public static Complex subtract(Complex a, Complex b){
		return new Complex(a.real - b.real, a.imaginary-b.imaginary, CARTESIAN);
	}

	@Override
	public String toString()
	{
	    float roundedImaginary, roundedReal;
        roundedReal = Math.round(real*10000f)/10000f;
        roundedImaginary = Math.round(imaginary*10000f)/10000f;
        if(roundedImaginary == 0){
            return roundedReal+"";
        }
        if(roundedReal ==0)return roundedImaginary+"i";
		return roundedReal+(roundedImaginary>=0?"+"+roundedImaginary:""+roundedImaginary)+"i";
	}
	public String polarFormString(){
	    return radius+"\\angle"+theta;
    }
	public static Complex parseComplex(String complexNumberString){

        if(complexNumberString.length()==0)return null;
        complexNumberString = complexNumberString.replace(" ", "");
        if(complexNumberString.charAt(0)=='+')
            complexNumberString = complexNumberString.substring(1, complexNumberString.length()-1);


	    String polarFormRegex = "-?\\d+(?:\\.\\d+)?\\\\angle-?\\d+(?:\\.\\d+)?";
        String cartesianFormRegex =
                "[+\\-]?\\d+(?:\\.\\d+)?i[\\+\\-]\\d+(?:\\.\\d+)?|" +
                "[+\\-]?\\d+(?:\\.\\d+)?[\\+\\-]\\d+(?:\\.\\d+)?i|" +
                "[-\\+]?\\d+(?:\\.\\d+)?i?";
	    if(complexNumberString.matches(polarFormRegex)){
            String[] stringParams = complexNumberString.split("\\\\angle");
            return new Complex(Float.parseFloat(stringParams[0]), (float) Math.toRadians(Float.parseFloat(stringParams[1])), POLAR);
        }else if(complexNumberString.matches(cartesianFormRegex)){
            //String[] params = ;
            float[] params = new float[2];
            if(complexNumberString.charAt(0)!='-'){//ie; 90-10i or 90i-10
                if(!complexNumberString.contains("+") && !complexNumberString.contains("-")){
                    if(complexNumberString.contains("i"))
                        return new Complex(0, Float.parseFloat(complexNumberString.replace("i", "")), CARTESIAN);
                    else return new Complex( Float.parseFloat(complexNumberString), 0, CARTESIAN);
                }

                    if(complexNumberString.contains("-")){
                    String[] stringParams = complexNumberString.split("-");
                    if(stringParams[0].contains("i")){
                        params[1] = Float.parseFloat(stringParams[0].replace("i", ""));
                        params[0] = -Float.parseFloat(stringParams[1]);

                    }else{
                        params[0] = Float.parseFloat(stringParams[0]);
                        params[1] = -Float.parseFloat(stringParams[1].replace("i", ""));
                    }
                }else{
                    String[] stringParams = complexNumberString.split("\\+");
                    if(stringParams[0].contains("i")){
                        params[1] = Float.parseFloat(stringParams[0].replace("i", ""));
                        params[0] = Float.parseFloat(stringParams[1]);

                    }else{
                        params[0] = Float.parseFloat(stringParams[0]);
                        params[1] = Float.parseFloat(stringParams[1].replace("i", ""));
                    }
                }
            }else  if(complexNumberString.charAt(0)=='-'){
                complexNumberString = complexNumberString.substring(1, complexNumberString.length());
                if(!complexNumberString.contains("+") && !complexNumberString.contains("-")){
                    if(complexNumberString.contains("i"))
                        return new Complex(0, -Float.parseFloat(complexNumberString.replace("i", "")), CARTESIAN);
                    else return new Complex( -Float.parseFloat(complexNumberString), 0, CARTESIAN);
                }
                if(!complexNumberString.contains("-")){//ie; -90+90i or -90i+90
                    String[] stringParams = complexNumberString.split("\\+");
                    if(stringParams[0].contains("i")){
                        params[1] = Float.parseFloat(stringParams[0].replace("i", ""));
                        params[0] = Float.parseFloat(stringParams[1]);

                    }else{
                        params[0] = Float.parseFloat(stringParams[0]);
                        params[1] = Float.parseFloat(stringParams[1].replace("i", ""));
                    }
                }else{//ie;   -90i-90 or -90-90i
                    String[] stringParams = complexNumberString.split("-");
                    if(stringParams[0].contains("i")){
                        Log.d(TAG, "parseComplex: "+stringParams[0]+"-"+stringParams[1]);
                        params[1] = -Float.parseFloat(stringParams[0].replace("i", ""));
                        params[0] = -Float.parseFloat(stringParams[1]);

                    }else{
                        params[0] = -Float.parseFloat(stringParams[0]);
                        params[1] = -Float.parseFloat(stringParams[1].replace("i", ""));
                    }
                }


            }


            return new Complex(params[0], params[1], CARTESIAN);
        }

        return null;
	}
    @Override
    public boolean equals( Object obj) {
        Complex c = (Complex) obj;
        return c.getReal() == this.real && c.getImaginary() == this.imaginary;
    }
}
