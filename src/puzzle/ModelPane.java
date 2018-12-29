package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.border.TitledBorder;

public class ModelPane extends JPanel
{
	AffineTransform transform = new AffineTransform(); // 声明一个仿射变换过滤器
	private double scaleX = 0.5; // 声明在X轴上的缩放比例
	private double scaleY = 0.5; // 声明在Y轴上的缩放比例
	private BufferedImage destImage; // 声明源图像和缩放以后的图像

	public ModelPane(BufferedImage srcImage)
	{
		super();
		this.destImage = filterSrcImage(srcImage); // 对原始图像进行缩放，并返回缩放以后的图像
		JLabel model = new JLabel(new ImageIcon(destImage));
		JLabel infoLabel = new JLabel("原始图像", SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
				null, null));
		this.add(infoLabel, BorderLayout.PAGE_START);
		this.add(model, BorderLayout.CENTER);
	}

	/**
	 * 对指定的图像进行缩放
	 * 
	 * @param srcImage
	 * @return
	 */
	public BufferedImage filterSrcImage(BufferedImage srcImage)
	{
		BufferedImage image;
		AffineTransform transform = new AffineTransform(); // 声明一个仿射变换过滤器
		double scaleX = 0.5; // 声明在X轴上的缩放比例
		double scaleY = 0.5; // 声明在Y轴上的缩放比例
		transform.setToScale(scaleX, scaleY);
		image = new BufferedImage(srcImage.getWidth(this) / 2, srcImage.getHeight(this) / 2,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = image.createGraphics(); // 获取image对象的绘图上下文环境
		g2d.clearRect(0, 0, image.getWidth(this), image.getHeight(this)); // 清空image缓冲区图像中的数据
		AffineTransformOp ato = new AffineTransformOp(transform, null); // 创建一个放射变换过滤器
		ato.filter(srcImage, image); // 利用过滤器过滤源图像，并将得到解决的处理后图像数据存入image中

		return image;
	}

}
