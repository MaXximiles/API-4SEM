package fatec.grupodois.endurance.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

<<<<<<< HEAD
    private Usuario user;
=======
    private User user;
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

<<<<<<< HEAD
        return stream(this.user.getUsuarioAuthorities())
=======
        return stream(this.user.getUserAuthorities())
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
<<<<<<< HEAD
        return this.user.getUsuarioPassword();
=======
        return this.user.getUserPassword();
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
    }

    @Override
    public String getUsername() {
<<<<<<< HEAD
        return this.user.getUsuarioEmail();
=======
        return this.user.getUserEmail();
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
<<<<<<< HEAD
        return this.user.isUsuarioIsLocked();
=======
        return this.user.isUserIsLocked();
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
<<<<<<< HEAD
        return this.user.isUsuarioIsActive();
=======
        return this.user.isUserIsActive();
>>>>>>> 2bea5a457ac43bd4613ca51b12f002630fb5629f
    }
}
