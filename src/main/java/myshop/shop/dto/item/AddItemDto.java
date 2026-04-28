package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myshop.shop.entity.item.ItemCategory;
import myshop.shop.entity.item.ItemStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString(of = {"sellerNo", "name", "itemCategory", "price", "totalStock", "discount", "addItemOptionDtoList", "mainImage", "mainImagePath",
"subImages", "subImagesPath", "content", "itemStatus", "useOptions"})
public class AddItemDto {
    private Long sellerNo;

    private String name;
    private ItemCategory itemCategory;

    private int price;
    private int totalStock;
    private int discount;
    private List<AddItemOptionDto> addItemOptionDtoList = new ArrayList<>();

    private MultipartFile mainImage;
    private String mainImagePath;
    private List<MultipartFile> subImages = new ArrayList<>();
    private List<String> subImagesPath = new ArrayList<>();
    private String content;

    private ItemStatus itemStatus;
    private boolean useOptions;     //옵션 사용 체크박스

    public AddItemDto() {
    }

    public AddItemDto(Long sellerNo, String name, ItemCategory itemCategory, int price, int totalStock, int discount, List<AddItemOptionDto> addItemOptionDtoList, MultipartFile mainImage, String mainImagePath, List<MultipartFile> subImages, List<String> subImagesPath, String content, boolean useOptions) {
        this.sellerNo = sellerNo;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.addItemOptionDtoList = addItemOptionDtoList;
        this.mainImage = mainImage;
        this.mainImagePath = mainImagePath;
        this.subImages = subImages;
        this.subImagesPath = subImagesPath;
        this.content = content;
        this.itemStatus = ItemStatus.판매중;
        this.useOptions = useOptions;
    }

    public AddItemDto(Long sellerNo, String name, ItemCategory itemCategory, int price, int totalStock, int discount, List<AddItemOptionDto> addItemOptionDtoList, MultipartFile mainImage, String mainImagePath, List<MultipartFile> subImages, List<String> subImagesPath, String content, ItemStatus itemStatus, boolean useOptions) {
        this.sellerNo = sellerNo;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.addItemOptionDtoList = addItemOptionDtoList;
        this.mainImage = mainImage;
        this.mainImagePath = mainImagePath;
        this.subImages = subImages;
        this.subImagesPath = subImagesPath;
        this.content = content;
        this.itemStatus = itemStatus;
        this.useOptions = useOptions;
    }

    public AddItemDto(Long sellerNo, String name, ItemCategory itemCategory, int price, int totalStock, int discount, String mainImagePath, String subImagesPath, String content, ItemStatus itemStatus) {
        this.sellerNo = sellerNo;
        this.name = name;
        this.itemCategory = itemCategory;
        this.price = price;
        this.totalStock = totalStock;
        this.discount = discount;
        this.mainImagePath = mainImagePath;
        this.subImagesPath.add(subImagesPath);
        this.content = content;
        this.itemStatus = itemStatus;
    }

    public void updateAddItemOptionDtoList(AddItemOptionDto addItemOptionDto) {
        this.addItemOptionDtoList.add(addItemOptionDto);
    }

    public void addTotalStock(int optionStock) {
        this.totalStock += optionStock;
    }
}
