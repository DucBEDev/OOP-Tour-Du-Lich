package entity;

import java.time.LocalDate;

public final class Employee {
    public static final String PERMISSION_STAFF = "staff";
    public static final String PERMISSION_ADMIN = "admin";
    
    public static final String STATUS_WORKING = "Đang làm việc";
    public static final String STATUS_RESIGN = "Đã nghỉ";
    
    private static int nextEmployeeId=1;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String employeeId;
    private String userName;

    @SuppressWarnings("unused")
    private String password;

    private String permissions;
    private LocalDate hireDate;
    private String status;

    public Employee() 
    {

    }

    public Employee(String fullName, String phone, String email, String address, String userName, String password, String permissions, String status) 
    {
    	
        this.fullName = fullName;
        setPhone(phone);
        setEmail(email);
        this.address = address;
        this.userName = userName;
        setPassword(password);
        this.permissions = permissions;
        this.setHireDate(hireDate);
        this.status = status;
    }
    
    public Employee(String employeeId, String fullName, String phone, String email, String address, String userName, String password, String permissions, String status) 
    {
    	this.employeeId = employeeId;
        this.fullName = fullName;
        setPhone(phone);
        setEmail(email);
        this.address = address;
        this.userName = userName;
        setPassword(password);
        this.permissions = permissions;
        this.setHireDate(hireDate);
        this.status = status;
    }
    
    public Employee( String fullName, String phone, String email, String address, String userName, String password, String permissions) 
    {
    	setEmployeeId();  
    	System.out.println(employeeId);
        this.fullName = fullName;
        setPhone(phone);
        setEmail(email);
        this.address = address;
        this.userName = userName;
        setPassword(password);
        this.permissions = permissions;
        this.setHireDate(hireDate);
        this.status = "Đang làm việc";
    }
    
    

        //Generate Getter & Setter for fullName
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

        //Generate Getter for phone
    public String getPhone() {
        return phone;
    }

        //Check the phone number is valid
    public void setPhone(String phone) {
            this.phone = phone;
    }

        //Generate Getter for email
    public String getEmail() {
        return email;
    }

        //Check the email number is valid
    public void setEmail(String email) {
            this.email = email;
    }

        //Generate Getter & Setter for address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

        //Generate Getter & Setter for employeeId
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId() 
    {
        this.employeeId = employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
    	this.employeeId = employeeId;
    }

        //Generate Getter & Setter for username
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

        //Generate Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassWord() {
        return password;
    }

        //Generate Setter for permissions
    public String getPermissions() {
        return permissions;
    }

        //Check if permissions is valid
    public void setPermissions(String permissions) {
            this.permissions = permissions;
    }
 
    	//Generate Getter & Setter for hireDate
	public void setHireDate(LocalDate hireDate)
	{
		this.hireDate=hireDate;
	}

	public LocalDate getDateHired()
	{
		return hireDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
			this.status = status;
	}
	

    	//Check if employee is admin
	public boolean isAdmin() {
		return this.permissions.equals(PERMISSION_ADMIN);
	}

    	//Check if employee is employee
	public boolean isStaff() {
		return this.permissions.equals(PERMISSION_STAFF);
	}
    
    @Override
    public String toString() {
        return "Employee ID: " + employeeId + 
               ", Name: " + fullName + 
               ", Phone: " + phone + 
               ", Email: " + email + 
               ", Address: " + address + 
              // ", Date Hired: " + hireDate + 
               ", Permissions: " + permissions + 
               ", Username: " + userName + 
               ", Password: " + password;
    }
    
   
}
