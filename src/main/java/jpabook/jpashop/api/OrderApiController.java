package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    @GetMapping("/api/orders")
    public List<OrderFlatDto> order() {
        return orderQueryRepository.findAllByDto_flat();
    }

    /**
     * 컬랙션 조회 최적화
     * @return
    @GetMapping("/api/orders")
    public List<OrderQueryDto> order() {
        return orderQueryRepository.findAllByDto_optimization();
    }
     */




    /** GOOD
     * 페이지 네이션 적용, 및 쿼리 최적화
     @GetMapping("/api/orders")
     public List<OrderDto> order_page(
     @RequestParam(value="offset", defaultValue = "0") int offset,
     @RequestParam(value="limit", defaultValue = "100") int limit) {
     List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

     List<OrderDto> result = orders.stream().map(OrderDto::new).collect(Collectors.toList());
     return result;
     }*/

    /** BAD
     * 컬랙션 dto 직접 조회
    @GetMapping("/api/orders")
    public List<OrderQueryDto> orders() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    */



    /** BAD
     * 패치 조인은 되었지만 페이징 불가
    @GetMapping("/api/orders")
    public List<OrderDto> orderv3() {

        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }
     */

    /**
     * 역참조 시 중복 쿼리 문제 발생
    @GetMapping("/api/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

     */
    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName; // 상품 명
        private int orderPrice;  //주문 가격
        private int count;       //주문수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }

    }

}
