package com.rowland.UI;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.moribitotech.mtx.scene2d.ui.MenuCreator;
//import com.moribitotech.mtx.MenuCreator;
//import com.moribitotech.mtx.models.base.TableModel;
import com.moribitotech.mtx.scene2d.ui.TableModel;

/**
 * @author Rowland
 *
 */
public class MyMenuCreator extends MenuCreator {

	public static Table createTable(TextureRegion textureBackground) {

		TableModel table = new TableModel(textureBackground);

		return table;
	}

}
