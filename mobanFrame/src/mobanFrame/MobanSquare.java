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
	
	public MobanSquare(int px,int py,int order,int spaceOrder) //MobanSquare�Ĺ��캯��  
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
	
	public boolean GetSquareHere()        //�õ���ǰλ���Ƿ��з���  
	{   
		return this.squareHere;   
	}   
	public void SetSquareHere(boolean b)  //���õ�ǰλ���Ƿ��Ƿ���  
	{   
		this.squareHere=b;  
	}   
	public int GetOrder()             //�õ���ǰλ�÷����ϵ����  
	{   
		return this.order;  
	}   
	public void SetOrder(int order)  //���õ�ǰλ�÷����ϵ����   
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
	public Image GetImage()  //��õ�ǰλ�÷�����ͼ���  
	{   
		return this.squareImage;   
	}   
	public void SetImage(Image ima)  //���õ�ǰλ�÷�����ͼ���   
	{   
		this.squareImage=ima;  
	}  
}
		
