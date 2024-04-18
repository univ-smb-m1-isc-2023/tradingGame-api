package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.currentGameDate < g.finalGameDate")
    List<Game> findUnfinshedGames();

}