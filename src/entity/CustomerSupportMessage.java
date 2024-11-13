package entity;

import java.time.LocalDateTime;

public class CustomerSupportMessage {
	public static final String STATUS_NOT_PROCESSED = "Chưa xử lý";
	public static final String STATUS_PROCESSING = "Đang xử lý";
	public static final String STATUS_PROCESSED = "Đã xử lý";
	
	private String content;
	private String customerId;
	private String employeeId;
	private int messageId;
	private LocalDateTime createdAt;
	private String status;
	
	public CustomerSupportMessage() {
		
	}

	public CustomerSupportMessage(String content, String customerId, String employeeId, String status) {
		this.setContent(content);
		this.setCustomerId(customerId);
		this.setEmployeeId(employeeId);
		setStatus(status);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status.equals(STATUS_NOT_PROCESSED) || status.equals(STATUS_PROCESSING) || status.equals(STATUS_PROCESSED)) {
			this.status = status;
		} else {
			throw new IllegalArgumentException("Trạng thái không hợp lệ");
		}
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	@Override
	public String toString() {
		return "Customer ID: " + customerId + "Employee ID: " + employeeId + "Content: " + content + "Status: " + status;
	}

}