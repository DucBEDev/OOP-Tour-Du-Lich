package entity;

import java.awt.Image;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class Tour {
    public static final String STATUS_AVAILABLE = "CÒN VÉ";
    public static final String STATUS_SOLD_OUT = "HẾT VÉ";

    private String tourId;
    private Image image;
    private String tourName;
    private String description;
    private LocalDate departureDate;
    private int duration;
    private String departureLocation;
    private LocalTime departureTime;
    private String destination;
    private String transportInfo;
    private double adultPrice;
    private double childPrice;
    private int maxParticipants;
    private int currentParticipants;
    private String status;
    private static int nextTourId =1;

    public Tour() {

    }

    public Tour(String tourId, Image image, String tourName, String description, LocalDate departureDate, int duration, String departureLocation, 
            LocalTime departureTime, String destination, String transportInfo, double adultPrice, double childPrice, 
            int maxParticipants, int currentParticipants, String status) {
    	this.tourId = tourId;
    	this.image=image;
        this.tourName = tourName;
        this.description = description;
        this.departureDate = departureDate;
        this.duration = duration;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.destination = destination;
        this.transportInfo = transportInfo;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.status = status;
    }

        //Generate Getter & Setter for tourId
    public String getTourId() {
        return tourId;
    }
    public void setTourId(String tourId) {
    	this.tourId = tourId;
    }

        //Generate Getter & Setter for image
    public Image getImage() 
    {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }

        //Generate Getter & Setter for tourName
    public String getTourName() {
        return tourName;
    }

        //Check tourName valid
    public void setTourName(String tourName) {
        if (tourName.length() <= 255) {
            this.tourName = tourName;
        }
        else {
            throw new IllegalArgumentException("Tên tour phải nhỏ hơn 255 ký tự");
        }
    }

        //Generate Getter for departureDate
    public LocalDate getDepartureDate() {
        return departureDate;
    }

        //departureDate must be after or at the present day.
    public void setDepartureDate(LocalDate departureDate) {
        if (departureDate.isAfter(LocalDate.now()) || departureDate.isEqual(LocalDate.now())) {
            this.departureDate = departureDate;
        } else {
            throw new IllegalArgumentException("Ngày khởi hành không hợp lệ");
        }
    }

        //Generate Getter & Setter for duration
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

        //Generate Getter & Setter for departureLocation
    public String getDepartureLocation() {
        return departureLocation;
    }

        //Check departureLocation valid
    public void setDepartureLocation(String departureLocation) {
        if (departureLocation.length() <= 50) {
            this.departureLocation = departureLocation;
        }
        else {
            throw new IllegalArgumentException("Địa điểm khởi hành phải nhỏ hơn 50 ký tự");
        }
    }

        //Generate Getter for departureTime
    public LocalTime getDepartureTime() {
        return departureTime;
    }

        //Check the departureTime must be after the present day
    public void setDepartureTime(LocalTime departureTime) {
        if (departureTime.isAfter(LocalTime.now())) {
            this.departureTime = departureTime;
        }
        else {
            throw new IllegalArgumentException("Giờ khởi hành không hợp lệ");
        }
    }

        //Generate Getter & Setter for destination
    public String getDestination() {
        return destination;
    }

        //Check destination valid
    public void setDestination(String destination) {
        if (destination.length() <= 50) {
            this.destination = destination;
        }
        else {
            throw new IllegalArgumentException("Địa điểm đến phải nhỏ hơn 50 ký tự");
        }
    }

        //Generate Getter & Setter for transportInfo
    public String getTransportInfo() {
        return transportInfo;
    }
    public void setTransportInfo(String transportInfo) {
        this.transportInfo = transportInfo;
    }

        //Generate Getter for adultPrice
    public double getAdultPrice() {
        return adultPrice;
    }

        //adultPrice must be greater than 0
    public void setAdultPrice(double adultPrice) {
        if (adultPrice > 0) {
            this.adultPrice = adultPrice;
        }
        else {
            throw new IllegalArgumentException("Giá vé người lớn phải là số dương");
        }
    }

    //Generate Getter for childPrice
    public double getChildPrice() {
        return childPrice;
    }

    //childPrice must be greater than 0
    public void setChildPrice(double childPrice) {
        if (childPrice> 0) {
            this.childPrice = childPrice;
        }
        else {
            throw new IllegalArgumentException("Giá vé trẻ em phải là số dương");
        }
    }

    //Generate Getter for status
    public String getStatus() {
        return status;
    }

    //Check if the status Valid / Invalid
    public void setStatus(String status) {
        if (status.equals(STATUS_AVAILABLE) || status.equals(STATUS_SOLD_OUT)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public int getCurrentParticipants() {
		return currentParticipants;
	}

	public void setCurrentParticipants(int currentParticipants) {
		this.currentParticipants = currentParticipants;
	}
	
	@Override
	public String toString() {
		return "Tour_ID: " + tourId + "Tour_Name: " + tourName + "Description: " + description + "Departure date: " + departureDate + "Duration: " + duration
				+ "Departure location: " + departureLocation + "Departure Time: " + departureTime + "Destination: " + destination + "Transport info: " + transportInfo
				+ "Adult price: " + adultPrice + "Child price: " + childPrice+ "Max participants: " + maxParticipants + "Current participants: " + currentParticipants + "Status: " + status;
	}
	
	/*public Tour(String tourId, String tourName, String description, LocalDate departureDate, int duration, String departureLocation, 
    LocalTime departureTime, String destination, String transportInfo, BigDecimal adultPrice, BigDecimal childPrice, 
    int maxParticipants, int currentParticipants, String status)*/
}
