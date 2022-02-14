Microservices
----------------------------------------------------------------------------

    Pre-Requisites Skills

        Java8
        Spring Framework
            Spring Core (IoC,Context,SpEL,AOP)
            Spring Web Rest
            Spring Boot
            Spring Data JPA
    
    Lab Setup

        JDK 8
        STS latest
        MySQL 8 or above

    Introduction

        Monolithic Approach
            Single Writing - We have only one deployment unit per application.

                                        Shopping Application
                                InventoryDAO  -- InventoryService -- InventoryController    
            shoppingDB  <---->  OrdersDAO     -- OrdersService    -- OrdersController       <-----> Clients....
                                DeliveryDAO   -- DeliveryService  -- DeliveryController
                                CustoemrDAO   -- CustoemrService  -- CustoemrController

        Microservice Approach
            is a ecosystem of inter-communicating individual and isolated sub-apps called services

                                        Shopping EcoSystem                                   <-----> Clients....
                                      Inventory Micro Service
            inventoryDB <---->  InventoryDAO  -- InventoryService -- InventoryController    
                                        Orders Micro Service
            ordersDB    <---->  OrdersDAO     -- OrdersService    -- OrdersController      
                                        Delivery Micro Service
            deliveryDB  <---->  DeliveryDAO   -- DeliveryService  -- DeliveryController
                                        Customer Micro Service
            custoemrDB  <---->  CustoemrDAO   -- CustoemrService  -- CustoemrController

        Modern Expectations

            Scalability
            Interoparability
            Techno-Enhancements
        
        Challenges in Microservice Approach

            1. inter service communication 
                    as the microserviceses are scalled at runtime and the 
                    scalling is dynamic as a result, the ip's or ports are not stable.

            2. distrubuted transaction management
                    placing an order is a transaction involving various operations...
                        a. insert the order details in ordersDB thru OrdersMicroService
                        b. update the stock accordingly in inventoryDB thru InventoryMicroService
                        c. insert the delivery record in deliveryDB thru DeliveryMiucroService to track the order

            3. distributed tracing
                    one request is to be processed by multiple microservices and
                    each will have their own logs.

            4. multiple api end points 
                    each microservice will have its own context root and hence
                    multiple context root for a client to address that too 
                    dynamically changing addresses.

            5. configuaration management
                    each microservcie will have its own configuaration, as we imagine that 
                    a minimum of 50 microserviceses are needed for a small scale applcation
                    we will end up in 50 configuration files (.yaml,.properties)........

            6. Monitoring
                    app's health and performence-metrics will allow us to improve
                    the applcitions performence, but here we have 50 or more individual
                    microservices to monitor.....

            





    
    