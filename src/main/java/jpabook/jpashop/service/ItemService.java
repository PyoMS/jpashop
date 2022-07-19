package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional  //상위 Transactional 설정 값을 오버라이딩 했다고 생각하면 됨. default readOnly = false
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경 감지를 통한 update
     */
    @Transactional
    public void updateItem(Long itemId, int price, String name) {
        Item item = itemRepository.findOne(itemId); // 엔티티를 호출. -> 영속 상태 보존
        item.setPrice(price);
        item.setName(name);
        /*itemRepository.save(item);*/    /** save 해줄필요가 없음!
         @Transactional 이 commit 할 때, JPA에서 flush가 발생하는데 Dirty Checking을 해서
         해당 변경사항이 있을 시 알아서 update 처리한 후 commit을 수행한다.
         */
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }


}
