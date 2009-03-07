package Hanoi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import Paint.Shapes.ShapeIF;

public class TowersUI {

	private static final int DEFULT_WAIT_TIME = 1000;

	private static TowersUI instance;

	private ArrayList<ShapeIF> shapes;
	private Display display;
	private Shell shell;
	private ArrayList<Tower> towers;
	private MoveTool tool;
	private Canvas canvas;
	private Font font;
	private long startTime = 0;
	private Runnable updateTimer;
	private Composite currentComposite;
	private Cursor handCursor;
	private Cursor defaultCursor;
	private boolean solution = false;

	private long waitTime = DEFULT_WAIT_TIME;

	public static synchronized TowersUI getInstance() {
		if (instance == null)
			return instance = new TowersUI();
		else
			return instance;
	}

	private TowersUI() {
		updateTimer = new Runnable(){
			@Override
			public void run() {
				Label timer = (Label) shell.getData("timer");
				long time = System.currentTimeMillis();
				int diff = (int) ((time - startTime) / 1000);
				int min = diff / 60; diff = diff % 60;
				int sec = diff;
				String minStr = min < 10 ? "0"+min : min+"";
				String secStr = sec < 10 ? "0"+sec : sec+"";
				timer.setText(minStr + ":" + secStr);
				display.timerExec(1000, this);
			}};
		shapes = new ArrayList<ShapeIF>();
		towers = new ArrayList<Tower>(3);
		tool = MoveTool.getInstance();
	}

	public void run() {
		display = new Display();
		shell = new Shell(SWT.NO_TRIM);
		font = new Font(display, "Comic Sans MS", 14, SWT.BOLD);
		shell.setText("Towers of Hanoi");
		createContents();
		shell.setMaximized(true);
		shell.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
				display.dispose();
				System.exit(0);
			}});
		shell.open();
		while (! shell.isDisposed())
			if (! display.readAndDispatch())
				display.sleep();
	}

	private void createContents() {
		handCursor = display.getSystemCursor(SWT.CURSOR_HAND);
		defaultCursor = display.getSystemCursor(SWT.CURSOR_ARROW);
		shell.setLayout(new FillLayout());
		customGame();
	}

	private void customGame() {
		currentComposite = new Composite(shell, SWT.NONE);
		currentComposite.setLayout(new FormLayout());
		canvas = new Canvas(currentComposite, SWT.DOUBLE_BUFFERED);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100, -400);
		formData.top = new FormAttachment(0, 50);
		formData.bottom = new FormAttachment(100, -55);
		canvas.setLayoutData(formData);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		canvas.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				Iterator<ShapeIF> iter = shapes.iterator();
				while (iter.hasNext())
					iter.next().draw(e.gc);
				Iterator<Tower> iterator = towers.iterator();
				while (iterator.hasNext())
					iterator.next().draw(e.gc);
				tool.drawShape(e.gc, null);
			}});
		createShapes();
		canvas.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				tool.mouseDoubleClick(e);
			}
			@Override
			public void mouseDown(MouseEvent e) {
				if (startTime == 0 && ! solution ) {
					startTime = System.currentTimeMillis();
					display.timerExec(1000, updateTimer);
				}
				tool.mouseDown(e);
			}
			@Override
			public void mouseUp(MouseEvent e) {
				tool.mouseUp(e);
			}});
		canvas.addMouseMoveListener(new MouseMoveListener(){
			@Override
			public void mouseMove(MouseEvent e) {
				tool.mouseMove(e);
			}});
		Label seperator = new Label(currentComposite, SWT.SEPARATOR);
		formData = new FormData();
		formData.left = new FormAttachment(canvas, 5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		seperator.setLayoutData(formData);

		Composite controls = new Composite(currentComposite, SWT.NONE);
		formData = new FormData();
		formData.left = new FormAttachment(seperator, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		controls.setLayoutData(formData);
		createControls(controls);

		CLabel title = new CLabel(currentComposite, SWT.NONE);
		title.setFont(font);
		title.setText("Towers of Hanoi");
		title.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_GRAY),
				display.getSystemColor(SWT.COLOR_GRAY), display.getSystemColor(SWT.COLOR_WHITE)}, new int[]{50, 100});
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(0, 5);
		formData.right = new FormAttachment(controls, -5);
		formData.bottom = new FormAttachment(canvas, -5);
		title.setLayoutData(formData);

		CLabel moves = new CLabel(currentComposite, SWT.NONE);
		moves.setFont(font);
		moves.setText("   moves done: 0");
		moves.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_GRAY),
				display.getSystemColor(SWT.COLOR_GRAY), display.getSystemColor(SWT.COLOR_WHITE)}, new int[]{50, 100});
		formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.top = new FormAttachment(canvas, 5);
		formData.right = new FormAttachment(controls, -5);
		formData.bottom = new FormAttachment(100, -5);
		moves.setLayoutData(formData);
		shell.setData("moves", moves);
	}

	private void createControls(Composite parent) {
		parent.setLayout(new GridLayout());
		new Label(parent, SWT.NONE).setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite controls = new Composite(parent, SWT.NONE);
		controls.setLayoutData(new GridData(GridData.FILL_BOTH));
		controls.setLayout(new GridLayout(1, true));
		Composite c1 = new Composite(controls, SWT.NONE);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(3, true));

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		final Button negative = new Button(c1, SWT.PUSH);
		negative.setText("-");
		negative.setFont(font);
		negative.setLayoutData(data);
		final Label diskNumber = new Label(c1, SWT.PUSH);
		diskNumber.setAlignment(SWT.CENTER);
		diskNumber.setFont(font);
		diskNumber.setLayoutData(data);
		diskNumber.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
		diskNumber.setText("3");
		final Button positive = new Button(c1, SWT.PUSH);
		positive.setFont(font);
		positive.setText("+");
		positive.setLayoutData(data);
		negative.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				int disks = Integer.parseInt(diskNumber.getText());
				if (disks > 3) {
					diskNumber.setText(--disks + "");
					CLabel c = (CLabel) shell.getData("moves");
					c.setText("   moves done: 0");
				}
				changeDisksNum(disks);
				refresh();
					
			}
		});
		positive.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				int disks = Integer.parseInt(diskNumber.getText());
				if (disks < 12) {
					diskNumber.setText(++disks + "");
					CLabel c = (CLabel) shell.getData("moves");
					c.setText("   moves done: 0");
				}
				changeDisksNum(disks);
				refresh();
			}
		});
		final Button restart = new Button(controls, SWT.PUSH);
		restart.setFont(font);
		restart.setAlignment(SWT.CENTER);
		restart.setText("Restart");
		restart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		restart.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				changeDisksNum(Integer.parseInt(diskNumber.getText()));
				CLabel c = (CLabel) shell.getData("moves");
				c.setText("   moves done: 0");
				refresh();
			}
		});
		final Label timer = new Label(controls, SWT.NONE);
		timer.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		timer.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		timer.setText(" 00:00 ");
		timer.setFont(font);
		timer.setAlignment(SWT.CENTER);
		timer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		shell.setData("timer", timer);
		Button solution = new Button(controls, SWT.PUSH);
		solution.setFont(font);
		solution.setAlignment(SWT.CENTER);
		solution.setText("Solution");
		solution.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		solution.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				changeDisksNum(Integer.parseInt(diskNumber.getText()));
				CLabel c = (CLabel) shell.getData("moves");
				c.setText("   moves done: 0");
				refresh();
				restart.setEnabled(false);
				positive.setEnabled(false);
				negative.setEnabled(false);
				TowersUI.this.solution = true;
				timer.setText("00:00");
				display.timerExec(-1, updateTimer);
				Thread t = new Thread(new Runnable(){
					private int diskNum;
					@Override
					public void run() {
						display.syncExec(new Runnable(){
							@Override
							public void run() {
								diskNum = Integer.parseInt(diskNumber.getText());
							}});
						hanoi(diskNum, towers.get(0), towers.get(2), towers.get(1));
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								restart.setEnabled(true);
								positive.setEnabled(true);
								negative.setEnabled(true);
								changeDisksNum(Integer.parseInt(diskNumber.getText()));
								refresh();
								TowersUI.this.solution = false;
							}});
						
					}});
				t.setDaemon(true);
				t.start();
			}
		});
		Composite speedComp = new Composite(controls, SWT.NONE);
		speedComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		speedComp.setLayout(new GridLayout(2, false));
		Label sp = new Label(speedComp, SWT.NONE);
		sp.setFont(font);
		sp.setText("Speed: ");
		final Spinner speed = new Spinner(speedComp, SWT.NONE);
		speed.setSelection(1);
		speed.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				int selection = speed.getSelection();
				if (selection <= 0)
					e.doit = false;
				else
					waitTime = DEFULT_WAIT_TIME / selection;
			}
		});
		final Button exit = new Button(controls, SWT.PUSH);
		exit.setFont(font);
		exit.setAlignment(SWT.CENTER);
		exit.setText("Exit");
		exit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				display.timerExec(-1, updateTimer);
				shell.dispose();
			}
		});

		new Label(parent, SWT.NONE).setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void winComposite() {
		final Player player = startMedia("win tower.mp3");
		Rectangle dispBounds = display.getBounds();
		final Image winImage = new Image(display,
				new ImageData(TowersUI.class.getResourceAsStream("towers win.png"))
						.scaledTo(dispBounds.width, dispBounds.height));
		currentComposite = new Composite(shell, SWT.NONE);
		currentComposite.setLayout(new FormLayout());
		canvas = new Canvas(currentComposite, SWT.DOUBLE_BUFFERED);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		PaletteData palette = new PaletteData(new RGB[] { white.getRGB(),
				black.getRGB() });
		ImageData sourceData = new ImageData(16, 16, 1, palette);
		sourceData.transparentPixel = 0;
		final Cursor cursor = new Cursor(display, sourceData, 0, 0);
		canvas.setCursor(cursor);
		canvas.setLayoutData(formData);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		canvas.addPaintListener(new PaintListener(){
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(winImage, 0, 0);
			}});
		final TraverseListener traverser = new TraverseListener(){
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ESC) {
					changeDisksNum(3);
					currentComposite.dispose();
					customGame();
					shell.layout();
				}
			}};
		shell.addTraverseListener(traverser);
		currentComposite.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				stopMedia(player);
				shell.removeTraverseListener(traverser);
				winImage.dispose();
				cursor.dispose();
			}
		});
	}

	private void changeDisksNum(int disks) {
		Iterator<Tower> iter = towers.iterator();
		while (iter.hasNext())
			iter.next().clear();
		while (disks != 0)
			towers.get(0).push(new Disk(disks--));
	}

	private void createShapes() {
		Rectangle area = display.getClientArea();
		area.width -= 400;
		int towerWidth = 300;
		int identWidth = 200;
		int identHeight = 200;
		Tower tower1 = new Tower(identWidth, area.height - identHeight, identWidth, identHeight,
				identWidth - towerWidth/2, area.height - identHeight, identWidth + towerWidth/2,
				area.height - identHeight);
		tower1.push(new Disk(3));
		tower1.push(new Disk(2));
		tower1.push(new Disk(1));
		towers.add(tower1);

		Tower tower2 = new Tower(area.width/2, area.height - identHeight, area.width/2, identHeight,
				area.width/2 - towerWidth/2, area.height - identHeight, area.width/2 + towerWidth/2,
				area.height - identHeight);
		towers.add(tower2);

		Tower tower3 = new Tower(area.width - identWidth, area.height - identHeight, area.width - identWidth,
				identHeight, area.width - identWidth - towerWidth/2, area.height - identHeight,
				area.width - identWidth + towerWidth/2, area.height - identHeight);
		towers.add(tower3);
	}

	private void hanoi(int number, Tower Source, Tower Destination,
			Tower Auxilary)		//source method
	{
		if (number == 0) //reached the top end of the elements
			return;
		hanoi(number - 1, Source, Auxilary, Destination);
		move(Source, Destination);		//describe the solution to the user
		hanoi(number - 1, Auxilary, Destination, Source);
	}

	private void move(final Tower source, final Tower destination) {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		display.syncExec(new Runnable(){
			@Override
			public void run() {
				destination.push(source.pop());
				incrementMoves();
				refresh();
			}
			});
	}

	public void incrementMoves() {
		CLabel c = (CLabel) shell.getData("moves");
		String s = c.getText();
		c.setText("   moves done: " + (Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1))+1) + "");
		if (towers.get(0).isEmpty() && towers.get(1).isEmpty() && ! solution) {// third tower has all the disks
			display.timerExec(-1, updateTimer);
			currentComposite.dispose();
			winComposite();
			shell.layout();
		}
	}

	public void refresh() {
		canvas.redraw();
	}

	public ArrayList<Tower> getTowers() {
		return towers;
	}

	private Player startMedia(String path) {
		MediaLocator loc;
		Player player = null;
		File file = new File(path);
		System.out.println(file.exists());
		// Create a Medialocator that represents our clip.  
		// This should be a file URL, so first we create 
		// an object representing the file and then we 
		// get the URL from that File object
		try {
			loc = new MediaLocator(
				file.toURI().toURL().toExternalForm());
			// Create the JMF Player for the audio file
			  player = Manager.createPlayer(loc);
			  // Play it
			  player.start();
			  System.out.println("sossa");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (NoPlayerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return player;
	}

	private void stopMedia(Player player) {
		player.stop();
	}

	public void handCursor() {
		canvas.setCursor(handCursor);
	}

	public void defaultCursor() {
		canvas.setCursor(defaultCursor);
	}
}
