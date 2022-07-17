package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    public void save_Item() throws Exception{
        //given
        Book item1 = new Book();
        item1.setAuthor("베르나르 베르베르");
        item1.setIsbn("11111111");
        item1.setName("개미");
        //when
        itemService.saveItem(item1);
        //then
    }

    @Test
    public void duplicate_Add_Item() throws Exception{
        //given
        Book item1 = new Book();
        item1.setAuthor("베르나르 베르베르");
        item1.setIsbn("11111111");
        item1.setName("개미");


        Book book = new Book();
        book.setAuthor("베르나르 베르베르");
        book.setIsbn("11111111");
        book.setName("개미");

        //when
        itemService.saveItem(item1);
        itemService.saveItem(book);

        //then
        assertEquals(item1.getId(),book.getId());
    }

    @Test
    public void reduce_Items() throws Exception{
        //given
        Album album = new Album();
        album.setStockQuantity(1);
        //when
        itemService.saveItem(album);
        album.removeStock(2);
//        itemService.saveItem(album);
        //then
        fail("Fail");
    }
}