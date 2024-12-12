package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Order {
    public static final String STATUS_PENDING = "Chờ thanh toán";
    public static final String STATUS_PAID = "Đã thanh toán";
    public static final String STATUS_CANCELLED = "Hủy";
    public static final String STATUS_COMPLETED = "Hoàn thành";

    private String orderId;
    private String customerId;
    private String tourId;
    private int adultTickets;
    private int childTickets;
    private LocalDateTime orderTime;
    private double totalAmount;
    private String status;
    private String confirmedBy;         //Nhân viên phụ trách đơn hàng
    
    private static int nextOrderId = 1;

    public Order() {}

    public Order(String orderId, String customerId, String tourId, int adultTickets, int childTickets, 
                 LocalDateTime orderTime, double totalAmount, String status, String confirmedBy) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.tourId = tourId;
        setAdultTickets(adultTickets);
        setChildTickets(childTickets);
        this.orderTime = orderTime;
        setTotalAmount(totalAmount);
        setStatus(status);
        this.confirmedBy = confirmedBy;
    }
    
    public Order(String orderId, String customerId, String tourId, int adultTickets, int childTickets, 
             double totalAmount, String status, String confirmedBy) 
    {
    	this.orderId = orderId;
    	this.customerId = customerId;
    	this.tourId = tourId;    	
    	setAdultTickets(adultTickets);
    	setChildTickets(childTickets);
    	setTotalAmount(totalAmount);   
    	setStatus(status);
    	this.confirmedBy = confirmedBy;
}

    // Generate Getter & Setter for orderId
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId() {
    	this.tourId = String.format("ORD%03d", nextOrderId++);
    }

    // Generate Getter & Setter for tourId
    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    // Generate Getter & Setter for adultTickets
    public int getAdultTickets() {
        return adultTickets;
    }

    public void setAdultTickets(int adultTickets) {
        if (adultTickets >= 0) {
            this.adultTickets = adultTickets;
        } else {
            throw new IllegalArgumentException("Số vé người lớn phải là số không âm");
        }
    }

    // Generate Getter & Setter for childTickets
    public int getChildTickets() {
        return childTickets;
    }

    public void setChildTickets(int childTickets) {
        if (childTickets >= 0) {
            this.childTickets = childTickets;
        } else {
            throw new IllegalArgumentException("Số vé trẻ em phải là số không âm");
        }
    }

    // Generate Getter & Setter for orderTime
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    // Generate Getter & Setter for totalAmount
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount> 0) {
            this.totalAmount = totalAmount;
        } else {
            throw new IllegalArgumentException("Tổng giá trị đơn hàng phải là số không âm");
        }
    }

    // Generate Getter & Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
            this.status = status;
    }

    // Generate Getter & Setter for confirmedBy
    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }
    
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public String toString() {
		return "Order ID: " + orderId + "Customer ID: " + customerId + "Tour ID: " + tourId + "Adult Tickets: " + adultTickets
				+ "Child Tickets: " + childTickets + "Order time: " + orderTime + "Total amount: " + totalAmount + "Status: " + status + "Confirmed by: " + confirmedBy;
	}
}
