package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player")
public class Player implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Vous pouvez ajouter d'autres champs selon vos besoins

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonManagedReference
    private List<Wallet> wallets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "admin")
    @JsonManagedReference
    private List<Game> createdGames = new ArrayList<>();

    // Implémentation des méthodes de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Dans cet exemple, nous renvoyons un rôle statique ROLE_USER.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Vous pouvez implémenter une logique d'expiration du compte ici.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Vous pouvez implémenter une logique de verrouillage du compte ici.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Vous pouvez implémenter une logique d'expiration des informations d'identification ici.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Vous pouvez implémenter une logique pour activer ou désactiver le compte ici.
        return true;
    }

    // Getters et setters

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<Game> getCreatedGames() {
        return createdGames;
    }

    public void setCreatedGames(List<Game> createdGames) {
        this.createdGames = createdGames;
    }
}
