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
	public final static int TYPE = 0;// ������Ϸ�Ѷ����ͣ�����չ
	public final static int[] level =
	{ 50, 100 }; // ���ò��ͼ��Ĵ�С����λ�����أ���С��������Ϸ�����׶�
	int len; // ����Ժ�Сͼ��ĳ��ȺͿ��
	int row, col; // ƴͼ������������
	private JPanel centerPane; // ����ƴͼ�������
	private JPanel modelPane; // ������ʾ�ο�ͼ������
	private BufferedImage image = null; // Ҫƴ�ӵ�ԭͼ
	private JMenuBar menuBar; // �����˵�������
	private ImageButton emptyButton; // �����հװ�ť����
	private String fileName = "background/bg.jpg"; // Ҫƴ�ӵ�ͼƬ��·��

	public PuzzleGame()
	{
		super();
		setResizable(false);
		setTitle("ƴͼ��Ϸ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ùرմ���ʱ�˳�����

		this.len = level[TYPE];
		this.image = loadImage(); // ����ԭʼͼ��
		setRowAndCol(); // ����Ҫƴ�ӵ��к���
		this.centerPane = this.createCenterPane(); // ����ƴ��ͼ���
		this.getContentPane().add(centerPane, BorderLayout.CENTER);
		this.modelPane = new ModelPane(this.image); // ������ʾ�ο�ͼ������
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
		{ new JMenu("��ʼ(B)"), new JMenu("����(A)") };		

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
		{ new JMenuItem("��Ϸ˵��(H)"), new JMenuItem("��������(A)") };
		mi[0].setMnemonic('H');
		mi[1].setMnemonic('A');

		mi[0].setAccelerator(KeyStroke.getKeyStroke("F1"));
		mi[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String help0 = "�ƶ�СͼƬ����ϳ����ұ���ʾԭʼͼ��.\n\n";
				String help1 = "�������������ڽ��հ�ͼƬ��ͼƬ�ϵ���������λ����հ�ͼƬλ�û���.";
				JOptionPane.showMessageDialog(null, help0 + help1);
			}
		});

		mi[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String version = "�汾��1.0\n";
				String author = "���ߣ�CityWalker";
				JOptionPane.showMessageDialog(null, version + author);
			}
		});
		m[1].add(mi[0]);
		m[1].add(mi[1]);
	}

	private void initMenuBegin(JMenu[] m)
	{
		JMenuItem[] mi =
		{ new JMenuItem("�¿���(N)"), new JMenuItem("�˳�(E)") };
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

	// ����ͼ��
	public BufferedImage loadImage()
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(fileName));
		} catch (Exception e)
		{
			System.out.println("����ͼ���쳣");
		}

		return image;
	}

	// ����ƴͼ������������
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

		BufferedImage firstImage = subimage.remove(0); // �Ƴ���һ��ͼƬ
		Collections.shuffle(subimage);
		subimage.add(0, firstImage);

		return subimage;
	}

	private JPanel createCenterPane()
	{
		JPanel centerPane = new JPanel();
		centerPane.setBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, null)); // Ϊ�����ӱ߿�
		centerPane.setLayout(new GridLayout(row, col, 0, 0)); // ����ƴͼ���Ĵ�С��������������
		JButton[][] imageButton = this.createImageButton(); // ����Ҫƴ�ӵİ�ť
		this.addButton(imageButton, centerPane);

		return centerPane;
	}

	public ImageButton[][] createImageButton()
	{
		ArrayList<BufferedImage> images = this.dividImage(this.image); // ���ԭʼͼ�񣬲����浽���鵱��
		ImageButton[][] imageButton = new ImageButton[this.row][this.col];
		for (int i = 0; i < this.row; i++)
		{
			for (int j = 0; j < this.col; j++)
			{
				imageButton[i][j] = new ImageButton(new ImageIcon(images.get(i * this.col + j)));
				imageButton[i][j].setRow(i);
				imageButton[i][j].setCol(j);
				imageButton[i][j].setPreferredSize(new Dimension(len, len)); // ���ð�ť����ѡ���С
				imageButton[i][j].addActionListener(new ImageButtonAction());
			}
		}
		imageButton[0][0].setIcon(null); // ����һ����ť����Ϊ����ͼ��İ�ť
		emptyButton = imageButton[0][0]; // ���հװ�ť����ָ�򲻴�ͼ��İ�ť

		return imageButton;
	}

	/**
	 * ���������Ӱ�ť�ķ���
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
			// �жϱ�������ť��հװ�ť�Ƿ�����
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
