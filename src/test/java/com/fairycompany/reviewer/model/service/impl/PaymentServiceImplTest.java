package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.PaymentDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.PaymentService;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class PaymentServiceImplTest {
    MockitoSession mockito;

    @Spy
    private SessionRequestContent content;

    @Mock
    private PaymentDao paymentDao;

    @Mock
    private UserDao userDao;

    @Mock
    private TransactionManager transactionManager;

    @InjectMocks
    private PaymentService paymentService = PaymentServiceImpl.getInstance();

    @BeforeMethod
    public void setUp() {
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
    }

    @AfterMethod
    public void tearDown() {
        mockito.finishMocking();
    }

    @Test
    public void testFindAllUserPayments() throws DaoException, ServiceException {
        List<Payment> payments = createPaymentList();
        List<BigDecimal> expectedPrices = List.of(new BigDecimal("15"), new BigDecimal("20"), new BigDecimal("25"));
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(paymentDao.findAllUserPayments(anyLong(), anyLong(), anyInt())).thenReturn(payments);
        when(paymentDao.findTotalPaymentAmount(anyLong())).thenReturn(9L);

        List actualPayments = paymentService.findAllUserPayments(content);

        assertEquals(actualPayments, payments);
        verify(content).addRequestAttribute(RequestAttribute.TOTAL_PRICE_LIST, expectedPrices);
        verify(content).addRequestAttribute(RequestAttribute.PAGE_AMOUNT, 3L);
        verify(content).addRequestAttribute(RequestAttribute.ACTUAL_PAGE, 1L);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindAllUserPaymentsDaoFindPaymentException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(paymentDao.findAllUserPayments(anyLong(), anyLong(), anyInt())).thenThrow(DaoException.class);

        paymentService.findAllUserPayments(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testFindAllUserPaymentsDaoFindPaymentAmountException() throws DaoException, ServiceException {
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User());
        when(content.getRequestParameter(ACTUAL_PAGE)).thenReturn("1");
        when(content.getSessionAttribute(ROW_AMOUNT)).thenReturn("3");
        when(paymentDao.findTotalPaymentAmount(anyLong())).thenThrow(DaoException.class);

        paymentService.findAllUserPayments(content);
    }

    @Test
    public void testAddPayment() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenReturn(new BigDecimal("40"));

        assertTrue(paymentService.addPayment(content));
        verify(userDao).updateUserBalance(1, new BigDecimal("10"));
        verify(paymentDao).updateTotalBalance(new BigDecimal("30"));
    }

    @Test
    public void testAddPaymentMoneyLack() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenReturn(new BigDecimal("29"));

        assertFalse(paymentService.addPayment(content));
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddPaymentDaoUserBalanceException() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenThrow(DaoException.class);

        paymentService.addPayment(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddPaymentDaoUpdateUserBalanceException() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenReturn(new BigDecimal("31"));
        when(userDao.updateUserBalance(eq(1L), eq(new BigDecimal("1")))).thenThrow(DaoException.class);

        paymentService.addPayment(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddPaymentDaoUpdateTotalBalanceException() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenReturn(new BigDecimal("31"));
        when(paymentDao.updateTotalBalance(eq(new BigDecimal("30")))).thenThrow(DaoException.class);

        paymentService.addPayment(content);
    }

    @Test (expectedExceptions = ServiceException.class)
    public void testAddPaymentDaoAddOrderException() throws DaoException, ServiceException {
        Map<Order, Game> shoppingCart = createShoppingCart();
        when(content.getSessionAttribute(SessionAttribute.USER)).thenReturn(new User.UserBuilder().setUserId(1).createUser());
        when(content.getSessionAttribute(SessionAttribute.SHOPPING_CART)).thenReturn(shoppingCart);
        when(userDao.findUserBalance(eq(1L))).thenReturn(new BigDecimal("31"));
        doThrow(DaoException.class).when(paymentDao).addOrders(anyLong(), eq(shoppingCart));

        paymentService.addPayment(content);
    }

    private List<Payment> createPaymentList() {
        List<Order> orders = List.of(
                new Order.OrderBuilder().setPrice(new BigDecimal("5")).createOrder(),
                new Order.OrderBuilder().setPrice(new BigDecimal("10")).createOrder(),
                new Order.OrderBuilder().setPrice(new BigDecimal("15")).createOrder()
        );

        List<Payment> payments = List.of(
                new Payment(1, 1, LocalDateTime.now(), List.of(orders.get(0), orders.get(1))),
                new Payment(2, 1, LocalDateTime.now(), List.of(orders.get(0), orders.get(2))),
                new Payment(3, 1, LocalDateTime.now(), List.of(orders.get(1), orders.get(2)))
        );

        return payments;
    }

    private Map<Order, Game> createShoppingCart() {
        Map<Order, Game> shoppingCart = new LinkedHashMap<>();
        shoppingCart.put(new Order.OrderBuilder().setGameName("Doom").createOrder(), new Game.GameBuilder().setPrice(new BigDecimal("5")).createGame());
        shoppingCart.put(new Order.OrderBuilder().setGameName("RE 7").createOrder(), new Game.GameBuilder().setPrice(new BigDecimal("10")).createGame());
        shoppingCart.put(new Order.OrderBuilder().setGameName("TLoU 2").createOrder(), new Game.GameBuilder().setPrice(new BigDecimal("15")).createGame());

        return shoppingCart;
    }
}