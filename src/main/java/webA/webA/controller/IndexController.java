package webA.webA.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	String helloWorld(HttpServletRequest re,Model model){
		model.addAttribute("username", re.getAttribute("username"));
		return "index";
	}

}
