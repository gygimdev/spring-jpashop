package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 오더를 조회하고
 * 맴버, 배송정보를 함께 가져온다.
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * BAD
     * @return
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    /**
     * BAD
     * 주문을 조회 했는데 2개의 주문 정보 가져온다.
     * 그리고 루프를 돌면서 각 주문의 맴버, 배송정보를 또 조회한다.
     * 중복 쿼리 발생
    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(o -> new OrderSimpleQueryDto(o))
                .collect(Collectors.toList());
    }
     */

    /**
     * GOOD
     * JPA query 를 사용해 중복 쿼리 해결
     */
    @GetMapping("/api/v3/simple-orders")
    public List<OrderQueryDto> orderV3() {
        List<Order> order =  orderRepository.findAllWithMemberDelivery();
        List<OrderQueryDto> result = order.stream()
                .map(o -> new OrderQueryDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Data
    public class OrderQueryDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderQueryDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus= order.getStatus();
            this.address = order.getDelivery().getAddress();
        }
    }


//    @GetMapping("/api/v4/simple-orders")
//    public List<OrderSimpleQueryDto> orderV4() {
//        return orderRepository.findOrderDtos();
//    }


}
