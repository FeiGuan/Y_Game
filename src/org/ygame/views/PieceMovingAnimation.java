package org.ygame.views;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class PieceMovingAnimation extends Animation {
	RootPanel panel;
	Image moving;
	ImageResource piece;
	Button start, end;
	int startX, startY, startWidth, startHeight, endX, endY;
	Audio soundAtEnd;
	boolean cancelled;

	public PieceMovingAnimation(Button startButton, Button endButton,
			ImageResource startRes) {
		start = startButton;
		panel = RootPanel.get();
		end = endButton;
		piece = startRes;
//		panel = (RootPanel) start.getParent().getParent().getParent()
//				.getParent().getParent().getParent().getParent().getParent()
//				.getParent().getParent().getParent().getParent().getParent()
//				.getParent().getParent().getParent().getParent().getParent()
//				.getParent();
		startX = panel.getWidgetLeft(start);
		startY = panel.getWidgetTop(start);
		startWidth = startButton.getOffsetWidth();
		startHeight = startButton.getOffsetHeight();
		endX = panel.getWidgetLeft(end);
		endY = panel.getWidgetTop(end);
		// soundAtEnd = sfx;
		cancelled = false;

		moving = new Image(startRes);
		moving.setPixelSize(startWidth, startHeight);
		panel.add(moving, startX, startY);
	}

	@Override
	protected void onUpdate(double progress) {
		int x = (int) (startX + (endX - startX) * progress);
		int y = (int) (startY + (endY - startY) * progress);
		double scale = 1 + 0.5 * Math.sin(progress * Math.PI);
		int width = (int) (startWidth * scale);
		int height = (int) (startHeight * scale);
		moving.setPixelSize(width, height);
		x -= (width - startWidth) / 2;
		y -= (height - startHeight) / 2;

		panel.remove(moving);
		moving = new Image(piece.getSafeUri());
		moving.setPixelSize(width, height);
		panel.add(moving, x, y);
	}

	@Override
	protected void onCancel() {
		if (!cancelled) {
			if (soundAtEnd != null)
				soundAtEnd.play();
			panel.remove(moving);
		}
	}

}
