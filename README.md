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

        Case Study - BudgetAnalysisSystem
            
            to record transactions made by an accoutn holder and provide him,
            a periodic statement.

            1. Register an account holder
            2. Edit an accoount holder profile
            3. retrive an accoutn holder profile
            4. Add a transaction
            5. Edit a transaction
            6. Delete a transaction
            7. Retrive all transaction of an account holder for a given period
            8. Retrive the currentBalance of the accoutn holder
            9. Generate a statement of an accoutn holder for a given period computing
                the totalCredit , totalDebit and balance for that statement period.

        Case Study - AddressBookSystem
            
            to record contacts related to each user.

            1. Each user has to register and at the time of registration they can choose
                to be public or private mode.
            2. Users registred under public mode must be able to choose the category (like doctors,lawyers,.....etc)
            3. Add a category if not exists
            4. A user must be able to request the contact of a user.
            5. A user must be able to share his / her contact with another user by accepting the request
            6. A user msut be able to retrive all the contacts that are shared with him/her.
            7. A user must be able to search for a contact if that contact is public or shared with him. 

        Microservice Design Patterns

            Decomposition Design Patterns
                Decomposition by Doamin
                Sub-Domain bounded Context Pattern
            Integration Design Patterns
                Api Gateway Design Pattern
                Aggregator Design Pattern
            Database Design Patterns
                Database per Service Pattern
                Shared Database Pattern
                CQRS Pattern
                Saga Pattern
            Observability Design Patterns
                Log Aggregation Design Pattern
                Performence Monitoring Design Pattern
                Distributed Tracing Pattern
            Cross Cutting Design Patterns
                Discovery Service Design Pattern
                External Configuaration Pattern
                Circuit Breaking Pattern

            Decomposition by Doamin

                guides us on how the monolithic solution design can be broken into microservices.
                domain refers to a set of features or operations that can be grouped as one unit.

                BudgetAnalysisSystem - decomposition            
                    profiles microservice
                        1. Register an account holder
                        2. Edit an accoount holder profile
                        3. retrive an accoutn holder profile
                    txns microservice
                        4. Add a transaction
                        5. Edit a transaction
                        6. Delete a transaction
                        7. Retrive all transaction of an account holder for a given period
                        8. Retrive the currentBalance of the accoutn holder
                    stateemnt microservice
                        9. Generate a statement of an accoutn holder for a given period computing
                            the totalCredit , totalDebit and balance for that statement period.

            Sub-Domain Bounded Context Pattern (Decomposition by sub-domain)

                BudgetAnalysisSystem - Decompos by sub-domain
                    profiles microservice
                        AccountHolder                   
                            Long ahId
                            String firstName
                            String lastName
                            String mailId
                            String mobile
                    txns microservice
                        AccountHolder
                            Long ahId
                            Double currentBalance
                            Set<Transaction> transactions
                        Transaction                
                            Long txnId
                            String header
                            Double amount
                            TransactionType type
                            LocalDate dateOfTransaction
                            AccountHolder holder
                    stateemnt microservice
                        Transaction                
                            Long txnId
                            String header
                            Double amount
                            TransactionType type
                            LocalDate dateOfTransaction
                    
                        Statement
                            Long ahId
                            String firstName
                            String lastName
                            String mailId
                            String mobile
                            Double currentBalance
                            Set<Transaction> transactions
                            LocalDate fromDate
                            LocalDate toDate
                            Double totalCredit
                            Double totalDebit
                            Double statementBalance




    
    