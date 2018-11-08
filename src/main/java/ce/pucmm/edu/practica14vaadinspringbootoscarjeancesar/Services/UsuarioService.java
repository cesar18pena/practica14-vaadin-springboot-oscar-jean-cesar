package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Services;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data.UsuarioRepository;
import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(long id, String nombre, String email, String contrasena) throws Exception {
        try {
            return usuarioRepository.save(new Usuario(usuarioRepository.findAll().size() + 1, nombre, email, contrasena));
        } catch (PersistenceException exp) {
            throw new PersistenceException("Hubo un error al crear un nuevo usuario.");
        } catch (NullPointerException exp) {
            throw new NullPointerException("Hubo un error de datos nulos al querer crear el usuario");
        } catch (Exception exp) {
            throw new Exception("Hubo un error general al querer crear el usuario");
        }
    }

    public void editarUsuario(Usuario usuario) throws Exception {
        try {
            usuarioRepository.save(usuario);
        } catch (PersistenceException e) {
            throw new PersistenceException("Hubo un error al editar el usuario.");
        } catch (NullPointerException e) {
            throw new NullPointerException("Al editar el usuario hubo un error de datos nulos.");
        } catch (Exception e) {
            throw new Exception("Hubo un error general al editar un usuario.");
        }
    }

    public boolean validarUsuario(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmailAndContrasena(email, contrasena);
        return (usuario != null);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public long contarUsuario() {
        return usuarioRepository.contar()+1;
    }
}


