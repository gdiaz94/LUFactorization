/*
 * Programmer: 	Gabe Diaz
 * Date: 		04/12/21
 * Purpose:		Take a square matrix as an input in a text file, first line is an integer
 * 				representing the number of rows/columns, and each consecutive row is the
 * 				matrix elements. The program applies LU factorization with partial pivoting 
 * 				(A = P'LU) on the input matrix and prints out P'LU.
 */
import java.io.*;
import java.util.Scanner;

public class LUFactorization 
{
	// Factorization and pivot matrices
	static float P[][] = null;
	static float L[][] = null;
	static float U[][] = null;
	public static void main(String[] args)
	{
		int rowColNum = 0;
		float A[][] = null;
		try
		{
			Scanner input = new Scanner(new File("input.txt"));
			rowColNum = input.nextInt();
			// Initialize A
			A = new float[rowColNum][rowColNum];
			for (int i = 0; i < rowColNum; i++)
			{
				for (int j = 0; j < rowColNum; j++)
				{
					A[i][j] = input.nextFloat();
				}
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		// Initialize P (permutation matrix) 1s down the diag, 0s everywhere else
		P = new float[rowColNum][rowColNum];
		for (int i = 0; i < rowColNum; i++)
		{
			for (int j = 0; j < rowColNum; j++)
			{
				P[i][j] = 0;
			}
		}
		for (int i = 0; i < rowColNum; i++)
		{
			P[i][i] = 1;
		}
		
		System.out.println("Original matrix A:");
		printMatrix(A, rowColNum);
		
		System.out.println("\nApplying LU factorization with partial pivoting (A = P'LU)...");
		
		U = elimination(A, rowColNum);
		
		System.out.println("\nP':");
		printMatrix(transposeMatrix(P, rowColNum), rowColNum);
		
		System.out.println("\nL:");
		printMatrix(L, rowColNum);
		
		System.out.println("\nU:");
		printMatrix(U, rowColNum);
		
	}
	
	// Take a square matrix and its row/column number and print its elements to the console
	private static void printMatrix(float matrix[][], int rowColNum)
	{
		for (int i = 0; i < rowColNum; i++)
		{
			for (int j = 0; j < rowColNum; j++)
			{
				System.out.printf("%-5.4f ", matrix[i][j]);
			}
			System.out.println();
		}
	}
	
	// Take a square matrix and its row/column number and return the transpose matrix of it
	private static float[][] transposeMatrix(float matrix[][], int rowColNum)
	{
		float[][] transpose = new float[rowColNum][rowColNum];
		
		for (int i = 0; i < rowColNum; i++)
		{
			for (int j = 0; j < rowColNum; j++)
			{
				transpose[i][j] = matrix[j][i];
			}
		}
		
		return transpose;
	}
	
	// Performs a partial pivot on matrix and updates P accordingly
	private static float[][] pivot(float matrix[][], int rowColNum, int pivotCol)
	{
		// Keeps track of the row with the largest leading value
		int largestIndex = pivotCol;
		for (int i = pivotCol + 1; i < rowColNum; i++)
		{
			if (matrix[i][pivotCol] > matrix[largestIndex][pivotCol])
			{
				largestIndex = i;
			}
		}
		
		if (largestIndex != pivotCol)
		{
			// Swap the row with the largest value with the row being compared
			float[] temp1 = new float[rowColNum];
			float[] temp2 = new float[rowColNum];
			
			for (int i = 0; i < rowColNum; i++)
			{
				temp1[i] = matrix[pivotCol][i];
				temp2[i] = P[pivotCol][i];
				
			}
			
			for (int i = 0; i < rowColNum; i++)
			{
				matrix[pivotCol][i] = matrix[largestIndex][i];
				P[pivotCol][i] = P[largestIndex][i];
			}
			
			for (int i = 0; i < rowColNum; i++)
			{
				matrix[largestIndex][i] = temp1[i];
				P[largestIndex][i] = temp2[i];
			}
		}
		
		return matrix;
	}
	
	// Returns U, Sets L as well
	private static float[][] elimination(float matrix[][], int rowColNum)
	{
		L = new float[rowColNum][rowColNum];
		
		// Initialize L with all 0s, but 1s down the diagonal
		for (int i = 0; i < rowColNum; i++)
		{
			for (int j = 0; j < rowColNum; j++)
			{	
				L[i][j] = (i == j) ? 1 : 0;
			}
		}
		
		// Perform Gaussian elimination on matrix
		// Setting U and L while it is happening
		for (int i = 0; i < rowColNum - 1; i++)
		{
			// Perform any necessary pivot on matrix
			matrix = pivot(matrix, rowColNum, i);
			
			for (int j = i + 1; j < rowColNum; j++)
			{
				float multiplier = matrix[j][i] / matrix[i][i];
				L[j][i] = multiplier;
				
				
				for (int k = i; k < rowColNum; k++)
				{
					matrix[j][k] = matrix[j][k] - matrix[i][k] * multiplier;
				}
			}
		}
		
		return matrix;
	}
}
