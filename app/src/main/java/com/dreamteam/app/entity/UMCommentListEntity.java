package com.dreamteam.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.umeng.socialize.bean.UMComment;

@SuppressWarnings("serial")
public class UMCommentListEntity implements Serializable
{
	private ArrayList<UMComment> commentList = new ArrayList<UMComment>();

	public ArrayList<UMComment> getCommentList()
	{
		return commentList;
	}

	public void setCommentList(ArrayList<UMComment> commentList)
	{
		this.commentList = commentList;
	}
}
