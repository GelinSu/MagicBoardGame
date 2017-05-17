package mobanFrame;

import java.awt.*;
import java.awt.event.*; 
import java.net.*; 
import java.applet.Applet; 
import javax.imageio.ImageIO; 
import java.io.*; 
import java.awt.image.*; 
import javax.swing.JOptionPane;  

public class MobanFrame extends Frame  implements  ActionListener,MouseListener,ItemListener {  
	MobanSquare[][] position;  //��������   
	Point startPoint=new Point(110,120);  //�������ͼ�����ʼ��  
	Point rightStartPoint=new Point(450,120); //�����ұ�ͼ�����ʼ��  
	int[] num;  //�洢�����������  
	Point[] pointMove;    
	int totalStep=0;  //�ܹ����ߵĲ���    
	String selectedImage="��ţ��";  //��ǰѡ�е�Ҫ��Ϸͼ��   
	String gamingImage="                               ";  //������Ϸ��ͼ��  
	boolean startGame=false;    
	int squareNumber=3;  
	int level=1;  //��Ϸ�ȼ�  
	int squareSize=80;     
	Image sourceImage; //��Ϸͼ�����Դͼ�� 
	Image spaceSourceImage; //�ո�ͼ�����Դͼ��  
	Image spaceImage;   
	Image[] myImage; //�洢���ҵ�ͼ�������  Graphics2D       
	/*--------------------*/ 
	/*��ʾ�ڽ����ϵ�һЩ���*/ 
	/*--------------------*/  
	TextField tfx;  
	TextField tfy;   
	MenuBar mnbMyMenuBar;   
	Menu mnChooseLevel;    
	MenuItem mniLevel1, mniLevel2;  
	Label lbStep; //��ʾ���ߵĲ���  
	Label lbSuccess=new Label("");  
	Button btnStartGame;  
	Choice chChoiceImage;   
	
	public MobanFrame()  {   
		super("ħ����Ϸ");    
		pointMove=new Point[4];  //��������ƶ����ĸ�����  
		pointMove[0]=new Point(-1,0);   
		pointMove[1]=new Point(1,0);   
		pointMove[2]=new Point(0,-1);   
		pointMove[3]=new Point(0,1);   
		setLayout(new FlowLayout());   
		chChoiceImage=new Choice();   
		chChoiceImage.add("��ţ��");   
		chChoiceImage.add("���˶��ǵ�");   
		chChoiceImage.add("����a��");   
		chChoiceImage.add("������");     
		chChoiceImage.add("������è");
		chChoiceImage.add("����С��");   
		chChoiceImage.add("����");   
		chChoiceImage.add("С��");   
		chChoiceImage.add("С��2");   
		chChoiceImage.add("Сè");   
		chChoiceImage.add("Baby");   
		chChoiceImage.add("����");   
		mniLevel1=new MenuItem("��");   
		mniLevel2=new MenuItem("����");   
		mnChooseLevel=new Menu("�Ѷ�");   
		mnbMyMenuBar=new MenuBar();         
		tfx=new TextField(8);          
		tfy=new TextField(8);   
		tfx.setText("0");   
		tfy.setText("1");   
		tfx.setVisible(false);   
		tfy.setVisible(false);    
		lbStep=new Label("���߲�����  "+Integer.toString(totalStep));   
		btnStartGame=new Button("��ʼ��Ϸ");   
		mnChooseLevel.add(mniLevel1);   
		mnChooseLevel.add(mniLevel2);   
		mnbMyMenuBar.add(mnChooseLevel);   
		this.setMenuBar(mnbMyMenuBar);   
		add(tfx);   
		add(tfy);   
		add(lbSuccess);   
		add(lbStep);   
		add(chChoiceImage);     
		add(btnStartGame);
		mniLevel1.addActionListener(this);   
		mniLevel2.addActionListener(this);   
		btnStartGame.addActionListener(this);   
		chChoiceImage.addItemListener(this);   
		this.addMouseListener(this);      
		addWindowListener(new WindowAdapter()                     
		{                        
			public void windowClosing(WindowEvent e)                      
			{                           
				System.exit(0);                     
				}                   
			});  
		ProInit();   
		GetImage();   
		GetRandom();   
		setSize(800,500);   
		setVisible(true);   
		}     
	public void GetImage()  //��ȡҪ��ɵ�ƴͼ������ƴͼ����  
	{   
		myImage=new Image[squareNumber*squareNumber];   
		ImageFilter cropFilter1;    
		ImageFilter cropFilter2;    
		ImageFilter cropFilter3;   
		Toolkit tool=getToolkit();
		String filepath="src";		//��ȡ��ǰĿ¼��·��
		File file=new File(filepath);
		sourceImage=tool.createImage("src/"+selectedImage+".jpg");      
		spaceSourceImage=tool.createImage("src/space.jpg");    
		sourceImage=sourceImage.getScaledInstance(squareSize*squareNumber,squareSize*squareNumber,Image.SCALE_DEFAULT); //�õ�һ��ָ����С��ͼ��
		cropFilter1 =new CropImageFilter(0,0,squareSize*squareNumber,squareSize*squareNumber);//�ĸ������ֱ�Ϊͼ���������Ϳ�ߣ���CropImageFilter(int x,int y,int width,int height)��    
		sourceImage=createImage(new  FilteredImageSource(sourceImage.getSource(),cropFilter1));         
		for(int i=0;i<squareNumber*squareNumber;i++) //��Դͼ���ϰ�ѡ�����Ϸ�ѶȽ�ȡ��Ӧ������Сͼ���     
			{     
			cropFilter2 =new CropImageFilter((i%squareNumber)*squareSize,(i/squareNumber)*squareSize,squareSize,squareSize);     
			myImage[i]=createImage(new  FilteredImageSource(sourceImage.getSource(),cropFilter2));   
			}      
		cropFilter3=new CropImageFilter(0,0,squareSize,squareSize);    
		spaceImage=createImage(new  FilteredImageSource(spaceSourceImage.getSource(),cropFilter3));   
		myImage[squareNumber*squareNumber-1]=spaceImage;   
		}   
	
	public void ProInit()  {   
		num=new int[squareNumber*squareNumber];     
		position=new MobanSquare[squareNumber+2][squareNumber+2];    
	}     
	
	public void paint(Graphics g)  //����ƴͼ  
	{   
		boolean showSuccess=true;    
		lbStep.setText("���߲�����  "+Integer.toString(totalStep));     
		if (startGame) 
		{    
			for (int i=1;i<squareNumber+1;i++)     
			{	      
				for (int j=1;j<squareNumber+1;j++)           
				{        
					g.drawImage(position[i][j].GetImage(),startPoint.x+(position[i][j].y-1)*squareSize,startPoint.y+(position[i][j].x-1)*squareSize,this); //������ߴ��ҵķ���     
				}      
			}       
			g.setColor(Color.cyan);     
			for (int i=0;i<squareNumber+1;i++)       
			{        
				g.drawLine(startPoint.x+squareSize*i,startPoint.y,startPoint.x+squareSize*i,startPoint.y+squareNumber*squareSize);        
				g.drawLine(startPoint.x,startPoint.y+squareSize*i,startPoint.x+squareNumber*squareSize,startPoint.y+squareSize*i);    
			}                 
		}   
		g.drawImage(sourceImage,rightStartPoint.x,rightStartPoint.y,this);     //�����ұ߲���ͼ     //
		g.drawImage(spaceImage,rightStartPoint.x+(squareNumber-1)*squareSize,rightStartPoint.y+(squareNumber-1)*squareSize,this);   
		for (int i=1;i<squareNumber+1;i++)  //�ж��û��Ƿ����ƴͼ    
		{     
			for (int j=1;j<squareNumber+1;j++)     
			{          
				if (position[i][j].GetOrder()!=(i-1)*squareNumber+(j-1))
				{       
					showSuccess=false;      
				}      
			}      
		}    
		if(showSuccess)//�û����ƴͼ��������ɶԻ���   
		{     
			showSuccess=false; 
			System.out.println();
			lbSuccess.setText("\n��ϲ�����ɹ����");    
		}   
	}   
	
	public void GetRandom() //���������,�������ͼ�񸳸�ÿ������  
	{     
		int k=squareNumber*squareNumber;  
		boolean numExist=false;   
		int total=0;     
		for (int j=0;j<squareNumber*squareNumber;j++)       
		{    
			num[j]=-1;      
			}    
		while(total<squareNumber*squareNumber)           //�����뷽�������ͬ�������(0..�������-1)   
		{            
			k=((int)(Math.random()*10)+(int)(Math.random()*10))%(squareNumber*squareNumber);    
			for (int j=0;j<total;j++)        
			{        
				if (k==num[j]) 
				{      
					numExist=true;      
					break;        
					}        
				}    
			if (!numExist)    
			{     
				num[total]=k;     
				total++;      
				}       
			numExist=false;       
		}       
		for(int i=0;i<squareNumber+2;i++)           
		{    
			for (int j=0;j<squareNumber+2;j++)       
			{      
				if(i==0||i==squareNumber+1||j==0||j==squareNumber+1)  //ʵ������Χ����,��Щ�����ǲ��ɼ���         
					{       
					this.position[i][j]=new  MobanSquare(i,j,-1,squareNumber*squareNumber-1);       
					}        
				}     
			}    
		for (int i=1;i<squareNumber+1;i++)       //ʵ����Ҫ��ʾ���û��ķ��飬��Щ����ɼ����������ƶ�   
			{    
			for (int j=1;j<squareNumber+1;j++)    
			{         
				this.position[i][j]=new MobanSquare(i,j,num[(i-1)*squareNumber+j-1],squareNumber*squareNumber-1);      
				this.position[i][j].SetImage(myImage[this.position[i][j].GetOrder()]);    
				}    
			}   
		}       
	public void Move(int x,int y)  //�ƶ�����  
	{   
		int square_X,square_Y,order_Old,order_New;    
		square_X=(y-1-startPoint.y)/squareSize+1;    
		square_Y=(x-1-startPoint.x)/squareSize+1;    
		for (int i=0;i<4;i++)     
		{     
			if  (position[square_X+pointMove[i].x][square_Y+pointMove[i].y].arrive) //��ǰ��������ƶ������հ׿��������ڿ�    
				{         
				this.totalStep++;      
				Point  newPoint=new  Point(square_X+pointMove[i].x,square_Y+pointMove[i].y);     
				order_Old=position[square_X][square_Y].GetOrder();     
				order_New=position[newPoint.x][newPoint.y].GetOrder();     
				position[square_X][square_Y].SetOrder(order_New);      
				position[square_X][square_Y].SetImage(myImage[order_New]);     
				position[newPoint.x][newPoint.y].SetOrder(order_Old);      
				position[newPoint.x][newPoint.y].SetImage(myImage[order_Old]);     
				repaint();         
				break;
				}     
			}    
		}    
	public void actionPerformed(ActionEvent e)  {   
		if (e.getSource()==btnStartGame)  //���"��ʼ��Ϸ��ť"   
		{    
			lbSuccess.setText("                               ");    
			totalStep=0;     
			if (level==1)         //�Ѷ�Ϊ��    
				{        
				squareNumber=3;      
				}     
			if (level==2)           //�Ѷ�Ϊ����    
				{       
				squareNumber=4;      
				}     
			startGame=true;     
			gamingImage=selectedImage;    
			ProInit();    
			GetImage();    
			GetRandom();    
			repaint();    
		}    
		if (e.getSource()==mniLevel1) //��"�Ѷ�"�˵�����ѡ����"��"�˵�����     
		{ 
			level=1;     
			startPoint=new Point(110,120);  //�������ͼ�����ʼ��    
			rightStartPoint=new Point(450,120); //�����ұ�ͼ�����ʼ��    
		}    
		if (e.getSource()==mniLevel2) //��"�Ѷ�"�˵�����ѡ����"����"�˵�����   
		{    
			level=2;     
			startPoint=new Point(40,100);  //�������ͼ�����ʼ��   
			rightStartPoint=new Point(440,100); //�����ұ�ͼ�����ʼ��     
		}    
	}     
	public void itemStateChanged(ItemEvent e) //�����������¼��ķ���  
	{   
		if (e.getItemSelectable() instanceof Choice)   
		{    
			selectedImage=((Choice)e.getItemSelectable()).getSelectedItem();    
			tfx.setText(selectedImage);    
			tfy.setText(gamingImage);         
			if (selectedImage!=gamingImage)    
			{     
				GetImage();     
				startGame=false;     
				repaint();     
				}              
			} 
	}    
	public void mouseClicked(MouseEvent e) //������ͼƬ�ϵ������¼��ķ���  
	{   
		int click_X,click_Y;   
		click_X=e.getX();   
		click_Y=e.getY();    
		tfx.setText(Integer.toString((e.getY()-1-startPoint.x)/squareSize+1));     
		tfy.setText(Integer.toString((e.getX()-1-startPoint.y)/squareSize+1));     
		if(click_X>=startPoint.x&&click_X<=startPoint.x+squareNumber*squareSize&&click_Y>=startPoint.y&&click_Y<=startPoint.y+squareNumber*squareSize)   
		{    
			Move(click_X,click_Y);   
		}   
	}   
	
	public void mouseExited(MouseEvent e)  
	{      
		
	}   
	
	public void mouseEntered(MouseEvent e)  
	{      
		
	}   
	
	public void mousePressed(MouseEvent e)  
	{      
		
	}   
	
	public void mouseReleased(MouseEvent e)   
	{
	}
   
	public static void main(String[] args)   
	{   
		new MobanFrame();
	}
}