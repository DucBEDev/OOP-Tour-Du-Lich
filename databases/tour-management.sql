-- Create database
CREATE DATABASE TourManagement;
GO
USE TourManagement;
GO

DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Tour;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS [Order];
DROP TABLE IF EXISTS CustomerSupportMessage;

USE TourManagement;

-- Create Customer table
CREATE TABLE Customer (
    CustomerID CHAR(10) PRIMARY KEY,  -- Thêm CustomerID để dễ quản lý
    FullName NVARCHAR(50) NOT NULL,
    Phone CHAR(10) NOT NULL UNIQUE,  -- Thêm UNIQUE cho số điện thoại
    Email VARCHAR(255) UNIQUE,
    Address NVARCHAR(255),
    Status NVARCHAR(20) DEFAULT N'Khách' CHECK (Status IN (N'Đã đăng nhập', N'Khách')),
    Username NVARCHAR(20) UNIQUE,
    Password VARCHAR(255) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE()  -- Thêm thời gian tạo tài khoản
);

-- Create Tour table
CREATE TABLE Tour (
    TourID CHAR(10) PRIMARY KEY,
    Images NVARCHAR(MAX),
    TourName NVARCHAR(255) NOT NULL,
    Description NVARCHAR(MAX),  -- Thêm mô tả tour
    DepartureDate DATE NOT NULL,
    Duration INT CHECK (Duration > 0),  -- Đổi thành số ngày để dễ tính toán
    DepartureLocation NVARCHAR(50) NOT NULL,
    DepartureTime TIME NOT NULL,
    Destination NVARCHAR(50) NOT NULL,
    TransportInfo NVARCHAR(20) CHECK (TransportInfo IN (N'Xe', N'Máy bay', N'Tàu')),
    AdultPrice DECIMAL(12,2) CHECK (AdultPrice >= 0),  -- Đổi sang DECIMAL để chính xác hơn
    ChildPrice DECIMAL(12,2) CHECK (ChildPrice >= 0),
    MaxParticipants INT CHECK (MaxParticipants > 0),  -- Thêm số lượng người tối đa
    CurrentParticipants INT DEFAULT 0 CHECK (CurrentParticipants >= 0),
    Status NVARCHAR(20) DEFAULT N'Còn vé' CHECK (Status IN (N'Còn vé', N'Hết vé', N'Đã hủy')),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Create Employee table
CREATE TABLE Employee (
    EmployeeID CHAR(10) PRIMARY KEY,
    FullName NVARCHAR(50) NOT NULL,
    Phone VARCHAR(11) NOT NULL UNIQUE,
    Email VARCHAR(50) UNIQUE,
    Address NVARCHAR(255),
    Username NVARCHAR(20) UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Permissions NVARCHAR(50) CHECK (Permissions IN ('admin', 'staff')),
    HireDate DATE DEFAULT GETDATE(),  -- Thêm ngày tuyển dụng
    Status NVARCHAR(20) DEFAULT N'Đang làm việc' CHECK (Status IN (N'Đang làm việc', N'Đã nghỉ'))
);

-- Create Order table
CREATE TABLE [Order] (
    OrderID CHAR(10) PRIMARY KEY,
    CustomerID CHAR(10) NOT NULL,  -- Thay đổi reference sang CustomerID
    TourID CHAR(10) NOT NULL,
    AdultTickets INT DEFAULT 0 CHECK (AdultTickets >= 0),
    ChildTickets INT DEFAULT 0 CHECK (ChildTickets >= 0),
    OrderTime DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(12,2) CHECK (TotalAmount >= 0),
    PaymentMethod NVARCHAR(50),  -- Thêm phương thức thanh toán
    Status NVARCHAR(20),
    ConfirmedBy CHAR(10),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (TourID) REFERENCES Tour(TourID) ON DELETE NO ACTION,  -- Thay đổi CASCADE thành NO ACTION
    FOREIGN KEY (ConfirmedBy) REFERENCES Employee(EmployeeID) ON DELETE NO ACTION
);

-- Create CustomerSupportMessage table
CREATE TABLE CustomerSupportMessage (
    MessageID INT IDENTITY(1,1) PRIMARY KEY,
    CustomerID CHAR(10),  -- Thay đổi UserID thành CustomerID
    EmployeeID CHAR(10),  -- Thêm EmployeeID để biết ai trả lời
    Content NVARCHAR(MAX) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(20) DEFAULT N'Chưa xử lý' CHECK (Status IN (N'Chưa xử lý', N'Đang xử lý', N'Đã xử lý')),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

-- Create indexes
CREATE INDEX IX_Customer_Username ON Customer(Username);
CREATE INDEX IX_Customer_Phone ON Customer(Phone);
CREATE INDEX IX_Customer_Email ON Customer(Email);
CREATE INDEX IX_Tour_Status_DepartureDate ON Tour(Status, DepartureDate);
CREATE INDEX IX_Order_Status ON [Order](Status);
CREATE INDEX IX_Order_CustomerID ON [Order](CustomerID);
CREATE INDEX IX_Order_TourID ON [Order](TourID);
CREATE INDEX IX_CustomerSupportMessage_Status ON CustomerSupportMessage(Status);

-- Sample data insertion
-- Customer data
INSERT INTO Customer (CustomerID, FullName, Phone, Email, Address, Status, Username, Password)
VALUES 
('CUS001', N'Nguyễn Văn An', '0901234567', 'nguyenvana@email.com', N'123 Nguyễn Huệ, Q1, TP.HCM', N'Đã đăng nhập', 'nguyenvana', 'hashed_password_1'),
('CUS002', N'Trần Thị Bình', '0907654321', 'tranthib@email.com', N'456 Lê Lợi, Q1, TP.HCM', N'Đã đăng nhập', 'tranthib', 'hashed_password_2'),
('CUS003', N'Lê Hoàng Cương', '0909876543', 'lehoangc@email.com', N'789 Võ Văn Tần, Q3, TP.HCM', N'Khách', 'lehoangc', 'hashed_password_3');

-- Employee data
INSERT INTO Employee (EmployeeID, FullName, Phone, Email, Address, Username, Password, Permissions, HireDate)
VALUES 
('EMP001', N'Lê Văn Chính', '0909876543', 'levanc@company.com', N'TP.HCM', 'levanc', 'hashed_password_4', 'admin', '2023-01-01'),
('EMP002', N'Phạm Thị Dung', '0908765432', 'phamthid@company.com', N'TP.HCM', 'phamthid', 'hashed_password_5', 'staff', '2023-02-01'),
('EMP003', N'Trần Văn Em', '0907654321', 'tranvane@company.com', N'TP.HCM', 'tranvane', 'hashed_password_6', 'staff', '2023-03-01');

-- Order data
INSERT INTO [Order] (OrderID, CustomerID, TourID, AdultTickets, ChildTickets, OrderTime, TotalAmount, Status, ConfirmedBy)
VALUES 
('ORD001', 'CUS001', 'TOUR001', 2, 1, '2024-11-01 09:00', 5000000, N'Đã thanh toán', 'EMP001'),
('ORD002', 'CUS002', 'TOUR002', 2, 0, '2024-11-02 10:00', 10000000, N'Chờ thanh toán', 'EMP002'),
('ORD003', 'CUS003', 'TOUR003', 1, 2, '2024-11-03 11:00', 12000000, N'Đã thanh toán', 'EMP001');

-- CustomerSupportMessage data
INSERT INTO CustomerSupportMessage (CustomerID, EmployeeID, Content, Status)
VALUES 
('CUS001', 'EMP001', N'Cần thông tin chi tiết về lịch trình tour Đà Lạt', N'Đã xử lý'),
('CUS002', 'EMP002', N'Xác nhận đặt cọc tour Phú Quốc', N'Đang xử lý'),
('CUS003', NULL, N'Cần tư vấn về tour Đà Nẵng - Hội An', N'Chưa xử lý');

-- Trigger để tự động cập nhật CurrentParticipants của Tour
GO
CREATE TRIGGER TR_UpdateTourParticipants
ON [Order]
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    -- Cập nhật cho INSERT và UPDATE
    UPDATE t
    SET CurrentParticipants = (
        SELECT ISNULL(SUM(o.AdultTickets + o.ChildTickets), 0)
        FROM [Order] o
        WHERE o.TourID = t.TourID
        AND o.Status NOT IN (N'Hủy')
    )
    FROM Tour t
    WHERE t.TourID IN (
        SELECT TourID FROM inserted
        UNION
        SELECT TourID FROM deleted
    );
    
    -- Tự động cập nhật trạng thái tour
    UPDATE Tour
    SET Status = CASE 
        WHEN CurrentParticipants >= MaxParticipants THEN N'Hết vé'
        ELSE N'Còn vé'
    END
    WHERE TourID IN (
        SELECT TourID FROM inserted
        UNION
        SELECT TourID FROM deleted
    );
END;
