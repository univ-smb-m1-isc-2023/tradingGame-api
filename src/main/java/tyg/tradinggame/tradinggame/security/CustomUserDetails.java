package tyg.tradinggame.tradinggame.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tyg.tradinggame.tradinggame.infrastructure.persistence.game.Player;

public class CustomUserDetails implements UserDetails {

    private final Player user;

    public CustomUserDetails(Player user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Vous pouvez définir les rôles de l'utilisateur ici. Pour l'instant, nous renvoyons un rôle statique "ROLE_USER".
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // D'autres méthodes de UserDetails à implémenter...

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
