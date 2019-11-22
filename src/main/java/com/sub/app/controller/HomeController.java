package com.sub.app.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sub.app.domain.Events;
import com.sub.app.model.UserModel;
import com.sub.app.service.UserDetailService;

@RestController
public class HomeController {

	@Autowired
	private UserDetailService userDetailService;

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public UserModel principal(Principal principal) {
		UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}

	@PostMapping(value = { "/home" })
	public String home(Model model) {
		model.addAttribute("userName", getPrincipal());
		return "home";
	}

	@RequestMapping(value = "/erase")
	public ResponseEntity<String> erase() {
		return userDetailService.erase();
	}

	@PostMapping(value = "/events")
	public ResponseEntity<String> addEvents(@RequestBody @Valid Events events) {
		return userDetailService.addEvents(events);
	}

	@GetMapping(value = "/getAllEvents")
	public ResponseEntity<List<Events>> getAllEvents() {
		return userDetailService.getAllEvents();
	}

	@GetMapping(value = "/events/actor/{actorID}")
	public ResponseEntity<List<Events>> getEventsByActor() {
		return userDetailService.getEventsByActor();
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}
