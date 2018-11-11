package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("select usuario from Usuario usuario where usuario.email = :email and usuario.contrasena = :contrasena")
    Usuario findByEmailAndContrasena(@Param("email") String email, @Param("contrasena") String contrasena);

    @Query("select count(usuario) from Usuario usuario")
    Integer contar();

    @Query(value = "SELECT * FROM usuario m offset(?1) limit(?2)", nativeQuery = true)
    List<Usuario> paginar(int offset, int limit);
}