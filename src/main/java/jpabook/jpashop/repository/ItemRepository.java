package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor    // final 변수에 대한 EntityManager에 대해 자동으로 @Autowired가 추가됨.
public class ItemRepository {
    private final EntityManager em;

//    public void save(Item item){
//        if (item.getId() == null) {
//            em.persist(item);
//        } else {
//            em.merge(item);
//        }
//    }

    public Long save(Item item){
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
        return item.getId();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
