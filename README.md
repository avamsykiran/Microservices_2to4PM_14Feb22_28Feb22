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
            Client Side Component Design Pattern
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
                    3. retrive an account holder profile
                txns microservice
                    4. Add a transaction        POST
                    5. Edit a transaction       PUT
                    6. Delete a transaction     DELETE
                    7. Retrive all transaction of an account holder for a given period  GET
                    8. Retrive the currentBalance of the account holder                 GET
                stateemnt microservice
                    9. Generate a statement of an account holder for a given month and year computing
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
                        Set<Txn> txns
                    Txn                
                        Long txnId
                        String header
                        Double amount
                        TxnType type
                        LocalDate dateOfTransaction
                        AccountHolder holder
                Statement microservice
                    Txn                
                        Long txnId
                        String header
                        Double amount
                        TxnType type
                        LocalDate dateOfTransaction

                    AccountHolderProfile
                        Long ahId
                        String firstName
                        String lastName
                        String mailId
                        String mobile
                        Double currentBalance
                
                    Statement
                        AccountHolderProfile profile
                        Set<Txn> txns
                        LocalDate fromDate
                        LocalDate toDate
                        Double totalCredit
                        Double totalDebit
                        Double statementBalance

        Api Gateway Design Pattern
            
            BudgetAnalysisSystem - API Gateway
                
                    Angular APP/REactJS APP/Andriod APP (CLEINTS)
                                    |
                                    |
                                    ???
                                apiGatway
                            (spring cloud api gateway)
                                    |
                    |---------------|-----------------------|
                    |               |                       |
                    ???               ???                       ???               
                profiles           txns                 stateemnt 
            localhost:9100      localhost:9200        localhost:9300
                    
        Aggregator Design Pattern

            an aggregator is a role played by any microservce that 
                a. when receives a request for a lumsum data (available with mutliple services)
                b. the data is retrived from those services and 
                c. composed into a single aggregated object and served back.

            BudgetAnalysisSystem - API Gateway
                
                        stateemnt 
                        |<---- 1. a req for a statemnt of a specific account holder <-- Client
                        |----------fetech accoutn details from <----------------------- profiles
                        |----------fetech the list of transactiosn from <-------------- txns
                        |-(Compute other details like totalCredit,totalDebit,statemetnBalence)
                        |-(Compose all that data as a Statement object) responseded--> Client

        Client Side Component Design Pattern

            Each SPA is going to composed of components,
            a component is small smnart section of a web page,
            now each component can raise a req parallelly and will be served isolatedly from
            other components.

        Database per Service Pattern

            each microservice is expected to have its own isolated database
            
            BudgetAnalysisSystem 
                    Angular APP/REactJS APP/Andriod APP (CLEINTS)
                                    |
                                    |
                                    ???
                                apiGatway
                            (spring cloud api gateway)
                                    |
                    |---------------|-----------------------|
                    |               |                       |
                    ???               ???                       ???               
                profiles           txns                 stateemnt 
            localhost:9100      localhost:9200        localhost:9300
                    ???               ???                       
                    ???               ???                       
                profilesDB       txnsDB                  
                    
        Shared Database Pattern
            is a anti microservices pattern, to have a single database shared by all microservices,
            but it becoems invitable while working on a brown-field project.

        CQRS Pattern
            Command Query Saggregation 
            this design pattern garunties data consistency when a common piece of data
            is expected to be accessed by multiple clients .

                eg: more than one client can try to book tickets in the smae thater for the smae show.

            we are expected to depend on a message service like RabbitMQ/Kafka ..etc where 
            the command (insert,update,delete) part of the eco-system will raise an event on
            executing a command,
            that event is heard by the Query part of the eco-system which executes pre-compiled views
            of data that are then served on request.

        Saga Pattern

            a saga is a single transaction across mutiple-services in a eco-system.
                placing an order is a transaction involving various operations... (IS A SAGA)
                    a. insert the order details in ordersDB thru OrdersMicroService
                    b. update the stock accordingly in inventoryDB thru InventoryMicroService
                    c. insert the delivery record in deliveryDB thru DeliveryMiucroService to track the order

            a saga can managed in two ways
                1. choreography

                client 
                    |---Req to place an order----> Orderes Service ---req to update stock--> InventoryService --> Delivery 
                                                    order record 
                                                    is inserted  
                                                    the order     <--req cancellation----- if fails 
                                                is rolledback  

                2. orchestration
                
                    client --Req to place an order----> OrchestrationSerice
                                                            | ------req inserting order---> OrderSerivce
                                                            | on success <----------------
                                                            | ------req stock update------> InventorySerivce
                                                            | on success <-----------------
                                                            |----req delivery update -----> DeliveryService

                                                            on any fialure, the prev step is rolledback.

        Observability Design Patterns- Distributed Tracing, Log Aggregation

            1. Pull
                    Each incoming req is givne a unique id and that id is passed along with the
                    req, whereever it is forwarded to. The request traces are then pulled
                    by a centeral server and are persisted there.
            2. Psuh
                    Each incoming req is givne a unique id and that id is passed along with the
                    req, whereever it is forwarded to. The request traces are then pushed by each microservice
                    to a centeral server and are persisted there.   

                        Zipkin-Server + Spring Cloud Sleuth             

                BudgetAnalysisSystem 
                    Angular APP/REactJS APP/Andriod APP (CLEINTS)
                                    |
                                    |
                                    ???
                                apiGatway
                            (spring cloud api gateway)
            |------------------sleuth  |
            |                          |
            |          |---------------|-----------------------|
            |          |               |                       |
            |          ???               ???                       ???               
            |       profiles           txns                 stateemnt 
            |  localhost:9100      localhost:9200        localhost:9300
            |   slueth ???        slueth ???                slueth
            |   |      ???          |    ???                   |        
            |   |   profilesDB    |   txnsDB               |
            |---|-----------------|------------------------|
                                |
                                ???
                    Distributed Tracing Service
                            Zipkin-Server

        Discovery Service Design Pattern

            1. a discovery service is a registary of microservices and thier ip:port's
            2. Each time a microservice instance launches it has to register itself on discovery serviece
            3. When a microservice(A) needs to talk to another microservice(B) , A will fetch the ip:port's list
                from the discovery service and the load balancer on (A) will choose one instance from the list.


            BudgetAnalysisSystem 
                    Angular APP/REactJS APP/Andriod APP (CLEINTS)
                                    |
                                    |
                                    ???
                                apiGatway                      Discovery Service
                            (spring cloud api gateway)        (Netflix Eureka Service)
            |------------------sleuth  |                                    |
            |                          |-address are feteched or registered-|
            |          |---------------|-----------------------|
            |          |               |                       |
            |          ???               ???                       ???               
            |       profiles          txns                 statement 
            |  localhost:9100      localhost:9200        localhost:9300
            |   sleuth ???        sleuth ???                sleuth
            |   |      ???          |    ???                   |        
            |   |   profilesDB    |   txnsDB               |
            |---|-----------------|------------------------|
                                |
                                ???
                    Distributed Tracing Service
                            Zipkin-Server

        External Configuaration Pattern

            A config server is the one which has an underlying repository of config files.
            A microservice before gets active will contact the config server and pick its respective
            config file.

            A production owner or adminsitator can change the cofigs anytime and commit them into
            that repository, and the underlying microservices will be notifed.

                BudgetAnalysisSystem 
                    Angular APP/REactJS APP/Andriod APP (CLEINTS)
                                    |
                                    |
                                    ???
                                apiGatway                      Discovery Service
                            (spring cloud api gateway)        (Netflix Eureka Service)
            |------------------sleuth  |                                    |
            |                          |-address are feteched or registered-|
            |          |---------------|-----------------------|
            |          |               |                       |
            |          ???               ???                       ???               
            |       profiles           txns                 statement 
            |  localhost:9100      localhost:9200        localhost:9300
            |   sleuth ???        sleuth ???                sleuth
            |   |      ???          |    ???                   |        
            |   |   profilesDB    |   txnsDB               |
            |---|-----------------|------------------------|
                                |--------------------------------|
                                ???                                |
                    Distributed Tracing Service             Config service
                            Zipkin-Server                  Spring Cloud Config Service
                                                                    |
                                                                    |
                                                                config repo
                                                                    profiles.properties
                                                                    txns.properties
                                                                    statement.properties
        Circuit Breaking Pattern 

            1. when a microservice(A) needs to send a req to another microservice(B),
                if (B) is 'UP', the circuit is closed and the req will be made.
                if (B) is 'DOWN', then the circuit is broken (opened) and a 
                fallback operation takes place, the ciruit remains open for a given
                thrushold of time within which no req is made to (B) and only 
                fallback operations will occur. After the thrushold time lapses,
                a req is attempted and the cirucit is closed only when atleast one
                successful req is made.

                the thrushhold and fallback are developer controlled.

                we will be using resiliance4j to implement cirucit braking pattern.

    Microservices - BudgetAnalysisSystem - implementation
    ----------------------------------------------------------------------------------

        Step 1: Develop the core services and establish inter-service communication
            in.cts.budgetanalysis : profiles
                dependencies
                    org.springframework.boot:spring-boot-starter-web
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-openfeign
                    mysq1:mysql-connector-java
                    org.springframework.boot:spring-boot-starter-data-jpa
                configuaration
                    spring.application.name=profiles
                    server.port=9100

                    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
                    spring.datasource.username=root
                    spring.datasource.password=root
                    spring.datasource.url=jdbc:mysql://localhost:3306/bapsDB?createDatabaseIfNotExist=true
                    spring.jpa.hibernate.ddl-auto=update

            in.cts.budgetanalysis : txns
                dependencies
                    org.springframework.boot:spring-boot-starter-web
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-openfeign
                    mysq1:mysql-connector-java
                    org.springframework.boot:spring-boot-starter-data-jpa
                configuaration
                    spring.application.name=txns
                    server.port=9200

                    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
                    spring.datasource.username=root
                    spring.datasource.password=root
                    spring.datasource.url=jdbc:mysql://localhost:3306/batxnsDB?createDatabaseIfNotExist=true
                    spring.jpa.hibernate.ddl-auto=update

            in.cts.budgetanalysis : statement
                dependencies
                    org.springframework.boot:spring-boot-starter-web
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-openfeign
                configuaration
                    spring.application.name=statement
                    server.port=9300

        Step 2: Implement Discovery Service Design Pattern and Client Side Load Balancing
            in.cts.budgetanalysis : discovery
                dependencies
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-starter-netflix-eureka-server
                configuaration
                    @EnableEurekaServer    on Application class

                    spring.application.name=discovery
                    server.port=9000

                    eureka.instance.hostname=localhost
                    eureka.client.registerWithEureka=false
                    eureka.client.fetchRegistry=false
                    eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
                    eureka.server.waitTimeInMsWhenSyncEmpty=0

            in.cts.budgetanalysis : profiles
                dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                    ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
                configuaration
                    ++@EnableDiscoveryClient  on Application class

                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                    eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                    eureka.client.registryFetchIntervalSeconds=5
                    eureka.instance.leaseRenewalIntervalInSeconds=5
                    eureka.instance.leaseExpirationDurationInSeconds=5

                    spring.cloud.loadbalancer.ribbon.enabled=false

            in.cts.budgetanalysis : txns
               dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                    ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
                configuaration
                    ++@EnableDiscoveryClient  on Application class

                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                    eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                    eureka.client.registryFetchIntervalSeconds=5
                    eureka.instance.leaseRenewalIntervalInSeconds=5
                    eureka.instance.leaseExpirationDurationInSeconds=5

                    spring.cloud.loadbalancer.ribbon.enabled=false

            in.cts.budgetanalysis : statement
               dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                    ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
                configuaration
                    ++@EnableDiscoveryClient  on Application class

                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                    eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                    eureka.client.registryFetchIntervalSeconds=5
                    eureka.instance.leaseRenewalIntervalInSeconds=5
                    eureka.instance.leaseExpirationDurationInSeconds=5

                    spring.cloud.loadbalancer.ribbon.enabled=false    

        Step 3: Implement API Gateway Design Pattern
            in.cts.budgetanalysis : gateway
                dependencies
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-starter-api-gateway
                    org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                    org.springframework.cloud:spring-cloud-starter-loadbalancer
                configuaration
                    @EnableDiscoveryClient          on Application class

                    spring.application.name=gateway
                    server.port=9999

                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                    eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                    eureka.client.registryFetchIntervalSeconds=5
                    eureka.instance.leaseRenewalIntervalInSeconds=5
                    eureka.instance.leaseExpirationDurationInSeconds=5

                    spring.cloud.gateway.discovery.locator.enabled=true
                    spring.cloud.gateway.discovery.locator.lower-case-service-id=true
                    
            in.cts.budgetanalysis : discovery
            in.cts.budgetanalysis : profiles
            in.cts.budgetanalysis : txns
            in.cts.budgetanalysis : statement
                  
        Step 4: Implement Distributed Tracing Design Pattern
              in.cts.budgetanalysis : discovery
              
              in.cts.budgetanalysis : gateway
                dependencies
                    ++org.springframework.boot:spring-boot-starter-actuator
                    ++org.springframework.cloud:spring-cloud-starter-sleuth
                    ++org.springframework.cloud:spring-cloud-starter-zipkin : 2.2.8.RELEASE
                
                configuaration
                    logger.level.org.springramework.web=debug
                    management.endpoints.web.exposure.include=*
           
            in.cts.budgetanalysis : profiles
                dependencies
                    ++org.springframework.boot:spring-boot-starter-actuator
                    ++org.springframework.cloud:spring-cloud-starter-sleuth
                    ++org.springframework.cloud:spring-cloud-starter-zipkin : 2.2.8.RELEASE
                
                configuaration
                    logger.level.org.springramework.web=debug
                    management.endpoints.web.exposure.include=*

            in.cts.budgetanalysis : txns
                dependencies
                    ++org.springframework.boot:spring-boot-starter-actuator
                    ++org.springframework.cloud:spring-cloud-starter-sleuth
                    ++org.springframework.cloud:spring-cloud-starter-zipkin : 2.2.8.RELEASE
                
                configuaration
                    logger.level.org.springramework.web=debug
                    management.endpoints.web.exposure.include=*

            in.cts.budgetanalysis : statement
                dependencies
                    ++org.springframework.boot:spring-boot-starter-actuator
                    ++org.springframework.cloud:spring-cloud-starter-sleuth
                    ++org.springframework.cloud:spring-cloud-starter-zipkin : 2.2.8.RELEASE
                
                configuaration
                    logger.level.org.springramework.web=debug
                    management.endpoints.web.exposure.include=*

            tracing-service
                zipkin-server
                    https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec 
                    
                    java -jar zipkin.jar

        Step 5: Implement Circuit Breaker Design Pattern
            in.cts.budgetanalysis : discovery  
            in.cts.budgetanalysis : gateway
            in.cts.budgetanalysis : profiles
            in.cts.budgetanalysis : txns
                dependencies
                    ++org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j
                
                configuaration
                    resilience4j.circuitbreaker.configs.default.registerHealthIndicator=true
                    resilience4j.circuitbreaker.configs.default.ringBufferSizeInClosedState=4
                    resilience4j.circuitbreaker.configs.default.ringBufferSizeInHalfOpenState=2
                    resilience4j.circuitbreaker.configs.default.automaticTransitionFromOpenToHalfOpenEnabled=true
                    resilience4j.circuitbreaker.configs.default.waitDurationInOpenState= 20s
                    resilience4j.circuitbreaker.configs.default.failureRateThreshold= 50
                    resilience4j.circuitbreaker.configs.default.eventConsumerBufferSize= 10

            in.cts.budgetanalysis : statement
               dependencies
                    ++org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j
                
                configuaration
                    resilience4j.circuitbreaker.configs.default.registerHealthIndicator=true
                    resilience4j.circuitbreaker.configs.default.ringBufferSizeInClosedState=4
                    resilience4j.circuitbreaker.configs.default.ringBufferSizeInHalfOpenState=2
                    resilience4j.circuitbreaker.configs.default.automaticTransitionFromOpenToHalfOpenEnabled=true
                    resilience4j.circuitbreaker.configs.default.waitDurationInOpenState= 20s
                    resilience4j.circuitbreaker.configs.default.failureRateThreshold= 50
                    resilience4j.circuitbreaker.configs.default.eventConsumerBufferSize= 10

        Step 6: External Configuaration Design Pattern
            inTheWorkSpace> md bt-props-repo
                //and then create these files in this directory
                    // gateway.properties
                    // profiles.properties
                    // txns.properties
                    // statement.properties
                    // move the content of 'application.properties' of each microservice into these respective files
                    
                inTheWorkSpace> cd bt-props-repo
                inTheWorkSpace\bt-props-repo> git init           
                inTheWorkSpace\bt-props-repo> git add .
                inTheWorkSpace\bt-props-repo> git commit -m "all service properties"
            
            in.cts.budgetanalysis : discovery
            in.cts.budgetanalysis : config
                dependencies
                    org.springframework.boot:spring-boot-devtools
                    org.springframework.cloud:spring-cloud-config-server
                    org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                
                configuaration  
                    @EnableDiscoveryClient
                    @EnableConfigServer             on Application class

                    spring.application.name=config
                    server.port=9090

                    spring.cloud.config.server.git.uri=file:///local/git/repo/path

                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                    eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                    eureka.client.registryFetchIntervalSeconds=5
                    eureka.instance.leaseRenewalIntervalInSeconds=5
                    eureka.instance.leaseExpirationDurationInSeconds=5
            
            in.cts.budgetanalysis : gateway
                dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-bootstrap
                    ++ org.springframework.cloud:spring-cloud-config-client

                configuaration - bootstrap.properties
                    spring.cloud.config.name=gateway
                    spring.cloud.config.discovery.service-id=config
                    spring.cloud.config.discovery.enabled=true
                    
                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/                    
            
            in.cts.budgetanalysis : profiles
                dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-bootstrap
                    ++ org.springframework.cloud:spring-cloud-config-client

                configuaration - bootstrap.properties
                    spring.cloud.config.name=profiles
                    spring.cloud.config.discovery.service-id=config
                    spring.cloud.config.discovery.enabled=true
                    
                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/   

            in.cts.budgetanalysis : txns
                dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-bootstrap
                    ++ org.springframework.cloud:spring-cloud-config-client

                configuaration - bootstrap.properties
                    spring.cloud.config.name=txns
                    spring.cloud.config.discovery.service-id=config
                    spring.cloud.config.discovery.enabled=true
                    
                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/   

            in.cts.budgetanalysis : statement
                dependencies
                    ++ org.springframework.cloud:spring-cloud-starter-bootstrap
                    ++ org.springframework.cloud:spring-cloud-config-client

                configuaration - bootstrap.properties
                    spring.cloud.config.name=statement
                    spring.cloud.config.discovery.service-id=config
                    spring.cloud.config.discovery.enabled=true
                    
                    eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/   

    Assignement - D2HConsumerSelfServiceSystem
    -----------------------------------------------------------

        Consumers
            1. Each consuemr to register 
            2. Retrive Consuemr Details

        Channels
            1. Add channels (chid,title,ratePerMonth)
            2. Edit channel
            3. Delete Channel ..etc

        Subscriptions
            
            Each consumer can have any number of subscriptions
            Each subscription will have (supId,startDate,endDate,status,channelId)

            1. Add susbcriptions
            2. Delete subscriptions
            3. Renew an existing subscription
            4. Retrive the consuemr detials along with list of active subscriptions.

    