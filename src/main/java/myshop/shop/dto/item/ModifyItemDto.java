package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.Item;
import myshop.shop.entity.item.ItemOption;
import myshop.shop.entity.item.ItemStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@ToString(of = {"itemNo", "name", "price", "totalStock", "itemStatus", "discount", "modifyItemOptionDtoList", "mainImage", "subImages"})
public class ModifyItemDto {
    private Long itemNo;
    private String name;
    private int price;

    private int totalStock;
    private ItemStatus itemStatus;
    private int discount;

    private List<ModifyItemOptionDto> modifyItemOptionDtoList = new ArrayList<>();

    private MultipartFile mainImage;
    private String mainImagePath;
    private List<MultipartFile> subImages = new ArrayList<>();
    private List<String> subImagesPath = new ArrayList<>();
    private String content;

    public ModifyItemDto() {
    }

    public ModifyItemDto(String name, int price, int totalStock, ItemStatus itemStatus, int discount, List<ModifyItemOptionDto> modifyItemOptionDtoList) {
        this.name = name;
        this.price = price;
        this.totalStock = totalStock;
        this.itemStatus = itemStatus;
        this.discount = discount;
        this.modifyItemOptionDtoList = modifyItemOptionDtoList;
    }

    public ModifyItemDto(Item item, List<ItemOption> itemOptionList) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.totalStock = item.getTotalStock();
        this.itemStatus = item.getItemStatus();
        this.discount = item.getDiscount();
        this.modifyItemOptionDtoList = itemOptionList.stream()
                .map(ModifyItemOptionDto::new)
                .toList();
        this.content = item.getContent();
    }
}
