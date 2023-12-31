package com.cos.blog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cos.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm(@RequestParam(value = "error", required = false) String error,
							@RequestParam(value = "exception", required = false) String exception, Model model) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		return "user/loginForm";
	}

	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

	@GetMapping("/auth/findForm")
	public String findForm() {
		return "user/findForm";
	}

	@GetMapping("/auth/google/callback")
	public String googleLoginCallback(OAuth2User oauth2User) {
		userService.googleLogin(oauth2User);
		return "redirect:/";
	}
}
