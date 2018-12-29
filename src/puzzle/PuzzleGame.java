package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.border.TitledBorder;
import java.util.Collections;
import java.util.ArrayList;

public class PuzzleGame extends JFrame
{
	public final static int TYPE = 0;// 定义游戏难度类型，可扩展
	public final static int[] level =
	{ 50, 100 }; // 设置拆分图像的大小（单位：像素）大小决定了游戏的难易度
	int len; // 拆分以后小图像的长度和宽度
	int row, col; // 拼图的行数和列数
	private JPanel centerPane; // 声明拼图所在面板
	private JPanel modelPane; // 声明显示参考图像的面板
	private BufferedImage image = null; // 要拼接的原图
	private JMenuBar menuBar; // 声明菜单栏对象
	private ImageButton emptyButton; // 声明空白按钮对象
	private String fileName = "background/bg.jpg"; // 要拼接的图片的路径

	public PuzzleGame()
	{
		super();
		setResizable(false);
		setTitle("拼图游戏");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭窗体时退出程序

		this.len = level[TYPE];
		this.image = loadImage(); // 加载原始图像
		setRowAndCol(); // 设置要拼接的行和列
		this.centerPane = this.createCenterPane(); // 创建拼接图面板
		this.getContentPane().add(centerPane, BorderLayout.CENTER);
		this.modelPane = new ModelPane(this.image); // 创建显示参考图像的面板
		this.getContentPane().add(modelPane, BorderLayout.LINE_END);
		this.menuBar = this.createMenuBar();
		this.setJMenuBar(menuBar);
		this.pack();
		this.setLocation(this.getToolkit().getScreenSize().width / 2 - this.getWidth() / 2,
				this.getToolkit().getScreenSize().height / 2 - this.getHeight() / 2);
		this.setVisible(true);
	}

	private JMenuBar createMenuBar()
	{
		JMenu[] m =
		{ new JMenu("开始(B)"), new JMenu("关于(A)") };		

		JMenuBar menuBar = new JMenuBar();		
		menuBar.add(m[0]);
		menuBar.add(m[1]);
		
		m[0].setMnemonic('B');
		m[1].setMnemonic('A');
		initMenuBegin(m);
		initMenuAbout(m);

		return menuBar;
	}

	private void initMenuAbout(JMenu[] m)
	{
		JMenuItem[] mi =
		{ new JMenuItem("游戏说明(H)"), new JMenuItem("关于作者(A)") };
		mi[0].setMnemonic('H');
		mi[1].setMnemonic('A');

		mi[0].setAccelerator(KeyStroke.getKeyStroke("F1"));
		mi[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String help0 = "移动小图片，组合成如右边所示原始图像.\n\n";
				String help1 = "操作方法：在邻近空白图片的图片上单击，将其位置与空白图片位置互换.";
				JOptionPane.showMessageDialog(null, help0 + help1);
			}
		});

		mi[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String version = "版本：1.0\n";
				String author = "作者：CityWalker";
				JOptionPane.showMessageDialog(null, version + author);
			}
		});
		m[1].add(mi[0]);
		m[1].add(mi[1]);
	}

	private void initMenuBegin(JMenu[] m)
	{
		JMenuItem[] mi =
		{ new JMenuItem("新开局(N)"), new JMenuItem("退出(E)") };
		mi[0].setMnemonic('N');
		mi[1].setMnemonic('E');
		mi[0].setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		mi[1].setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
		mi[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuNewClick();
			}
		});
		mi[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		m[0].add(mi[0]);
		m[0].add(mi[1]);
		m[0].insertSeparator(1);
	}

	private void menuNewClick()
	{
		this.getContentPane().remove(centerPane);
		this.centerPane = this.createCenterPane();
		this.getContentPane().add(this.centerPane, BorderLayout.CENTER);
		this.validate();

	}

	// 加载图像
	public BufferedImage loadImage()
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(fileName));
		} catch (Exception e)
		{
			System.out.println("加载图像异常");
		}

		return image;
	}

	// 设置拼图的行数和列数
	public void setRowAndCol()
	{
		if (this.image != null)
		{
			this.col = image.getWidth() / this.len;
			this.row = image.getHeight() / this.len;
		} else
		{
			this.row = 0;
			this.col = 0;
		}
	}

	public ArrayList<BufferedImage> dividImage(BufferedImage image)
	{
		ArrayList<BufferedImage> subimage = new ArrayList<BufferedImage>(this.row * this.col);
		for (int i = 0; i < this.row; i++)
		{
			for (int j = 0; j < this.col; j++)
			{
				subimage.add((i * this.col + j), image.getSubimage(j * len, i * len, len, len));
			}
		}

		BufferedImage firstImage = subimage.remove(0); // 移除第一个图片
		Collections.shuffle(subimage);
		subimage.add(0, firstImage);

		return subimage;
	}

	private JPanel createCenterPane()
	{
		JPanel centerPane = new JPanel();
		centerPane.setBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null)); // 为面板添加边框
		centerPane.setLayout(new GridLayout(row, col, 0, 0)); // 设置拼图面板的大小（行数和列数）
		JButton[][] imageButton = this.createImageButton(); // 创建要拼接的按钮
		this.addButton(imageButton, centerPane);

		return centerPane;
	}

	public ImageButton[][] createImageButton()
	{
		ArrayList<BufferedImage> images = this.dividImage(this.image); // 拆分原始图像，并保存到数组当中
		ImageButton[][] imageButton = new ImageButton[this.row][this.col];
		for (int i = 0; i < this.row; i++)
		{
			for (int j = 0; j < this.col; j++)
			{
				imageButton[i][j] = new ImageButton(new ImageIcon(images.get(i * this.col + j)));
				imageButton[i][j].setRow(i);
				imageButton[i][j].setCol(j);
				imageButton[i][j].setPreferredSize(new Dimension(len, len)); // 设置按钮的首选项大小
				imageButton[i][j].addActionListener(new ImageButtonAction());
			}
		}
		imageButton[0][0].setIcon(null); // 将第一个按钮设置为不带图像的按钮
		emptyButton = imageButton[0][0]; // 将空白按钮对象指向不带图像的按钮

		return imageButton;
	}

	/**
	 * 向组件中添加按钮的方法
	 * 
	 * @param imageButton
	 * @param centerPane
	 */
	public void addButton(JButton[][] imageButton, JPanel centerPane)
	{
		for (int i = 0; i < this.row; i++)
		{
			for (int j = 0; j < this.col; j++)
			{
				centerPane.add(imageButton[i][j]);
			}
		}
	}

	class ImageButtonAction implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			ImageButton clickButton = (ImageButton) e.getSource();
			int clickRow = clickButton.getRow();
			int clickCol = clickButton.getCol();
			int emptyRow = emptyButton.getRow();
			int emptyCol = emptyButton.getCol();
			// 判断被单击按钮与空白按钮是否相邻
			if (Math.abs(clickRow - emptyRow) + Math.abs(clickCol - emptyCol) == 1)
			{
				emptyButton.setIcon(clickButton.getIcon());
				clickButton.setIcon(null);
				emptyButton = clickButton;
			}

		}

	}

	public static void main(String[] args)
	{
		PuzzleGame game = new PuzzleGame();		
	}

}
