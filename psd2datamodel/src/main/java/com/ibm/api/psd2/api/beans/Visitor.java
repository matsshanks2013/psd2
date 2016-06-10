package com.ibm.api.psd2.api.beans;

public interface Visitor
{
	public <T, U> T visit(U u);
}
