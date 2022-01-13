package com.ibmw.userportal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PhotoResponse 
{
	  private String name;
	  private String url;
	  private String type;
	  private long size;
}
