
package ru.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.entity.Ad;
import ru.diplom.entity.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findByAuthor(User author);
}
