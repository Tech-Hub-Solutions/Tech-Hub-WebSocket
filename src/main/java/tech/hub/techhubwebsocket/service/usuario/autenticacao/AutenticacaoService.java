package tech.hub.techhubwebsocket.service.usuario.autenticacao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import tech.hub.techhubwebsocket.entity.usuario.Usuario;
import tech.hub.techhubwebsocket.repository.UsuarioRepository;
import tech.hub.techhubwebsocket.service.usuario.dto.UsuarioDetailsDto;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "TechHub";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndIsAtivoTrue(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(
                  String.format("usuario: %s não encontrado!", username));
        }

        return new UsuarioDetailsDto(usuarioOpt.get());
    }

    public UsuarioDetailsDto getUsuarioDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UsuarioDetailsDto) authentication.getPrincipal();
    }

    public Usuario getUsuarioFromUsuarioDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetailsDto usuarioDetailsDto = (UsuarioDetailsDto) authentication.getPrincipal();

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioDetailsDto.getId());
        return usuario.get();

    }

}
