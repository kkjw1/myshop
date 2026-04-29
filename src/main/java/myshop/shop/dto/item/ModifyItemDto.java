package myshop.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import myshop.shop.entity.item.ItemStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ModifyItemDto {
    private String name;
    private int price;

    private int totalCount;
    private ItemStatus itemStatus;
    private int discount;

    private List<ModifyItemOptionDto> modifyItemOptionDtoList = new ArrayList<>();

    private MultipartFile mainImage;
    private String mainImagePath;
    private List<MultipartFile> subImages = new ArrayList<>();
    private List<String> subImagesPath = new ArrayList<>();
    private String content;
}
