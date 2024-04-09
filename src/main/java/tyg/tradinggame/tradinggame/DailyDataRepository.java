
package tyg.tradinggame.tradinggame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyDataRepository extends JpaRepository<DailyData, Long> {

}