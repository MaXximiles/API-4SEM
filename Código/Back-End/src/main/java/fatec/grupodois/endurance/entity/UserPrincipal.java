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

    private Usuario user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return stream(this.user.getUsuarioAuthorities())
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getUsuarioPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsuarioEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isUsuarioIsLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isUsuarioIsActive();
    }
}
