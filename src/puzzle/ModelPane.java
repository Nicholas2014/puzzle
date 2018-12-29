package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.border.TitledBorder;

public class ModelPane extends JPanel
{
	AffineTransform transform = new AffineTransform(); // ����һ������任������
	private double scaleX = 0.5; // ������X���ϵ����ű���
	private double scaleY = 0.5; // ������Y���ϵ����ű���
	private BufferedImage destImage; // ����Դͼ��������Ժ��ͼ��

	public ModelPane(BufferedImage srcImage)
	{
		super();
		this.destImage = filterSrcImage(srcImage); // ��ԭʼͼ��������ţ������������Ժ��ͼ��
		JLabel model = new JLabel(new ImageIcon(destImage));
		JLabel infoLabel = new JLabel("ԭʼͼ��", SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
				null, null));
		this.add(infoLabel, BorderLayout.PAGE_START);
		this.add(model, BorderLayout.CENTER);
	}

	/**
	 * ��ָ����ͼ���������
	 * 
	 * @param srcImage
	 * @return
	 */
	public BufferedImage filterSrcImage(BufferedImage srcImage)
	{
		BufferedImage image;
		AffineTransform transform = new AffineTransform(); // ����һ������任������
		double scaleX = 0.5; // ������X���ϵ����ű���
		double scaleY = 0.5; // ������Y���ϵ����ű���
		transform.setToScale(scaleX, scaleY);
		image = new BufferedImage(srcImage.getWidth(this) / 2, srcImage.getHeight(this) / 2,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = image.createGraphics(); // ��ȡimage����Ļ�ͼ�����Ļ���
		g2d.clearRect(0, 0, image.getWidth(this), image.getHeight(this)); // ���image������ͼ���е�����
		AffineTransformOp ato = new AffineTransformOp(transform, null); // ����һ������任������
		ato.filter(srcImage, image); // ���ù���������Դͼ�񣬲����õ�����Ĵ����ͼ�����ݴ���image��

		return image;
	}

}
