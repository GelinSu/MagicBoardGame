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
	MobanSquare[][] position;  //声明方块   
	Point startPoint=new Point(110,120);  //绘制左边图像的起始点  
	Point rightStartPoint=new Point(450,120); //绘制右边图像的起始点  
	int[] num;  //存储随机数的数组  
	Point[] pointMove;    
	int totalStep=0;  //总共已走的步数    
	String selectedImage="金牛座";  //当前选中的要游戏图像   
	String gamingImage="                               ";  //正在游戏的图像  
	boolean startGame=false;    
	int squareNumber=3;  
	int level=1;  //游戏等级  
	int squareSize=80;     
	Image sourceImage; //游戏图像的来源图像 
	Image spaceSourceImage; //空格图像的来源图像  
	Image spaceImage;   
	Image[] myImage; //存储打乱的图像的数组  Graphics2D       
	/*--------------------*/ 
	/*显示在界面上的一些组件*/ 
	/*--------------------*/  
	TextField tfx;  
	TextField tfy;   
	MenuBar mnbMyMenuBar;   
	Menu mnChooseLevel;    
	MenuItem mniLevel1, mniLevel2;  
	Label lbStep; //显示所走的步数  
	Label lbSuccess=new Label("");  
	Button btnStartGame;  
	Choice chChoiceImage;   
	
	public MobanFrame()  {   
		super("魔板游戏");    
		pointMove=new Point[4];  //方块可以移动的四个方向  
		pointMove[0]=new Point(-1,0);   
		pointMove[1]=new Point(1,0);   
		pointMove[2]=new Point(0,-1);   
		pointMove[3]=new Point(0,1);   
		setLayout(new FlowLayout());   
		chChoiceImage=new Choice();   
		chChoiceImage.add("金牛座");   
		chChoiceImage.add("迈克尔乔丹");   
		chChoiceImage.add("多啦a梦");   
		chChoiceImage.add("射手座");     
		chChoiceImage.add("功夫熊猫");
		chChoiceImage.add("蜡笔小新");   
		chChoiceImage.add("柯南");   
		chChoiceImage.add("小狗");   
		chChoiceImage.add("小狗2");   
		chChoiceImage.add("小猫");   
		chChoiceImage.add("Baby");   
		chChoiceImage.add("数字");   
		mniLevel1=new MenuItem("简单");   
		mniLevel2=new MenuItem("困难");   
		mnChooseLevel=new Menu("难度");   
		mnbMyMenuBar=new MenuBar();         
		tfx=new TextField(8);          
		tfy=new TextField(8);   
		tfx.setText("0");   
		tfy.setText("1");   
		tfx.setVisible(false);   
		tfy.setVisible(false);    
		lbStep=new Label("已走步数：  "+Integer.toString(totalStep));   
		btnStartGame=new Button("开始游戏");   
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
	public void GetImage()  //获取要完成的拼图，并将拼图打乱  
	{   
		myImage=new Image[squareNumber*squareNumber];   
		ImageFilter cropFilter1;    
		ImageFilter cropFilter2;    
		ImageFilter cropFilter3;   
		Toolkit tool=getToolkit();
		String filepath="src";		//获取当前目录的路径
		File file=new File(filepath);
		sourceImage=tool.createImage("src/"+selectedImage+".jpg");      
		spaceSourceImage=tool.createImage("src/space.jpg");    
		sourceImage=sourceImage.getScaledInstance(squareSize*squareNumber,squareSize*squareNumber,Image.SCALE_DEFAULT); //得到一个指定大小的图像
		cropFilter1 =new CropImageFilter(0,0,squareSize*squareNumber,squareSize*squareNumber);//四个参数分别为图像起点坐标和宽高，即CropImageFilter(int x,int y,int width,int height)，    
		sourceImage=createImage(new  FilteredImageSource(sourceImage.getSource(),cropFilter1));         
		for(int i=0;i<squareNumber*squareNumber;i++) //从源图像上按选择的游戏难度截取相应个数的小图像块     
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
	
	public void paint(Graphics g)  //绘制拼图  
	{   
		boolean showSuccess=true;    
		lbStep.setText("已走步数：  "+Integer.toString(totalStep));     
		if (startGame) 
		{    
			for (int i=1;i<squareNumber+1;i++)     
			{	      
				for (int j=1;j<squareNumber+1;j++)           
				{        
					g.drawImage(position[i][j].GetImage(),startPoint.x+(position[i][j].y-1)*squareSize,startPoint.y+(position[i][j].x-1)*squareSize,this); //绘制左边打乱的方块     
				}      
			}       
			g.setColor(Color.cyan);     
			for (int i=0;i<squareNumber+1;i++)       
			{        
				g.drawLine(startPoint.x+squareSize*i,startPoint.y,startPoint.x+squareSize*i,startPoint.y+squareNumber*squareSize);        
				g.drawLine(startPoint.x,startPoint.y+squareSize*i,startPoint.x+squareNumber*squareSize,startPoint.y+squareSize*i);    
			}                 
		}   
		g.drawImage(sourceImage,rightStartPoint.x,rightStartPoint.y,this);     //绘制右边参照图     //
		g.drawImage(spaceImage,rightStartPoint.x+(squareNumber-1)*squareSize,rightStartPoint.y+(squareNumber-1)*squareSize,this);   
		for (int i=1;i<squareNumber+1;i++)  //判断用户是否完成拼图    
		{     
			for (int j=1;j<squareNumber+1;j++)     
			{          
				if (position[i][j].GetOrder()!=(i-1)*squareNumber+(j-1))
				{       
					showSuccess=false;      
				}      
			}      
		}    
		if(showSuccess)//用户完成拼图，弹出完成对话框   
		{     
			showSuccess=false; 
			System.out.println();
			lbSuccess.setText("\n恭喜您，成功完成");    
		}   
	}   
	
	public void GetRandom() //生成随机数,并将随机图像赋给每个方块  
	{     
		int k=squareNumber*squareNumber;  
		boolean numExist=false;   
		int total=0;     
		for (int j=0;j<squareNumber*squareNumber;j++)       
		{    
			num[j]=-1;      
			}    
		while(total<squareNumber*squareNumber)           //生成与方块个数相同的随机数(0..方块个数-1)   
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
				if(i==0||i==squareNumber+1||j==0||j==squareNumber+1)  //实例化外围方块,这些方块是不可见的         
					{       
					this.position[i][j]=new  MobanSquare(i,j,-1,squareNumber*squareNumber-1);       
					}        
				}     
			}    
		for (int i=1;i<squareNumber+1;i++)       //实例化要显示给用户的方块，此些方块可见，并可以移动   
			{    
			for (int j=1;j<squareNumber+1;j++)    
			{         
				this.position[i][j]=new MobanSquare(i,j,num[(i-1)*squareNumber+j-1],squareNumber*squareNumber-1);      
				this.position[i][j].SetImage(myImage[this.position[i][j].GetOrder()]);    
				}    
			}   
		}       
	public void Move(int x,int y)  //移动方块  
	{   
		int square_X,square_Y,order_Old,order_New;    
		square_X=(y-1-startPoint.y)/squareSize+1;    
		square_Y=(x-1-startPoint.x)/squareSize+1;    
		for (int i=0;i<4;i++)     
		{     
			if  (position[square_X+pointMove[i].x][square_Y+pointMove[i].y].arrive) //当前方块可以移动，即空白块是它的邻块    
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
		if (e.getSource()==btnStartGame)  //点击"开始游戏按钮"   
		{    
			lbSuccess.setText("                               ");    
			totalStep=0;     
			if (level==1)         //难度为简单    
				{        
				squareNumber=3;      
				}     
			if (level==2)           //难度为困难    
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
		if (e.getSource()==mniLevel1) //在"难度"菜单项里选择了"简单"菜单子项     
		{ 
			level=1;     
			startPoint=new Point(110,120);  //绘制左边图像的起始点    
			rightStartPoint=new Point(450,120); //绘制右边图像的起始点    
		}    
		if (e.getSource()==mniLevel2) //在"难度"菜单项里选择了"困难"菜单子项   
		{    
			level=2;     
			startPoint=new Point(40,100);  //绘制左边图像的起始点   
			rightStartPoint=new Point(440,100); //绘制右边图像的起始点     
		}    
	}     
	public void itemStateChanged(ItemEvent e) //处理下拉框事件的方法  
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
	public void mouseClicked(MouseEvent e) //处理在图片上点击鼠标事件的方法  
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