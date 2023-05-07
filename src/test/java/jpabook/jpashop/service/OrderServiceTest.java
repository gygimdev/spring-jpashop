package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        //given
        Member member = createMember("회원1");
        Item book = creakBook("JPA", 10000 ,10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000);
        //재고
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    private Member createMember(String name){
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강서", "123-123"));
        em.persist(member);
        return member;
    }

    private Item creakBook(String name, int price, int quantity){
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

//    @Test
//    public void 상품주문_재고수량초과() {
//        Member member = createMember("회원1");
//        Item book = creakBook("JPA", 10000, 10);
//
//        int orderCount
//
//    }

//    @Test
//    public void 주문취소() {
//        //given
//
//        //when
//
//        //then
//    }

//    @Test
//    public void 상품주문_재고수량초과() {
//        //given
//
//        //when
//
//        //then
//
//    }

}