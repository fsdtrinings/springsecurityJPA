package com.mkj.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mkj.app.entity.AppUsers;
import com.mkj.app.entity.RapipayEmployee;
import com.mkj.app.repo.AppUserRepository;
import com.mkj.app.repo.RapipayEmployeeRepository;


@RestController
@RequestMapping("/admin")
public class AdminWebController {

	@Autowired
	AppUserRepository repo;
	
	@GetMapping("/test")
	public String homepage()
	{
		return "Admin can add user";
	}
	
	@PostMapping("/create")
	public String doAdd(@RequestBody AppUsers user)
	{
		Object obj = repo.save(user);
		return obj!=null?"Data Saved "+user : "Not Saved "+obj; 
	}
	
}


