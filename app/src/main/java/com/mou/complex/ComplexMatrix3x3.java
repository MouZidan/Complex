package com.mou.complex;
import android.util.Log;

import java.util.*;

public class ComplexMatrix3x3
{
	private Complex[][] matrix;
	public ComplexMatrix3x3(){
		
		matrix = new Complex[3][3];
	}
	
	public ComplexMatrix3x3(Complex ...elements){
		if(elements.length!=9) throw new IllegalArgumentException("elements must be 9");
		matrix = new Complex[3][3];
		for(int row=0; row < 3; row++){
			for(int col=0; col < 3; col++){
				matrix[row][col] = elements[row*3+col];
			}
		}
	}
	public void setElement(Complex c, int row, int col){
		if(row >2 || col>2 || col<0 || row<0) 
			throw new IllegalArgumentException("position "+row+","+col+" is out of matrix size!");
		matrix[row][col]= c;
		
	}
	public Complex getElement(int row, int col){
		if(row >2 || col>2 || col<0 || row<0) 
			throw new IllegalArgumentException("position "+row+","+col+" is out of matrix size!");
		return matrix[row][col];
		
	}	
	public Complex[] multiplyByColMat(Complex ...columnMatrix){
		if(columnMatrix.length!=3)throw new IllegalArgumentException("column matrix  isnt a system of 3x1");
		Complex[] res= new Complex[3];
		for(int row=0; row <3; row++){
			Complex e = Complex.ZERO;
			for(int i=0; i<3; i++){
				e=e.add(Complex.multiply(matrix[row][i], columnMatrix[i]), e);
			}
			res[row]=e;
			
		}
		return res;
	}
	public ComplexMatrix3x3 multiplyBy(Complex c){
		ComplexMatrix3x3 res = new ComplexMatrix3x3();
		for(int row =0; row<3; row++){
			for(int col =0; col<3; col++){
				res.setElement( Complex.multiply(matrix[row][col], c), row, col);
			}
		}
		return res;
		
	}
	public ComplexMatrix3x3 transpose(){
		ComplexMatrix3x3 transpose = new ComplexMatrix3x3();
		for(int row=0; row<3; row++){
			for(int col=0; col<3; col++){
				transpose.setElement(matrix[row][col], col, row);
			}
		}
		return transpose;
	}
	public Complex determinant(){
		Complex x = Complex.multiply(matrix[0][0], determinant2X2(matrix[1][1] , matrix[1][2], matrix[2][1] , matrix[2][2]));
		Complex y = Complex.multiply(matrix[0][1], determinant2X2(matrix[1][0] , matrix[1][2], matrix[2][0] , matrix[2][2]));
		Complex z = Complex.multiply(matrix[0][2], determinant2X2(matrix[1][0] , matrix[1][1], matrix[2][0] , matrix[2][1]));
		return Complex.add(Complex.subtract(x, y), z);
	}
	public ComplexMatrix3x3 minor(){
	
		ComplexMatrix3x3 minor = new ComplexMatrix3x3();
	
		minor.setElement(determinant2X2(matrix[1][1] , matrix[1][2], matrix[2][1], matrix[2][2]), 0, 0);
		minor.setElement(determinant2X2(matrix[1][0] , matrix[1][2], matrix[2][0], matrix[2][2]), 0, 1);
		minor.setElement(determinant2X2(matrix[1][0] , matrix[1][1], matrix[2][0], matrix[2][1]), 0, 2);
		minor.setElement(determinant2X2(matrix[0][1] , matrix[0][2], matrix[2][1], matrix[2][2]), 1, 0);
		minor.setElement(determinant2X2(matrix[0][0] , matrix[0][2], matrix[2][0], matrix[2][2]), 1, 1);
		minor.setElement(determinant2X2(matrix[0][0] , matrix[0][1], matrix[2][0], matrix[2][1]), 1, 2);
		minor.setElement(determinant2X2(matrix[0][1] , matrix[0][2], matrix[1][1], matrix[1][2]), 2, 0);
		minor.setElement(determinant2X2(matrix[0][0] , matrix[0][2], matrix[1][0], matrix[1][2]), 2, 1);
		minor.setElement(determinant2X2(matrix[0][0] , matrix[0][1], matrix[1][0], matrix[1][1]), 2, 2);
		
		return minor;
	}
	private Complex determinant2X2(Complex a, Complex b, Complex c, Complex d){
		return Complex.subtract( Complex.multiply(a , d) , Complex.multiply(c , b));
	}

	public ComplexMatrix3x3 inverse(){
		Complex determinant = this.determinant();
		if(determinant.equals(Complex.ZERO))return null;
		ComplexMatrix3x3 minors = minor();
		Complex sign = Complex.POSITIVE;
		for(int i =0; i<3; i++){
			for(int j =0; j<3; j++){
				minors.setElement(Complex.multiply(sign, minors.getElement(i, j)), i, j);
				sign = Complex.multiply(sign, Complex.NEGATIVE);
			}
		}
		ComplexMatrix3x3 transpose = minors.transpose();
		Log.d("Complex", "inverse: transpose=\n"  +transpose );

		Log.d("Complex", "inverse: determinant="  +determinant );
		return transpose.multiplyBy(Complex.divide(Complex.POSITIVE, determinant));
	}

	@Override
	public String toString()
	{
		// TODO: Implement this method
		StringBuilder b = new StringBuilder();
		for(int row=0; row < 3; row++){
			for(int col=0; col < 3; col++){
				b.append(matrix[row][col].toString()).append(" ");
			}
			b.append("\n");
		}
		return b.toString();
	}


}
