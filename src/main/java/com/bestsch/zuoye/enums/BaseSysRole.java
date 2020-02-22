package com.bestsch.zuoye.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * 用户中心的用户身份
 * @author xuchuxiao
 *
 */
public enum BaseSysRole
{
	Student(3, "学生"), Teacher(1, "教师"), Tourist(0,"游客"),SchLeader(2,"校领导"),Parent(4,"家长"),AreaLeader(5,"局领导");
	
	private int id;
	private String name;

	private BaseSysRole(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public static List<BaseSysRole> list;

	public static List<BaseSysRole> list()
	{
		if (list != null) return list;

		list = new ArrayList<>();

		EnumSet<BaseSysRole> roleSet = EnumSet.allOf(BaseSysRole.class);
		for (BaseSysRole role : roleSet)
			list.add(role);

		return list;
	}

	public static BaseSysRole getById(int id)
	{
		switch (id)
		{
		case 1:
			return Student;
		case 2:
			return Teacher;
		default:
			return null;
		}
	}
}
