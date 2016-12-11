package algorithm.dewei;

import java.util.Scanner;
public class DF
{
	public static void main(String[] args)
	{
		Scanner scanner=new Scanner(System.in);
		String str=scanner.nextLine();
//		String str="()[)]";
		char[] a=str.toCharArray();
		int l=a.length;
		int[][] dp=new int[l][l];
		for(int i=0;i<l;i++)
			dp[i][i]=1;
		for(int len=2;len<=l;len++)
		{
			for(int s=0;s<=l-len;s++)
			{
				int e=s+len-1;
				dp[s][e]=999999;
				if((a[s]=='('&&a[e]==')')||(a[s]=='['&&a[e]==']'))   
					dp[s][e]=dp[s][e]<dp[s+1][e-1]?dp[s][e]:dp[s+1][e-1]; 
				if((a[s]=='('&&a[e]!=')')||(a[s]=='['&&a[e]!=']'))   
					dp[s][e]=(dp[s][e]<dp[s][e-1]?dp[s][e]:dp[s][e-1])+1; 
				if((a[e]==')'&&a[s]!='(')||(a[e]==']'&&a[s]!='['))   
					dp[s][e]=(dp[s][e]<dp[s+1][e]?dp[s][e]:dp[s+1][e])+1; 
				for(int k=s;k<e;k++)  
					dp[s][e]=dp[s][e]<dp[s][k]+dp[k+1][e]?dp[s][e]:dp[s][k]+dp[k+1][e];
			}
		}
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<l;j++)
				System.out.print(""+dp[i][j]+" ");
			System.out.println();
		}
		System.out.println(dp[0][l-1]);
	}
}
