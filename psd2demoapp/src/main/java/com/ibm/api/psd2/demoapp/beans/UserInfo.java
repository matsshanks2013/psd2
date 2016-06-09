package com.ibm.api.psd2.demoapp.beans;

import java.io.Serializable;

public class UserInfo implements Serializable
{
	private String email;
	private String password;
	private String role;
	
	private String firstName;
	private String lastName;
	private String phone;

	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public Boolean getAccountNonExpired()
	{
		return accountNonExpired;
	}
	public void setAccountNonExpired(Boolean accountNonExpired)
	{
		this.accountNonExpired = accountNonExpired;
	}
	public Boolean getAccountNonLocked()
	{
		return accountNonLocked;
	}
	public void setAccountNonLocked(Boolean accountNonLocked)
	{
		this.accountNonLocked = accountNonLocked;
	}
	public Boolean getCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(Boolean credentialsNonExpired)
	{
		this.credentialsNonExpired = credentialsNonExpired;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	
}
