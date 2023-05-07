package jpabook.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 총 주문 2개
 * * USER A
 *      * JPA1 BOOK
 *      * JPA2 BOOK
 * * USER B
 *      * SPRING 1 BOOK
 *      * SPRING 2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member1 = createMember("userA", "서울", "거리1", "우편번호1");

            Book book1 = createBook("JPA1 Book", 10000, 100);
            Book book2 = createBook("JPA2 Book", 20000, 100);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member1.getAddress());
            Order order = Order.createOrder(member1, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "부산", "거리2", "우편번호2");

            Book book1 = createBook("Spring1 Book", 10000, 200);
            Book book2 = createBook("Spring2 Book", 40000, 300);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Book createBook(String name, int price, int quantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(quantity);
            em.persist(book1);
            return book1;
        }

        private Member createMember(String username, String city, String street, String zipCode) {
            Member member = new Member();
            member.setName(username);
            member.setAddress(new Address(city, street, zipCode));
            em.persist(member);
            return member;
        }

    }


}
