package com.codingdojo.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingdojo.modelos.Usuario;
import com.codingdojo.servicios.ServicioUsuario;

@Controller

public class ControladorUsuario {
	
	private final ServicioUsuario servicioUsuario;
	
	public ControladorUsuario(ServicioUsuario servicioUsuario) {
		this.servicioUsuario = servicioUsuario;
	}

	@RequestMapping( value = "/", method=RequestMethod.GET)
	public String despliegaRegistroyLogin(@ModelAttribute("usuario") Usuario nuevoUsuario) {
		//System.out.println("si estoy");
		return "index.jsp";
	}
	
	@RequestMapping( value= "/registrado", method = RequestMethod.POST)
	public String registrarUsuario(@Valid @ModelAttribute("usuario")Usuario nuevoUsuario,BindingResult result, HttpSession session) {
		if(result.hasErrors()) {
			return "index.jsp";
		}
		else {
			//String hash = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			//System.out.println (hash);
			//nuevoUsuario.setPassword(hash);
			Usuario usuarioObtenido = servicioUsuario.insertIntoUsuarios(nuevoUsuario, result);//en nuevo usuario va password tecleado previo a setter debo enciptar 
			if(usuarioObtenido == null) {
				return "index.jsp";
			}
			session.setAttribute("id", nuevoUsuario.getId());
			//System.out.println(nuevoUsuario.getId());
			session.setAttribute( "userName", nuevoUsuario.getUserName());
			session.setAttribute( "email", nuevoUsuario.getEmail());
			session.setAttribute( "password", nuevoUsuario.getPassword());
			return "redirect:/usuarios/" + nuevoUsuario.getId();
	    }
		
		
	}
	
	@RequestMapping( value="/usuarios/{id}", method = RequestMethod.GET)
	public String despliegaDashboard(@PathVariable("id") long id, Model model, HttpSession session) {
		if(session.getAttribute("id") == null) {
			return "redirect:/";
		}
		else {
			Usuario usuario = servicioUsuario.findUsuario(id);
	    	model.addAttribute("usuario", usuario);
			return "show.jsp";
		}
	}
	
	@RequestMapping( value = "/logout", method = RequestMethod.GET)
	public String logout(  HttpSession session) {
		session.removeAttribute("id");
		session.removeAttribute("userName");
		session.removeAttribute("email");
		session.removeAttribute("password");
		return "redirect:/";
	}
	
	
	@RequestMapping( value= "/login", method =RequestMethod.POST)
	public String login(@RequestParam( value = "email") String email,
						@RequestParam( value = "password") String password,
						HttpSession session,
						RedirectAttributes flash) {
		
		Usuario usuarioEncontrado = servicioUsuario.selectUsuarioWhereEmail(email);
		if( usuarioEncontrado == null) {
			if (email.equals("")) {
				flash.addFlashAttribute("errorLoginEmail", "Por favor proporciona tu email.");
			}
			if (password.equals("")) {
				flash.addFlashAttribute("errorLoginPassword", "Por favor proporciona tu password de usuario.");
			}
			flash.addFlashAttribute("LoginError", "Credenciales incorrectas." );
			//System.out.println("Credenciales invalidas");
			return "redirect:/";
		}
		else {
			if (BCrypt.checkpw(password, usuarioEncontrado.getPassword())) {
				//System.out.println("id " + usuarioEncontrado.getId());
				//System.out.println("userName " + usuarioEncontrado.getUserName());
				session.setAttribute( "id", usuarioEncontrado.getId());
				session.setAttribute( "userName", usuarioEncontrado.getUserName());
				session.setAttribute( "email", usuarioEncontrado.getEmail());
				session.setAttribute( "password", usuarioEncontrado.getPassword());
				return "redirect:/usuarios/" + usuarioEncontrado.getId();
			}
			else {
				flash.addFlashAttribute("LoginError", "Credenciales incorrectas." );
				//System.out.println("Credenciales invalidas");
				return "redirect:/";
			}	
		}	
	}
}
