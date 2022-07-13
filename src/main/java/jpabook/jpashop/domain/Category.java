package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",      //N대N 관계에선 일대다 / 다대일로 풀어내는 중간 테이블이 필요함. JoinTable이 해당 역할을 수행. (단, 실무에선 사용하지 않음)
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    // *** 카테고리 구조 작성 ***
    /* 셀프로 양방향 연관관계를 작성할 수 있다.
    특히 카테고리의 경우 계층구조를 갖기 때문에 해당 코드를 다음과 같이 정의할 수 있다.*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this); //셀프로 자식에 추가된 값을 set해준다.
    }

}
