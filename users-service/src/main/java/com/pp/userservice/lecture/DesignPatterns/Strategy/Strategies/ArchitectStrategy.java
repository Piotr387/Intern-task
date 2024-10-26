package com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies;

import com.pp.userservice.lecture.DesignPatterns.Strategy.SyllabusStrategy;

import java.util.*;

// l3z5
public class ArchitectStrategy implements SyllabusStrategy {
    @Override
    public List<Map<String, List<String>>> show() {
        List<Map<String, List<String>>> syllabus = new ArrayList<>();
        syllabus.add(createFundamentalsSection());
        syllabus.add(createStylesSection());
        syllabus.add(createDesignPrinciplesSection());
        syllabus.add(createNfrSection());
        syllabus.add(createSystemDesignSection());
        syllabus.add(createDistributedSystemsSection());
        syllabus.add(createTradeoffsSection());
        return syllabus;
    }

    private Map<String, List<String>> createFundamentalsSection() {
        Map<String, List<String>> fundamentalsSection = new LinkedHashMap<>();
        fundamentalsSection.put("Introduction to Software Architecture", Arrays.asList(
                "What is Software Architecture?", "Role of an Architect", "Architecture vs Design"));
        fundamentalsSection.put("Architectural Patterns", Arrays.asList(
                "Layered Architecture", "Event-driven Architecture", "Microkernel Architecture", "Microservices Architecture", "Serverless Architecture"));
        fundamentalsSection.put("Key Architectural Concepts", Arrays.asList(
                "Separation of Concerns", "Abstraction", "Modularity", "Scalability", "Maintainability"));
        return fundamentalsSection;
    }

    private Map<String, List<String>> createStylesSection() {
        Map<String, List<String>> stylesSection = new LinkedHashMap<>();
        stylesSection.put("Monolithic Architecture", Arrays.asList(
                "Characteristics", "Benefits and Drawbacks", "Deployment", "Use Cases"));
        stylesSection.put("Service-Oriented Architecture (SOA)", Arrays.asList(
                "SOA Principles", "Web Services", "Enterprise Service Bus (ESB)", "Loose Coupling"));
        stylesSection.put("Microservices Architecture", Arrays.asList(
                "Characteristics of Microservices", "Communication Patterns", "Data Management in Microservices", "Challenges of Microservices"));
        stylesSection.put("Event-Driven Architecture", Arrays.asList(
                "Event Sourcing", "CQRS (Command Query Responsibility Segregation)", "Message Brokers", "Use Cases"));
        stylesSection.put("Serverless Architecture", Arrays.asList(
                "Serverless Functions (AWS Lambda, Azure Functions)", "Event Triggers", "Scaling", "Cost Considerations"));
        return stylesSection;
    }

    private Map<String, List<String>> createDesignPrinciplesSection() {
        Map<String, List<String>> designPrinciplesSection = new LinkedHashMap<>();
        designPrinciplesSection.put("SOLID Principles", Arrays.asList(
                "Single Responsibility Principle", "Open/Closed Principle", "Liskov Substitution Principle", "Interface Segregation Principle", "Dependency Inversion Principle"));
        designPrinciplesSection.put("DRY, KISS, and YAGNI", Arrays.asList(
                "Don't Repeat Yourself (DRY)", "Keep It Simple, Stupid (KISS)", "You Aren't Gonna Need It (YAGNI)"));
        designPrinciplesSection.put("Design Patterns", Arrays.asList(
                "Creational Patterns", "Structural Patterns", "Behavioral Patterns", "Concurrency Patterns"));
        return designPrinciplesSection;
    }

    private Map<String, List<String>> createNfrSection() {
        Map<String, List<String>> nfrSection = new LinkedHashMap<>();
        nfrSection.put("Scalability", Arrays.asList(
                "Horizontal vs Vertical Scaling", "Load Balancing", "Auto-scaling Strategies", "Caching"));
        nfrSection.put("Security", Arrays.asList(
                "Data Encryption", "Authentication & Authorization", "OWASP Top 10", "Securing APIs", "Identity and Access Management (IAM)"));
        nfrSection.put("Performance", Arrays.asList(
                "Latency and Throughput", "Caching Strategies", "Database Optimization", "Asynchronous Processing"));
        nfrSection.put("Reliability", Arrays.asList(
                "Redundancy", "Failover Mechanisms", "Data Backup and Recovery", "Disaster Recovery"));
        nfrSection.put("Maintainability", Arrays.asList(
                "Code Refactoring", "Modularity", "Automated Testing", "Continuous Integration/Continuous Deployment (CI/CD)"));
        return nfrSection;
    }

    private Map<String, List<String>> createSystemDesignSection() {
        Map<String, List<String>> systemDesignSection = new LinkedHashMap<>();
        systemDesignSection.put("High-Level System Design", Arrays.asList(
                "Component Diagrams", "Data Flow Diagrams", "Entity-Relationship Diagrams (ERD)", "UML Diagrams"));
        systemDesignSection.put("Architecture Decision Records (ADR)", Arrays.asList(
                "Documenting Key Decisions", "Trade-offs", "Justification of Decisions", "Versioning ADRs"));
        systemDesignSection.put("Technical Documentation", Arrays.asList(
                "API Documentation", "Database Schemas", "Infrastructure Diagrams", "Deployment Guides"));
        return systemDesignSection;
    }

    private Map<String, List<String>> createDistributedSystemsSection() {
        Map<String, List<String>> distributedSystemsSection = new LinkedHashMap<>();
        distributedSystemsSection.put("Distributed Systems", Arrays.asList(
                "Characteristics of Distributed Systems", "Consistency, Availability, Partition Tolerance (CAP Theorem)", "Replication", "Sharding"));
        distributedSystemsSection.put("Cloud Architecture", Arrays.asList(
                "Cloud Models (IaaS, PaaS, SaaS)", "Designing for Cloud Scalability", "Cloud-Native Applications", "Serverless Architectures in Cloud"));
        distributedSystemsSection.put("Hybrid and Multi-Cloud Architecture", Arrays.asList(
                "Hybrid Cloud Models", "Multi-Cloud Strategies", "Cloud Provider Agnosticism", "Data Migration Strategies"));
        return distributedSystemsSection;
    }

    private Map<String, List<String>> createTradeoffsSection() {
        Map<String, List<String>> tradeoffsSection = new LinkedHashMap<>();
        tradeoffsSection.put("Evaluating Trade-offs", Arrays.asList(
                "Performance vs Cost", "Scalability vs Complexity", "Monolithic vs Microservices", "Availability vs Consistency"));
        tradeoffsSection.put("Designing for Change", Arrays.asList(
                "Anticipating Future Requirements", "Building Extensible Systems", "Versioning and Backward Compatibility"));
        tradeoffsSection.put("Risk Management", Arrays.asList(
                "Identifying Architectural Risks", "Risk Mitigation Strategies", "Monitoring and Observability"));
        return tradeoffsSection;
    }
}
