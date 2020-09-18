package net.javaguides.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;

@Controller
public class MainController {

    @Autowired
    private UserRepository repository;
    
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
//	@GetMapping("admin")
//	public String admin() {
//		return "admin";
//	}
	
    @GetMapping("/admin")
    private String getAll(Model model){
        model.addAttribute("users", repository.findAll());
        return "admin";
    }

    @GetMapping("/delete/{id}")
    private String deleteMovie(@PathVariable("id") Long id){
        java.util.Optional<User> user = repository.findById(id);
        if(user.isPresent()){
            repository.delete(user.get());
        }else{
            System.err.println("not found");
            return "redirect:/admin?error";
        }
        return "redirect:/admin?success";
    }  
    @GetMapping(path = {"update/{id}"})
    private String addForm(@PathVariable("id") java.util.Optional<Long> id, Model model){
        if(id.isPresent()){
            model.addAttribute("user", repository.findById(id.get()));
        }else{
            model.addAttribute("user", new User());
        }
        return "update";
    }

    @PostMapping("/update")
    private String insertOrUpdate(User user){
    	System.out.println(user);
        if(user.getId() == null){
            repository.save(user);
        }else{
        	 java.util.Optional<User> userOptional = repository.findById(user.getId());
            if(userOptional.isPresent()){
                User editUser = userOptional.get();
                editUser.setFirstName(user.getFirstName());
                editUser.setLastName(user.getLastName());
                editUser.setEmail(user.getEmail());
                repository.save(editUser);
            }
        }
        return "redirect:/admin?success";    
   	}

}
