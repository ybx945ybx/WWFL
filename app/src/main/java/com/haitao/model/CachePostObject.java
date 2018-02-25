package com.haitao.model;

import com.haitao.model.forum.BoardObject;
import com.haitao.view.richEdit.RichTextEditor;

import java.util.ArrayList;

/**
 * 缓存发布贴子
 * Created by tqy on 15/12/2.
 */
public class CachePostObject extends BaseObject {
	private static final long serialVersionUID = 1L;
	public BoardObject boardObject = null;
	public SectionObject section = null;
	public int categoryPosition = -1;
	public String categoryId = "";
	public String categoryName = "";
	public String subject = "";
	public String content = "";
	public ArrayList<String> images = null;
	public ArrayList<EditDataObject> editList = null;

	public String boardId = "";
	public String boardName = "";
	public String subBoardId = "";
	public String subBoardName = "";

}