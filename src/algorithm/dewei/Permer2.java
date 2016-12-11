package algorithm.dewei;

public class Permer2
{
	public static void perm(Item[] items,int n)
	{
		boolean isNext=true;
		while(isNext)
		{
			for(int i=0;i<n;i++)
				System.out.print(items[i]);
			System.out.println();
			int s=0,e=n-1;
			isNext=false;
			if(items[s].direction==-1) s=1;
			if(items[e].direction==1) e=n-2;
			boolean[] activeItems=new boolean[n];
			for(;s<=e;s++)
			{
				if(items[s].value>items[s+items[s].direction].value)
				{
					activeItems[s]=true;
					isNext=true;
				}
			}
			if(isNext)
			{
				int maxActive=0;
				boolean isFirst=true;
				for(int i=0;i<n;i++)
				{
					if(isFirst&&activeItems[i])
					{
						maxActive=i;
						isFirst=false;
					}
					else if(activeItems[i])
					{
						if(items[i].value>items[maxActive].value)
						{
							maxActive=i;
						}
					}
				}
				for(int i=0;i<n;i++)
				{
					if(items[i].value>items[maxActive].value)
					{
						items[i].direction=-items[i].direction;
					}
				}
				swap(items,maxActive,maxActive+items[maxActive].direction);
			}
		}
	}
	private static void swap(Item[] items,int i,int j)
	{
		Item temp=items[i];
		items[i]=items[j];
		items[j]=temp;
	}
	public static void main(String[] args)
	{
		//System.out.println("test");
		int[] list={1,2,3,4,5};
		Item[] items=new Item[list.length];
		for(int i=0;i<list.length;i++)
		{
			Item item=new Item();
			item.value=list[i];
			items[i]=item;
		}
		long startTime=System.currentTimeMillis();
		perm(items,items.length);
		long endTime=System.currentTimeMillis();
		long runTime=endTime-startTime;
		System.out.println("run time:"+runTime+"ms");
	}
}

class Item
{
	int value=0;
	int direction=-1;//-1:left   1:right
	public	String toString()
	{
		return ""+value+":"+direction+" ";
	}
}