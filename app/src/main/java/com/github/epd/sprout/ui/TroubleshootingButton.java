
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.windows.help.WndHlpCatTS;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class TroubleshootingButton extends Button {

	private Image image;

	public TroubleshootingButton() {
		super();

		width = image.width;
		height = image.height;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		image = Icons.get(Icons.T_S);
		add(image);
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x;
		image.y = y;
	}

	@Override
	protected void onTouchDown() {
		image.brightness(1.5f);
		Sample.INSTANCE.play(Assets.SND_CLICK);
	}

	@Override
	protected void onTouchUp() {
		image.resetColor();
	}

	@Override
	protected void onClick() {
		parent.add(new WndHlpCatTS());
	}

}
