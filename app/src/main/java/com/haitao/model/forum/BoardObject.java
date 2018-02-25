package com.haitao.model.forum;

import com.haitao.model.BaseObject;
import com.haitao.model.TagObject;

import java.util.List;

/**
 * Created by apple on 17/4/6.
 */

public class BoardObject extends BaseObject{
    public String id = "";
    public String name = "";
    public String typeid = "";
    public String categoryName = "";
    public List<TagObject> subBoardModels = null;
}
