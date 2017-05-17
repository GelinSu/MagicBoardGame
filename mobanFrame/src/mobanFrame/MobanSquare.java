package mobanFrame;

import java.awt.*; 
import java.awt.event.*; 
public class  MobanSquare 
{   
	public int x,y;      
	private int order;  
	private int nowSpaceOrder;  
	private boolean squareHere;  
	public boolean arrive;   
	private Image squareImage;   
	
	public MobanSquare(int px,int py,int order,int spaceOrder) //MobanSquare的构造函数  
	{   
		this.x=px;   
		this.y=py;   
		this.order=order;    
		this.nowSpaceOrder=spaceOrder;     
		if (this.order==this.nowSpaceOrder)
			arrive=true;    
		else     
			arrive=false;    
		if (this.order==this.nowSpaceOrder||this.order==-1)     
			squareHere=false;    
		else     
			squareHere=true;   
		}
	
	public boolean GetSquareHere()        //得到当前位置是否有方块  
	{   
		return this.squareHere;   
	}   
	public void SetSquareHere(boolean b)  //设置当前位置是否是方块  
	{   
		this.squareHere=b;  
	}   
	public int GetOrder()             //得到当前位置方块上的序号  
	{   
		return this.order;  
	}   
	public void SetOrder(int order)  //设置当前位置方块上的序号   
	{          
		this.order=order;   
		if (this.order==this.nowSpaceOrder)   
		{    
			arrive=true;    
			squareHere=false;    
			}   
		else     {
			arrive=false;    
			squareHere=true;    
			}   
		}   
	public Image GetImage()  //获得当前位置方块上图像块  
	{   
		return this.squareImage;   
	}   
	public void SetImage(Image ima)  //设置当前位置方块上图像块   
	{   
		this.squareImage=ima;  
	}  
}
		
