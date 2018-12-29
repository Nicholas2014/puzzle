package puzzle;

import javax.swing.JButton;
import javax.swing.ImageIcon;

public class ImageButton extends JButton
{
	private int row = 0;
	public int getRow()
	{
		return row;
	}
	public void setRow(int row)
	{
		this.row = row;
	}
	public int getCol()
	{
		return col;
	}
	public void setCol(int col)
	{
		this.col = col;
	}
	private int col = 0;
	
	public ImageButton()
	{
		
	}
	public ImageButton(ImageIcon image)
	{
		super(image);
	}
	
	
}
