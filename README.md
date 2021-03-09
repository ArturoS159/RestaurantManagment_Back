
## Table of contents
* [General info](#general-info)
* [Setup](#setup)
* [Technologies](#technologies)
* [Frontend](#frontend)

## General info
![Background](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/backend.png?raw=true "Backend")

The backend is written in Java using Spring Boot and Spring Cloud technologies using the Kubernetes platform.
Using a web browser both from a computer and from a mobile device, we communicate with the Gateway Server, which is the only one with an IP address available from outside. We improve security by preventing the user from direct communication with modules and all network traffic is sent through the gate. Each microservice can work independently of others and have its own database, which gives great flexibility when choosing the type of database.

## Setup
 1. Requires before
     - Docker desktop [tutorial](https://docs.docker.com/docker-for-windows/install/)
     - Maven [tutorial](https://maven.apache.org/download.cgi)
     - Minikube [tutorial](https://minikube.sigs.k8s.io/docs/start/)
     - Helm [tutorial](https://helm.sh/docs/intro/install/)
     
 2. Install Apache Kafka & Prometheus on Kubernetes

    2.1 Create namespace for Kafka and Promethues
    ```
    minikube create namespace kafka
    minikube create namespace monitoring
    ```

    2.2 Add kafka to k8s (kafka.yaml is located in K8S folder)
    ```
    helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/sfsf
    helm repo update
    helm install kafka -n kafka -f .\kafka.yaml confluentinc/cp-helm-charts
    ```
    Now you should see
    
    ![Kafka](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/kafka.png?raw=true "Kafka")
    
    2.3 Install Promethues and PromethuesAdapter for custom metrics for autoscaling per requests
    ```
    helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
    helm repo update
    helm install prometheus prometheus-community/prometheus -n monitoring
    helm install -f adapter.yaml prometheus-adapter prometheus-community/prometheus-adapter -n monitoring
    ```
    In adapter file there is pre configured custom metrics.
    
    Should look like this:
    
    ![Promethues](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/prometheus.png?raw=true "Promethues")

3. Deploy microservices to k8s

    3.1. Create images by maven
    ```
    mvn clean package -DskipTests=true
    ```
   
    ![Maven](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/prometheus.png?raw=true "Maven")

    3.2 Enter to docker engine in minikube (windows command)
    ```
    minikube -p minikube docker-env | Invoke-Expression
    ```
    3.3 Now we are able to push images do minikube
    ```
    docker build -t gateway-server gateway-server\ --force-rm
    docker build -t config-server config-server\ --force-rm
    docker build -t order-service order-service\ --force-rm
    docker build -t restaurant-service restaurant-service\ --force-rm
    docker build -t auth-server authorization-server\ --force-rm
    docker build -t resource-server resource-server\ --force-rm
    ```
    3.4 Deploy configs (located in K8S/deploy)
    ```
    kubectl apply -f .\K8S\deploy\.
    ```
   
    ![Pods](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/pods.png?raw=true "Pods")


4. Testing autoscaling
    ```
    minikube tunnel
    ```
   
    After this command we can connect via gateway on localhost
   
    If I do requests on auth-server for example. Prometheus will scrap metrics from pod auth-server.

    ![Hpa](https://github.com/ArturoS159/RestaurantManagment_Back/blob/master/images/hpa.png?raw=true "Hpa")
    
    Now we have autoscaling microservice server.
   
    Great :punch:
## Technologies
Project is created with:
 1. Java 11
 2. Spring Boot
 3. Spring Cloud
 4. Apache Kafka
 5. PostgreSQL
 6. Kubernetes 
## Frontend
[React](https://github.com/ArturoS159/RestaurantManagment_Front)
