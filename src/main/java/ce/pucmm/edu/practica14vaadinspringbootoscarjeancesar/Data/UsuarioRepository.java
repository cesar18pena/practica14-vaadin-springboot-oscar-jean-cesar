package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Data;

import ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario,  String> {

    @Query("select usuario from Usuario usuario where usuario.email = :email and usuario.contrasena = :contrasena")
    Usuario findByEmailAndContrasena(@Param("email") String email, @Param("contrasena") String contrasena);
}