package de.cromon.graphics.ui;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import de.cromon.graphics.BLPTexture;
import de.cromon.input.TouchListener;
import de.cromon.math.Vector2;
import de.cromon.wowme.Game;

public class Button implements TouchListener {
	public Button(float x, float y, float cx, float cy) {
		mButtonQuad = new UIQuad(x, y, cx, cy);
		mPosition = new Vector2(x, y);
		mSize = new Vector2(cx, cy);
		
		mButtonQuad.setTextureEdges(79, 22, 128, 32);
		
		Game.Instance.getInputMgr().addTouchListener(this);
	}
	
	static {
		mTextureUp = new BLPTexture("Interface/Buttons/UI-Panel-Button-UP.blp");
		mTextureDown = new BLPTexture("Interface/Buttons/UI-Panel-Button-Down.blp");
		mTextRender = new FontRender("Calibri-24");
	}
	
	public void draw() {
		if(mIsPressed)
			mButtonQuad.setTexture(mTextureDown.getTexture());
		else
			mButtonQuad.setTexture(mTextureUp.getTexture());
		
		mButtonQuad.draw();
		mText.FitHeight = true;
		mText.HorizontalCenter = true;
		mText.Position = mPosition;
		mText.Size = mSize;
		mText.Text = "Button1";
		mText.Position = new Vector2(mPosition.x + (7.0f / 79.0f) * mSize.x, mPosition.y + (5.0f / 22.0f) * mSize.y);
		mText.Size = new Vector2((66.0f / 79.0f) * mSize.x, (13 / 22.0f) * mSize.y);
		
		mTextRender.renderString(mText, 0xFFDFDFDF);
	}
	

	@Override
	public void OnTouchEvent(MotionEvent args) {		
		if(args.getPointerCount() != 1)
			return;
		
		int action = args.getActionMasked();
		
		if(action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) {
			PointerCoords coords = new PointerCoords();
			args.getPointerCoords(0, coords);
			if(mButtonQuad.isInRect(coords.x, coords.y)) {
				Game.Instance.displayError("Click event seems to work!", true);
			}
			
			mIsPressed = false;
			return;
		} else if(action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN) {
			PointerCoords coords = new PointerCoords();
			args.getPointerCoords(0, coords);
			
			mIsPressed = mButtonQuad.isInRect(coords.x, coords.y);
		}
	}
	
	private TextElement mText = new TextElement();
	private Vector2 mPosition, mSize;
	private UIQuad mButtonQuad;
	private boolean mIsPressed = false;
	
	private static BLPTexture mTextureUp, mTextureDown;
	private static FontRender mTextRender;
}
