package com.androdome.plugin.paintdotjar.basictools;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.androdome.util.paintdotjar.plugin.JavaPlugin;
import com.androdome.util.paintdotjar.plugin.tool.Tool;
import com.androdome.util.paintdotjar.ui.CanvasManager;

public class SelectionTools {
	public static void init(JavaPlugin plugin) throws IOException
	{
		plugin.getManager().registerTool(new BoxSelectionTool(plugin), plugin, ImageIO.read(plugin.getClass().getResourceAsStream("/testicon.png")), "Box selection tool");
	}
}

abstract class SelectionTool extends Tool
{

	public SelectionTool(JavaPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}
	
}

class BoxSelectionTool extends SelectionTool
{

	public BoxSelectionTool(JavaPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	public void onSelect(CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onDeselect(CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseMove(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseDrag(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseClick(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMousePress(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseRelease(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseEnter(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseExit(MouseEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyType(KeyEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyPress(KeyEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyRelease(KeyEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onMouseWheelMove(MouseWheelEvent e, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}

	public void onCanvasPaint(Graphics2D g2d, Rectangle innerFrame, CanvasManager manager) {
		// TODO Auto-generated method stub
		
	}
	
}