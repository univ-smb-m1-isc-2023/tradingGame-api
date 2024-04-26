package tyg.tradinggame.tradinggame.infrastructure.persistence.game;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import tyg.tradinggame.tradinggame.infrastructure.persistence.game.enums.GameTypeEnum;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.currentGameDate < g.finalGameDate")
    List<Game> findUnfinshedGames();

    @Query("SELECT g FROM Game g WHERE g.currentGameDate <= g.finalGameDate AND g.type = :gameType")
    List<Game> findUnfinshedGamesByType(@Param("gameType") GameTypeEnum gameType);

    @Query("SELECT g FROM Game g WHERE g.type = :gameType")
    List<Game> findGamesByType(@Param("gameType") GameTypeEnum gameType);

    @Query("SELECT g FROM Game g WHERE g.currentGameDate < g.finalGameDate AND g.type = :gameType AND g.isLoaded = false")
    List<Game> findUnfinshedNotLoadedGamesByType(@Param("gameType") GameTypeEnum gameType);

    @Modifying
    @Transactional
    @Query("UPDATE Game g SET g.isLoaded = :isLoaded WHERE g.currentGameDate < g.finalGameDate AND g.type = :gameType")
    void setLoadState(@Param("gameType") GameTypeEnum gameType,
            @Param("isLoaded") boolean isLoaded);

}