package tyg.tradinggame.tradinggame.application;

import tyg.tradinggame.tradinggame.infrastructure.persistence.UserGame;
import tyg.tradinggame.tradinggame.infrastructure.persistence.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public List<UserGame> users() {
        return userRepository.findAll();
    }

    public void delete(Long factId) {
        Optional<UserGame> fact = userRepository.findById(factId);
        fact.ifPresent(userRepository::delete);
    }

    public UserGame create(String name) {
        // FIXME : check if not already present
        UserGame u = new UserGame(name);
        userRepository.save(u);
        return u;
    }

}