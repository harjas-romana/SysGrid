# SysGrid

**A lightweight, in-memory graph topology service built with Spring Boot.**

SysGrid is a streamlined application designed to manage and expose system topologies using an in-memory graph structure consisting of `Node` and `Edge` entities. It is ideal for rapid prototyping, mocking network architectures, or testing graph-based logic without the overhead of database configuration.

---

## 🚀 Key Features

* **In-Memory Data Store:** Fast, volatile storage for graph operations, perfect for local development and testing.
* **RESTful API:** Exposes endpoints to retrieve and interact with the network topology.
* **Zero Configuration:** No database setup, authentication, or external dependencies required.
* **Clean Architecture:** Built with Spring Boot and utilizes Lombok to reduce boilerplate code and maintain readability.

## 📂 Project Structure

The application follows a standard Spring Boot architectural pattern. Key components include:

* **Entry Point:** [`SysGridApplication.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/SysGridApplication.java)
* **API Layer:** [`GraphController.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/controller/GraphController.java) handles incoming HTTP requests.
* **Business Logic:** [`GraphService.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/service/GraphService.java) manages the creation, deletion, and retrieval of nodes and edges.
* **Domain Models:** * [`Graph.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/model/Graph.java)
* [`Node.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/model/Node.java)
* [`Edge.java`](https://www.google.com/search?q=src/main/java/com/sysgrid/model/Edge.java)



## 🛠️ Getting Started

### Prerequisites

* Java Development Kit (JDK) installed (Java 17 or higher recommended).
* An IDE with Lombok plugin support enabled.

### Running the Application

You can start the application directly from the repository root using the Gradle Wrapper:

```bash
./gradlew bootRun

```

### Verifying the Setup

Once the application is running, you can retrieve the current graph topology by hitting the exposed endpoint. Open a new terminal window and execute:

```bash
curl -s http://localhost:8080/api/graph | jq

```

## ⚙️ Development Notes

* **Volatility:** Because the data is stored in memory, all nodes and edges will be cleared upon application restart.
* **Lombok Requirement:** Ensure your IDE is configured to support Lombok annotations to prevent compilation warnings or errors in your editor.
* **Extensibility:** To expand the application's capabilities, add new route handlers in `GraphController` and implement the corresponding business logic in `GraphService`. The service layer already contains helper methods for basic node and edge management.

## 📄 License

This project is open-source and provided "as-is". You are free to use, modify, and distribute the code for personal or commercial purposes.

```
SysGrid
├─ .DS_Store
├─ README.md
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ sysgrid
   │  │        ├─ SysGridApplication.java
   │  │        ├─ controller
   │  │        │  └─ GraphController.java
   │  │        ├─ dto
   │  │        │  ├─ CreateEdgeRequest.java
   │  │        │  ├─ CreateNodeRequest.java
   │  │        │  └─ GraphResponse.java
   │  │        ├─ exceptions
   │  │        │  ├─ GlobalExceptionHandler.java
   │  │        │  └─ NodeNotFoundException.java
   │  │        ├─ model
   │  │        │  ├─ Edge.java
   │  │        │  ├─ Graph.java
   │  │        │  ├─ Node.java
   │  │        │  ├─ NodeStatus.java
   │  │        │  └─ NodeType.java
   │  │        └─ service
   │  │           └─ GraphService.java
   │  └─ resources
   │     └─ application.properties
   └─ test
      └─ java
         └─ com
            └─ sysgrid
               └─ SysGrid
                  └─ SysGridApplicationTests.java

```