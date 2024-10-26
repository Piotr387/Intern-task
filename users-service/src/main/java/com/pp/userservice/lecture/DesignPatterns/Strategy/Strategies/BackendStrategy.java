package com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies;

import com.pp.userservice.lecture.DesignPatterns.Strategy.SyllabusStrategy;

import java.util.*;

// l3z5
public class BackendStrategy implements SyllabusStrategy {
    @Override
    public List<Map<String, List<String>>> show() {
        List<Map<String, List<String>>> syllabus = new ArrayList<>();
        syllabus.add(createDatabaseSection());
        syllabus.add(createServerSideSection());
        syllabus.add(createApiSection());
        syllabus.add(createSecuritySection());
        syllabus.add(createDevOpsSection());
        return syllabus;
    }

    private Map<String, List<String>> createDatabaseSection() {
        Map<String, List<String>> databaseSection = new LinkedHashMap<>();
        databaseSection.put("Relational Databases", Arrays.asList(
                "Introduction to SQL", "Normalization", "Joins", "Transactions", "Indexes", "Stored Procedures", "ACID Properties"));
        databaseSection.put("NoSQL Databases", Arrays.asList(
                "Document Stores (e.g., MongoDB)", "Key-Value Stores", "Column-family Stores", "Graph Databases"));
        databaseSection.put("Database Design", Arrays.asList(
                "ER Diagrams", "Schema Design", "Database Sharding", "Data Replication"));
        return databaseSection;
    }

    private Map<String, List<String>> createServerSideSection() {
        Map<String, List<String>> serverSideSection = new LinkedHashMap<>();
        serverSideSection.put("Java and Spring Boot", Arrays.asList(
                "Java Basics", "Spring Boot Introduction", "Dependency Injection", "Spring Data JPA", "REST API Development", "Spring Security"));
        serverSideSection.put("Node.js and Express", Arrays.asList(
                "Node.js Basics", "Express Framework", "Routing", "Middleware", "Error Handling", "Building REST APIs"));
        serverSideSection.put("Python and Django", Arrays.asList(
                "Python Basics", "Django Overview", "MVC Pattern", "ORM", "Django REST Framework", "Authentication and Authorization"));
        return serverSideSection;
    }

    private Map<String, List<String>> createApiSection() {
        Map<String, List<String>> apiSection = new LinkedHashMap<>();
        apiSection.put("RESTful APIs", Arrays.asList(
                "What is REST?", "HTTP Methods (GET, POST, PUT, DELETE)", "Status Codes", "Authentication (OAuth, JWT)", "Versioning APIs"));
        apiSection.put("GraphQL", Arrays.asList(
                "Introduction to GraphQL", "Queries and Mutations", "Resolvers", "Schema Design", "GraphQL vs REST"));
        apiSection.put("gRPC", Arrays.asList(
                "Introduction to gRPC", "Protocol Buffers", "Unary vs Streaming RPCs", "Client and Server Implementation", "Authentication in gRPC"));
        return apiSection;
    }

    private Map<String, List<String>> createSecuritySection() {
        Map<String, List<String>> securitySection = new LinkedHashMap<>();
        securitySection.put("Authentication", Arrays.asList(
                "Basic Authentication", "OAuth 2.0", "JWT (JSON Web Tokens)", "Single Sign-On (SSO)"));
        securitySection.put("Security Best Practices", Arrays.asList(
                "CORS", "Cross-Site Scripting (XSS)", "SQL Injection Prevention", "Secure Password Storage", "Data Encryption", "Rate Limiting", "Security Headers"));
        return securitySection;
    }

    private Map<String, List<String>> createDevOpsSection() {
        Map<String, List<String>> devOpsSection = new LinkedHashMap<>();
        devOpsSection.put("Continuous Integration/Continuous Deployment (CI/CD)", Arrays.asList(
                "CI/CD Pipelines", "Jenkins, CircleCI, GitLab CI", "Automated Testing", "Deployment Strategies (Blue-Green, Canary)"));
        devOpsSection.put("Containers & Orchestration", Arrays.asList(
                "Introduction to Docker", "Docker Compose", "Kubernetes Basics", "Service Discovery", "Scaling and Load Balancing"));
        devOpsSection.put("Cloud Platforms", Arrays.asList(
                "AWS, Azure, GCP", "Serverless Architecture", "Infrastructure as Code (Terraform)", "Monitoring and Logging"));
        return devOpsSection;
    }
}
