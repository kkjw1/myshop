package myshop.shop.repository.Item;

import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    List<ItemOption> findByItem(Item item);
}
