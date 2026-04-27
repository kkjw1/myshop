package myshop.shop.repository.Item;

import myshop.shop.entity.Seller;
import myshop.shop.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findBySeller(Seller seller);
}
