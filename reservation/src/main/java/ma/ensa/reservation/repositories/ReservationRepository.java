package ma.ensa.reservation.repositories;


import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.Reservation;
import org.aspectj.weaver.ast.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
