package com.androdome.plugin.paintdotjar.basictools;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.androdome.util.paintdotjar.Canvas;
import com.androdome.util.paintdotjar.plugin.JavaPlugin;
import com.androdome.util.paintdotjar.plugin.tool.ToolAdapter;
import com.androdome.util.paintdotjar.ui.CanvasManager;



public class BasicTools extends JavaPlugin {

	public void init() {
		try {
			System.out.println("Basic tools loaded");
			this.getManager().registerTool(new PencilTool(this), this, ImageIO.read(this.getClass().getResourceAsStream("/testicon.png")), "Pencil tool");
			this.getManager().registerTool(new BrushTool(this), this, ImageIO.read(this.getClass().getResourceAsStream("/paintbrush.png")), "Paintbrush tool");			
			this.getManager().registerTool(new EraserTool(this), this, ImageIO.read(this.getClass().getResourceAsStream("/eraser.png")), "Eraser tool");
			this.getManager().registerTool(new DropperTool(this), this, ImageIO.read(this.getClass().getResourceAsStream("/dropper.png")), "Dropper tool");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}

class DropperTool extends ToolAdapter
{

	public DropperTool(JavaPlugin plugin) {
		super(plugin);
	}
	

	public void onMousePress(MouseEvent e, CanvasManager comp) {
		Point point = comp.getLocationOnGraphics(new Point(e.getX(), e.getY()));
		Color c = new Color(comp.getSelectedCanvas().getImage().getRGB(point.x, point.y), true);
		if(e.getButton() == MouseEvent.BUTTON1)
			comp.setPrimary(c);
		else if(e.getButton() == MouseEvent.BUTTON3)
			comp.setSecondary(c);
	}
}

class EraserTool extends ToolAdapter
{
	private static final int REPAINT_TIME = 30;
	long lastDraw = System.currentTimeMillis();
	Point porg = new Point(-1,0);
	Point mpoint = new Point(0,0);
	private int brushSize = 1;
	private boolean antialias = false;
	public EraserTool(JavaPlugin plugin) {
		super(plugin);
	}
	
	public void onSelect(CanvasManager manager)
	{
		System.out.println("Selected!");
		final JSlider brushSlider = new JSlider();
		final JTextField text = new JTextField();
		final JCheckBox antiAlias = new JCheckBox("Anti-Aliasing");
		antiAlias.setSelected(antialias);
		brushSlider.setMaximum(256);
		brushSlider.setValue(brushSize );
		brushSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				brushSize = brushSlider.getValue();
				text.setText(brushSize + "");
			}
		});
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try
					{
						brushSize = Integer.parseInt(text.getText().trim());
						if(brushSize < 1)
							brushSize = 1;
					}
					catch(Exception ex){}
					text.setText(brushSize + "");
				}
			}		
		});
		antiAlias.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				antialias = antiAlias.isSelected();
			}
			
		});
		
		
		JLabel label = new JLabel("Eraser Size:");
		text.setColumns(4);
		text.setText(brushSize + "");
		
		this.getToolbar().add(label);
		this.getToolbar().add(brushSlider);
		this.getToolbar().add(text);
		this.getToolbar().add(antiAlias);
		this.getToolbar().revalidate();
	}

	public void onMouseMove(MouseEvent e, CanvasManager comp) {
		mpoint.x = e.getX(); mpoint.y = e.getY();
		if(porg.x > -1)
		{
			Graphics2D g = (Graphics2D)comp.getSelectedCanvas().getGraphics();
			if(antialias)
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setComposite(AlphaComposite.Clear);
			g.setColor(new Color(0,0,0,0));
			Point loc1 = comp.getLocationOnGraphics(porg);
			porg.x = e.getX(); porg.y = e.getY();
			Point loc2 = comp.getLocationOnGraphics(porg);
			//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
			g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
			//comp.repaint();
		}
		if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
		{
			lastDraw = System.currentTimeMillis();
			comp.repaint();
		}
	}

	public void onMouseDrag(MouseEvent e, CanvasManager comp) {
		mpoint.x = e.getX(); mpoint.y = e.getY();
		Graphics2D g = (Graphics2D)comp.getSelectedCanvas().getGraphics();
		if(antialias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(new Color(0,0,0,0));
		g.setComposite(AlphaComposite.Clear);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
		{
			lastDraw = System.currentTimeMillis();
			comp.repaint();
		}
	}

	public void onMousePress(MouseEvent e, CanvasManager comp) {
		porg.x = e.getX(); porg.y = e.getY();
	}
	
	public void onMouseExit(MouseEvent e, CanvasManager comp)
	{
		mpoint.x = -1;
		comp.repaint();
	}

	public void onMouseRelease(MouseEvent e, CanvasManager comp) {
		Graphics2D g = (Graphics2D)comp.getSelectedCanvas().getGraphics();
		if(antialias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(new Color(0,0,0,0));
		g.setComposite(AlphaComposite.Clear);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		porg.x = -1;
		comp.repaint();	
	}
	public void onCanvasPaint(Graphics2D g, Rectangle innerFrame, CanvasManager comp){
		g.setClip(innerFrame);
		int brushSize=  (int) (this.brushSize*comp.getScale());
		if(mpoint.x > -1)
		{

			g.setColor(Color.BLACK);
			g.drawArc((mpoint.x-brushSize/2)-1, (mpoint.y-brushSize/2)-1, brushSize, brushSize, 0, 360);
			g.setColor(Color.WHITE);
			g.drawArc(mpoint.x-(brushSize)/2, mpoint.y-(brushSize)/2, brushSize-2, brushSize-2, 0, 360);
		}
	}
}


class BrushTool extends ToolAdapter
{
	private static final int REPAINT_TIME = 30;
	long lastDraw = System.currentTimeMillis();
	private Canvas tempCanvas = null;
	private boolean antialias = false;
	private Point porg = new Point(-1,0);
	private Color selectedColor = null;
	private int brushSize = 1;
	public BrushTool(JavaPlugin plugin) {
		super(plugin);
	}
	//This will only be for the brush tool!
	public void onSelect(CanvasManager manager)
	{
		System.out.println("Selected!");
		final JSlider brushSlider = new JSlider();
		final JTextField text = new JTextField();
		final JCheckBox antiAlias = new JCheckBox("Anti-Aliasing");
		antiAlias.setSelected(antialias);
		brushSlider.setMaximum(256);
		brushSlider.setValue(brushSize);
		brushSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				brushSize = brushSlider.getValue();
				text.setText(brushSize + "");
			}
		});
		text.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try
					{
						brushSize = Integer.parseInt(text.getText().trim());
						if(brushSize < 1)
							brushSize = 1;
					}
					catch(Exception ex){}
					text.setText(brushSize + "");
				}
			}		
		});
		antiAlias.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				antialias = antiAlias.isSelected();
			}
			
		});
		JLabel label = new JLabel("Brush Size:");
		text.setColumns(4);
		text.setText(brushSize + "");
		this.getToolbar().add(label);
		this.getToolbar().add(brushSlider);
		this.getToolbar().add(text);
		this.getToolbar().add(antiAlias);
		this.getToolbar().revalidate();
	}

	public void onMouseMove(MouseEvent e, CanvasManager comp) {
		if(porg.x > -1)
		{
			Graphics2D g = (Graphics2D) tempCanvas.getGraphics();
			if(antialias)
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setColor(selectedColor);
			Point loc1 = comp.getLocationOnGraphics(porg);
			porg.x = e.getX(); porg.y = e.getY();
			Point loc2 = comp.getLocationOnGraphics(porg);
			//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
			g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
			if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
			{
				lastDraw = System.currentTimeMillis();
				comp.repaint();
			}
		}
	}

	public void onMouseDrag(MouseEvent e, CanvasManager comp) {
		Graphics2D g = (Graphics2D)tempCanvas.getGraphics();
		if(antialias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(selectedColor);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
		{
			lastDraw = System.currentTimeMillis();
			comp.repaint();
		}
	}

	public void onMousePress(MouseEvent e, CanvasManager comp) {
		System.out.println("Painting!");
		porg.x = e.getX(); porg.y = e.getY();
		tempCanvas = comp.getTemporaryCanvas(comp.getSelectedIndex());
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			selectedColor = colorMinusAlpha(comp.getPrimary());
			tempCanvas.setOpacity(comp.getPrimary().getAlpha());
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			selectedColor = colorMinusAlpha(comp.getPrimary());
			tempCanvas.setOpacity(comp.getPrimary().getAlpha());
		}
	}

	private Color colorMinusAlpha(Color color) {
		// TODO Auto-generated method stub
		return new Color(color.getRed(),color.getGreen(),color.getBlue());
	}

	public void onMouseRelease(MouseEvent e, CanvasManager comp) {
		Graphics2D g = (Graphics2D)tempCanvas.getGraphics();
		if(antialias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(this.brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(selectedColor);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		porg.x = -1;
		comp.applyTemporaryCanvas();
		tempCanvas = null;
		selectedColor = null;
		comp.repaint();	
	}
}


class PencilTool extends ToolAdapter
{
	Canvas tempCanvas = null;
	Point porg = new Point(-1,0);
	Color selectedColor = null;
	public static final int REPAINT_TIME = 30;
	private long lastDraw = System.currentTimeMillis();
	public PencilTool(JavaPlugin plugin) {
		super(plugin);
	}

	public void onMouseMove(MouseEvent e, CanvasManager comp) {
		if(porg.x > -1)
		{
			Graphics g = tempCanvas.getGraphics();
			g.setColor(selectedColor);
			Point loc1 = comp.getLocationOnGraphics(porg);
			porg.x = e.getX(); porg.y = e.getY();
			Point loc2 = comp.getLocationOnGraphics(porg);
			//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
			g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
			if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
			{
				lastDraw = System.currentTimeMillis();
				comp.repaint();
			}
		}
	}

	public void onMouseDrag(MouseEvent e, CanvasManager comp) {
		Graphics g = tempCanvas.getGraphics();
		g.setColor(selectedColor);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		if(lastDraw < (System.currentTimeMillis()-REPAINT_TIME))
		{
			lastDraw = System.currentTimeMillis();
			comp.repaint();
		}
	}

	public void onMousePress(MouseEvent e, CanvasManager comp) {
		porg.x = e.getX(); porg.y = e.getY();
		tempCanvas = comp.getTemporaryCanvas(comp.getSelectedIndex());
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			selectedColor = colorMinusAlpha(comp.getPrimary());
			tempCanvas.setOpacity(comp.getPrimary().getAlpha());
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			selectedColor = colorMinusAlpha(comp.getPrimary());
			tempCanvas.setOpacity(comp.getPrimary().getAlpha());
		}
	}

	private Color colorMinusAlpha(Color color) {
		// TODO Auto-generated method stub
		return new Color(color.getRed(),color.getGreen(),color.getBlue());
	}

	public void onMouseRelease(MouseEvent e, CanvasManager comp) {
		Graphics g = tempCanvas.getGraphics();
		g.setColor(selectedColor);
		Point loc1 = comp.getLocationOnGraphics(porg);
		porg.x = e.getX(); porg.y = e.getY();
		Point loc2 = comp.getLocationOnGraphics(porg);
		//System.out.println("Drawing line from" + loc1.x+ ", " + loc1.y + " to " + loc2.x+ ", " + loc2.y);
		g.drawLine(loc1.x, loc1.y, loc2.x, loc2.y);
		porg.x = -1;
		comp.applyTemporaryCanvas();
		tempCanvas = null;
		selectedColor = null;
		if(lastDraw < (System.currentTimeMillis()-40))
		{
			lastDraw  = System.currentTimeMillis();
			comp.repaint();
		}	
	}
}