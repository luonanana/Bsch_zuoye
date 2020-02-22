package com.bestsch.zuoye.mem;

public enum GroupType
{
	TOP(0, "根"), GENERAL(1, "普通组"), SCHOOL(2, "学校组"), GRADE(3, "年级组"), CLASS(4, "班级组"), BUREAU(5, "区域组"),PERIOD(6,"学段组"),TEACHER(7,"教师组"),STUDENT(8,"学生组");

	private int mId;
	private String mName;
	
	private GroupType(int id, String name)
	{
		this.mId = id;
		this.mName = name;
	}

	public int id()
	{
		return mId;
	}
	
	public static GroupType byId(int id)
	{
		if (id == GENERAL.id())
			return GENERAL;
		if (id == SCHOOL.id())
			return SCHOOL;
		if (id == GRADE.id())
			return GRADE;
		if (id == CLASS.id())
			return CLASS;
		if (id == TOP.id())
			return TOP;
		if (id == BUREAU.id())
			return BUREAU;
		if (id == PERIOD.id())
			return PERIOD;
		if (id == TEACHER.id())
			return TEACHER;
		if (id == STUDENT.id())
			return STUDENT;
		return null;
	}
	
	@Override
	public String toString()
	{
		return mName;
	}
}
