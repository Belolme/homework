package algorithm.dewei;

public class Permer1
{
	public static void perm(int[] list,int k,int m)
	{
		if(k==m)
		{
			for(int i=0;i<=m;i++)
				System.out.print(""+list[i]+" ");
			System.out.println();
		}
		else
		{
			for(int i=k;i<=m;i++)
			{
				swap(list,k,i);
				perm(list,k+1,m);
				swap(list,k,i);
			}
		}
	}
	private static void swap(int[] list,int k,int m)
	{
		int temp=list[k];
		list[k]=list[m];
		list[m]=temp;
	}
	public static void main(String[] args)
	{
		int[] list={1,2,3,4,5};
		long startTime=System.currentTimeMillis();
		perm(list,0,list.length-1);
		long endTime=System.currentTimeMillis();
		long runTime=endTime-startTime;
		System.out.println("run time:"+runTime+"ms");
	}
}