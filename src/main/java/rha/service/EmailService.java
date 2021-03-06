package rha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import rha.jwt.model.security.ActivacionUsuario;
import rha.util.Fecha;
import rha.util.Mail;

@Service
public class EmailService {

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
    private String remitente;
	
	@Value("${protocolo}")
	private String protocolo;
	
	@Value("${dominio}")
	private String dominio;
	
	@Value("${puerto}")
	private String puerto;
	
	@Value("${jwt.expiration}")
    private Long expiration;

    public void enviarEmailPersonalizado(final Mail mail){
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setSubject(mail.getAsunto());
        correo.setText(mail.getContenido());
        correo.setTo(mail.getPara());
        correo.setFrom(mail.getDe());

        javaMailSender.send(correo);
    }

	public void enviarCorreoActivacion(ActivacionUsuario activacionUsuario) {
		SimpleMailMessage correo = new SimpleMailMessage();
		correo.setSubject("Activación de usuario");
		
		String contenido = "Estimado " + activacionUsuario.getUser().getFullName() + "\n\n";
		contenido += "Para finalizar tu registro es necesario que actives tu cuenta de usuario, "
				+ "siguiendo el siguiente enlace o copiándolo en la barra de direcciones de tu "
				+ "navegador.\n\n";
		contenido += protocolo + dominio + puerto;
		contenido += "/activacion/" + activacionUsuario.getTokenActivacion() + "\n\n";
		contenido += "Tienes hasta el " + Fecha.fechaHoraSP(activacionUsuario.getFechaExpiracion()) +
				" para activar tu cuenta"; 
		
		correo.setText(contenido);
		correo.setTo(activacionUsuario.getUser().getEmail());
        correo.setFrom(remitente);
		
		javaMailSender.send(correo);		
	}
    
    
}
