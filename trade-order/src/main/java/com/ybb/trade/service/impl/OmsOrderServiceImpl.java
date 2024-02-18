package com.ybb.trade.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import com.ybb.trade.common.MessageResult;
import com.ybb.trade.common.PageUtils;
import com.ybb.trade.common.Query;
import com.ybb.trade.entity.AuthMember;
import com.ybb.trade.entity.OmsOrder;
import com.ybb.trade.entity.OmsOrderItem;
import com.ybb.trade.entity.OmsOrderReturnReason;
import com.ybb.trade.enume.OrderStatusEnum;
import com.ybb.trade.feign.CartService;
import com.ybb.trade.feign.MemberService;
import com.ybb.trade.feign.ProductService;
import com.ybb.trade.feign.WareService;
import com.ybb.trade.service.OmsOrderItemService;
import com.ybb.trade.service.OmsOrderService;
import com.ybb.trade.mapper.OmsOrderMapper;
import com.ybb.trade.to.OrderCreateTo;
import com.ybb.trade.vo.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.ybb.trade.constant.OrderConstant.USER_ORDER_TOKEN_PREFIX;

/**
* @author zhouf
* @description 针对表【oms_order(订单)】的数据库操作Service实现
* @createDate 2024-02-08 12:40:26
*/
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder>
    implements OmsOrderService{
    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CartService cartService;
    @Autowired
    private WareService wareService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OmsOrderItemService omsOrderItemService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public OrderConfirmVo confirmOrder(Long memberId){
        OrderConfirmVo orderConfirmVo = OrderConfirmVo.builder().build();
         // 1、获取收货物地址
        MessageResult addressResult =  memberService.listMemberAddress(memberId);
        if(addressResult.getCode() == 0) {
            List<MemberAddressVo> list = (List<MemberAddressVo>) addressResult.getData();
            orderConfirmVo.setMemberAddressVos(list);
        }

         // 2、获取账户支付方式
        MessageResult  result = memberService.listPayCoin();
        if(result.getCode() == 0) {
            String coinStr = JSON.toJSONString(result.getData());
            List<String> payCoinList = JSON.parseArray(coinStr,String.class);
            orderConfirmVo.setPayCoins(payCoinList);
        }
         // 3、获取购物车
        MessageResult messageResult = cartService.getCurrentCartItems();
        List<CartItemVo> cartItemVoList = new ArrayList<>();
        if(messageResult.getCode() == 0) {
            String jsonString = JSON.toJSONString(messageResult.getData());
            cartItemVoList = JSON.parseArray(jsonString,CartItemVo.class);
            orderConfirmVo.setItems(cartItemVoList);
        }

        // 4、获取库存信息
        List<Long> skuList = cartItemVoList.stream()
                .filter((e)->e.getCheck() == true)
                .map(CartItemVo::getSkuId)
                .collect(Collectors.toList());
        MessageResult hasStockResult = wareService.getSkuHasStock(skuList);
        if(hasStockResult.getCode() == 0) {
            String jsonStr = JSON.toJSONString(hasStockResult.getData());
            List<SkuHasStockVo> hasStockVos = JSON.parseArray(jsonStr,SkuHasStockVo.class);
            Map<Long, Boolean> stocks = hasStockVos.stream()
                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
            orderConfirmVo.setStocks(stocks);
        }
        // 生成令牌
        //为用户设置一个token，三十分钟过期时间（存在redis）
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(USER_ORDER_TOKEN_PREFIX + memberId,token);
        orderConfirmVo.setOrderToken(token);
        return orderConfirmVo;
    }

    /**
     *
     * @param orderSubmitVo
     */
    @Override
    @Transactional
    public void submitOrder(OrderSubmitVo orderSubmitVo, AuthMember member) {
        confirmVoThreadLocal.set(orderSubmitVo);
        //1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = orderSubmitVo.getOrderToken();
        //通过lure脚本原子验证令牌和删除令牌
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(USER_ORDER_TOKEN_PREFIX + member.getId()),
                orderToken);
        if(result == 0) {
            throw new RuntimeException("令牌不合法");
        }
        // check 远程获取address
        Long addressId = orderSubmitVo.getAddrId();
        MessageResult messageResult = memberService.info(addressId);
        MemberAddressVo memberAddressVo = JSON.parseObject(JSON.toJSONString(messageResult.getData()),MemberAddressVo.class);
        if(memberAddressVo == null) {
            throw new RuntimeException("地址不合法");
        }
        MessageResult messageResult1 = memberService.queryWallet(orderSubmitVo.getPayCoin(),member.getId());
        if(messageResult1.getCode() == 500||messageResult1.getData() == null) {
            throw new RuntimeException("钱包不存在");
        }
        UmsWalletVo umsWalletVo = JSON.parseObject(JSON.toJSONString(messageResult1.getData()),UmsWalletVo.class);
        if(umsWalletVo.getBalance().compareTo(orderSubmitVo.getPayPrice())<0) {
            throw new RuntimeException("账户金额不足");
        }
        //1、创建订单、订单项等信息
        OrderCreateTo order = createOrder(orderSubmitVo.getPayCoin(),member);
        //2、验证价格
        BigDecimal payAmount = order.getOrder().getPayAmount();
        BigDecimal payPrice = orderSubmitVo.getPayPrice();
        if (Math.abs(payAmount.subtract(payPrice).doubleValue()) > 0.01) {
            throw new RuntimeException("应付金融异常");
        }
        // 保存订单
        saveOrder(order);
        // 调用远程账户暂扣除
        MessageResult walletUpdateResult = memberService.lockWallet(orderSubmitVo.getPayCoin(),member.getId(),payPrice,order.getOrder().getOrderSn());
        if(walletUpdateResult.getCode() == 500) {
            throw new RuntimeException("扣除失败");
        }
        //4、库存锁定,只要有异常，回滚订单数据
        //订单号、所有订单项信息(skuId,skuNum,skuName)
        WareSkuLockVo lockVo = new WareSkuLockVo();
        lockVo.setOrderSn(order.getOrder().getOrderSn());
        //获取出要锁定的商品数据信息
        List<OrderItemVo> orderItemVos = order.getOrderItems().stream().map((item) -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            orderItemVo.setSkuId(item.getSkuId());
            orderItemVo.setCount(item.getSkuQuantity());
            orderItemVo.setTitle(item.getSkuName());
            return orderItemVo;
        }).collect(Collectors.toList());
        lockVo.setLocks(orderItemVos);
        //TODO 调用远程锁定库存的方法
        //出现的问题：扣减库存成功了，但是由于网络原因超时，出现异常，导致订单事务回滚，库存事务不回滚(解决方案：seata)
        //为了保证高并发，不推荐使用seata，因为是加锁，并行化，提升不了效率,可以发消息给库存服务
        MessageResult stockLockResult = wareService.orderLockStock(lockVo);
        if (stockLockResult.getCode() == 500) {
            //锁定失败
            String msg = stockLockResult.getMessage();
            throw new RuntimeException(msg);
        }
        rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", order.getOrder());
        // 远程调用删除购物车里的数据
        cartService.clearCartItem();
        return;
    }

    /**
     * 保存订单所有数据
     * @param orderCreateTo
     */
    private void saveOrder(OrderCreateTo orderCreateTo) {


        //获取订单信息
        OmsOrder order = orderCreateTo.getOrder();
        order.setModifyTime(new Date());
        order.setCreateTime(new Date());
        //保存订单
        this.baseMapper.insert(order);
        List<OmsOrderItem> orderItems = orderCreateTo.getOrderItems();
        omsOrderItemService.saveBatch(orderItems);
    }

    private OrderCreateTo createOrder(String payWay,AuthMember member) {

        OrderCreateTo createTo = new OrderCreateTo();

        //1、生成订单号
        String orderSn = IdWorker.getTimeId();
        OmsOrder orderEntity = builderOrder(payWay,orderSn,member);

        //2、获取到所有的订单项
        List<OmsOrderItem> orderItemEntities = builderOrderItems(orderSn);

        //3、验价(计算价格、积分等信息)
        computePrice(orderEntity, orderItemEntities);

        createTo.setOrder(orderEntity);
        createTo.setOrderItems(orderItemEntities);

        return createTo;
    }
    /**
     * 计算价格的方法
     * @param orderEntity
     * @param orderItemEntities
     */
    private void computePrice(OmsOrder orderEntity, List<OmsOrderItem> orderItemEntities) {

        //总价
        BigDecimal total = new BigDecimal("0.0");
        //优惠价
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal intergration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");

        //积分、成长值
        Integer integrationTotal = 0;
        Integer growthTotal = 0;

        //订单总额，叠加每一个订单项的总额信息
        for (OmsOrderItem orderItem : orderItemEntities) {
            //优惠价格信息
            coupon = coupon.add(orderItem.getCouponAmount());
            promotion = promotion.add(orderItem.getPromotionAmount());
            intergration = intergration.add(orderItem.getIntegrationAmount());

            //总价
            total = total.add(orderItem.getRealAmount());

            //积分信息和成长值信息
            integrationTotal += orderItem.getGiftIntegration();
            growthTotal += orderItem.getGiftGrowth();

        }
        //1、订单价格相关的
        orderEntity.setTotalAmount(total);
        //设置应付总额(总额+运费)
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(intergration);

        //设置积分成长值信息
        orderEntity.setIntegration(integrationTotal);
        orderEntity.setGrowth(growthTotal);

        //设置删除状态(0-未删除，1-已删除)
        orderEntity.setDeleteStatus(0);

    }

    private List<OmsOrderItem> builderOrderItems(String orderSn) {
        List<OmsOrderItem> orderItemEntityList = new ArrayList<>();
        MessageResult messageResult = cartService.getCurrentCartItems();
        //最后确定每个购物项的价格
        if(messageResult.getCode() == 0) {
            String jsonStr = JSON.toJSONString(messageResult.getData());
            List<OrderItemVo> currentCartItems = JSON.parseArray(jsonStr,OrderItemVo.class);
            if (currentCartItems != null && currentCartItems.size() > 0) {
                orderItemEntityList = currentCartItems.stream().map((items) -> {
                    //构建订单项数据
                    OmsOrderItem orderItemEntity = builderOrderItem(items);
                    orderItemEntity.setOrderSn(orderSn);
                    return orderItemEntity;
                }).collect(Collectors.toList());
            }
        }
        return orderItemEntityList;
    }

    private OmsOrderItem builderOrderItem(OrderItemVo items) {

        OmsOrderItem orderItemEntity = new OmsOrderItem();

        //1、商品的spu信息
        Long skuId = items.getSkuId();
        //获取spu的信息
        MessageResult messageResult = productService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoData = JSON.parseObject(JSON.toJSONString(messageResult.getData()), SpuInfoVo.class);
        orderItemEntity.setSpuId(spuInfoData.getId());
        orderItemEntity.setSpuName(spuInfoData.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoData.getBrandName());
        orderItemEntity.setCategoryId(spuInfoData.getCatalogId());

        //2、商品的sku信息
        orderItemEntity.setSkuId(skuId);
        orderItemEntity.setSkuName(items.getTitle());
        orderItemEntity.setSkuPic(items.getImage());
        orderItemEntity.setSkuPrice(items.getPrice());
        orderItemEntity.setSkuQuantity(items.getCount());

        //使用StringUtils.collectionToDelimitedString将list集合转换为String
        String skuAttrValues = StringUtils.collectionToDelimitedString(items.getSkuAttrValues(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttrValues);

        //3、商品的优惠信息

        //4、商品的积分信息
        orderItemEntity.setGiftGrowth(items.getPrice().multiply(new BigDecimal(items.getCount())).intValue());
        orderItemEntity.setGiftIntegration(items.getPrice().multiply(new BigDecimal(items.getCount())).intValue());

        //5、订单项的价格信息
        orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
        orderItemEntity.setCouponAmount(BigDecimal.ZERO);
        orderItemEntity.setIntegrationAmount(BigDecimal.ZERO);

        //当前订单项的实际金额.总额 - 各种优惠价格
        //原来的价格
        BigDecimal origin = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        //原价减去优惠价得到最终的价格
        BigDecimal subtract = origin.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(subtract);

        return orderItemEntity;
    }

    /**
     * 构建订单数据
     * @param orderSn
     * @return
     */
    private OmsOrder builderOrder(String payWay,String orderSn,AuthMember member) {
        OmsOrder orderEntity = new OmsOrder();
        orderEntity.setMemberId(member.getId());
        orderEntity.setOrderSn(orderSn);
        orderEntity.setPayType(payWay);
        orderEntity.setMemberUsername(member.getUsername());
        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();

        //远程获取收货地址和运费信息
        MessageResult messageResult = wareService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = JSON.parseObject(JSON.toJSONString(messageResult.getData()),FareVo.class);
        //获取到运费信息
        BigDecimal fare = fareResp.getFare();
        orderEntity.setFreightAmount(fare);
        //获取到收货地址信息
        MemberAddressVo address = fareResp.getAddress();
        //设置收货人信息
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverCity(address.getCity());
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());

        //设置订单相关的状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        orderEntity.setConfirmStatus(0);
        return orderEntity;
    }

    @Override
    public OmsOrder getOrderByOrderSn(String orderSn) {
        return this.baseMapper.selectOne(new QueryWrapper<OmsOrder>().eq("order_sn",orderSn));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrder> page = this.page(
                new Query<OmsOrder>().getPage(params),
                new QueryWrapper<OmsOrder>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        return new PageUtils(null);
    }

    @Override
    public void closeOrder(OmsOrder order) {
        // 关闭订单
        OmsOrder omsOrder = this.baseMapper.selectById(order.getId());
        if(omsOrder == null || omsOrder.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()) {
            order.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(order);
            // 发送mq
            WalletUnlockOrder walletUnlockOrder = WalletUnlockOrder.builder()
                    .orderSn(order.getOrderSn())
                    .amount(order.getPayAmount())
                    .coin(order.getPayType())
                    .memberId(order.getMemberId())
                    .build();
            OrderMq orderMq = new OrderMq();
            BeanUtils.copyProperties(omsOrder,orderMq);
            rabbitTemplate.convertAndSend("wallet-event-exchange","wallet.unlock.order",walletUnlockOrder);
             // 库存回滚
            rabbitTemplate.convertAndSend("order-event-exchange", "order.release.other", orderMq);
        }
    }

}




