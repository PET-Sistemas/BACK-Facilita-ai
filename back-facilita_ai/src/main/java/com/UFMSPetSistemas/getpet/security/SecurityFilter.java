package com.UFMSPetSistemas.getpet.security;

import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import com.UFMSPetSistemas.getpet.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //pega o token do cabeçalho da requisição
        var token = this.recoverToken(request);
        if (token != null) {
            // compara o "subject" do token e procura o usuário no banco
            var login = tokenService.validateToken(token);
            UserDetails usuario = usuarioRepository.findByEmail(login);

            // verifica a role do usuário encontrado
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            // registra o usuário no contexto do spring security e faz com que os endpoints reconheçam o usuário como autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // continua com o fluxo do filtro, seguindo com a requisição
        filterChain.doFilter(request, response);
    }

    //Metodo que recupera o token do cabeçalho da requisição "Bearer token" e retorna só o token
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
