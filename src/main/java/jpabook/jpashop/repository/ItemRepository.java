package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ItemRepository {

    private final EntityManager em;

    public ItemRepository(EntityManager em){
        this.em = em;
    }

    public void save(Item item) {
        log.info("::: 아이템 이름 확인 ::: ={}", item.getName());
        if (item.getId() == null) {
            log.info("::: persist");
            em.persist(item);
        } else {
            log.info("::: merge");
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
