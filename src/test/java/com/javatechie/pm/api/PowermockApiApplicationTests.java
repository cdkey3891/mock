package com.javatechie.pm.api;

import com.javatechie.pm.api.dto.OrderRequest;
import com.javatechie.pm.api.dto.OrderResponse;
import com.javatechie.pm.api.service.OrderService;
import com.javatechie.pm.api.util.NotificationUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.javatechie.pm.api.*")
public class PowermockApiApplicationTests {

    @InjectMocks
    private OrderService service;

    OrderRequest request = new OrderRequest(111, "Mobile", 1, 10000, "test@gmail.com", true);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(NotificationUtil.class);
    }

    @Test
    public void testStaticMethod() {
        System.out.println("--start test static method--");
        // Given
        String emailid = "test@gmail.com";
        PowerMockito.mockStatic(NotificationUtil.class);
        // When
        when(NotificationUtil.sendEmail(emailid)).thenReturn("success");
        // Then
        OrderResponse response = service.checkoutOrder(request);
        assertEquals("success", response.getMessage());
    }

    /**
     * Purpose: Mock private method by PowerMockito, user ArgumentMatchers to mock argument
     * Condition: private method "addDiscount" at OrderService, method return int
     * Expect: be able to mock this private method to return expected value
     */
    @Test
    public void testPrivateMethod01() {
        System.out.println("--start test private method return int--");
        //create spy
        OrderService spy = PowerMockito.spy(service);
        //mock private method to return 2000
        try {
            doReturn(2000).when(spy, "addDiscount", ArgumentMatchers.any());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //run public method which calls this private method
        OrderResponse response = spy.checkoutOrder(request);
        //price: 10000 - mocked value: 2000 = 8000
        int actualPrice = response.getResponse().getPrice();
        System.out.println("price : " + actualPrice);
        int expectedPrice = 8000;
        assertThat(actualPrice, is(expectedPrice));
    }

    /**
     * Purpose: Mock private method by PowerMockito, user ArgumentMatchers to mock argument
     * Condition: private method "showOrderInformation" at OrderService, method return void
     * Expect: be able to mock this private method to return expected value
     */
    @Test
    public void testPrivateMethod02() {
        System.out.println("--start test private method return void--");
        //create spy
        OrderService spy = PowerMockito.spy(service);
        //mock private method to return nothing
        try {
            doNothing().when(spy, "showOrderInformation", request);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        //run public method which calls this private method
        spy.checkoutOrder(request);
    }

    @Test
    public void test() {

    }
}
