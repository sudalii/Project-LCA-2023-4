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

    @Transactional  // 해당 메서드에선 readOnly를 안쓰기 위해 오버라이딩한 것, 우선권 가짐
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) { // == merge()와 완전히 동일한 메커니즘 코드

        Item findItem = itemRepository.findOne(itemId);
        
        // set 쓰지 말고 차라리 아래와 같이 묶자. set으로 풀어버리면 향후 추적하기 상당히 어려워짐
        // findItem.change(name, price, stockQuantity); 
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
