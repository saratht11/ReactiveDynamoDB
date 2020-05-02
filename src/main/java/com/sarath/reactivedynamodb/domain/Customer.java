package com.sarath.reactivedynamodb.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Customer {
    private String customerID;
    private String fName;
    private String lName;
    private String contactNo;
    private Address address;
    private String createdTimeStamp;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("CustomerID")
    public String getCustomerID() {
        return customerID;
    }

    @DynamoDbAttribute("CustomerFirstName")
    public String getfName() {
        return fName;
    }

    @DynamoDbAttribute("CustomerLastName")
    public String getlName() {
        return lName;
    }

    @DynamoDbAttribute("CustomerContactNumber")
    public String getContactNo() {
        return contactNo;
    }

    @DynamoDbAttribute("CustomerAddress")
    public Address getAddress() {
        return address;
    }

    @DynamoDbAttribute("CustomerCreatedTime")
    public String getCreatedTimeStamp() { return createdTimeStamp; }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) { this.createdTimeStamp = createdTimeStamp; }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", address=" + address +
                ", createdTimeStamp='" + createdTimeStamp + '\'' +
                '}';
    }
}
