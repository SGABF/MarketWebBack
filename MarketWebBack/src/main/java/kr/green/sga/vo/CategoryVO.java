package kr.green.sga.vo;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 상품 분류 카테고리
category_idx	int				
category_name	String	10			
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class CategoryVO {
	private int category_idx;
	private String category_name;
}
