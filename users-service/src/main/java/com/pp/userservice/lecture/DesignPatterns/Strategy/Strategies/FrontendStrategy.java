package com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies;

import com.pp.userservice.lecture.DesignPatterns.Strategy.SyllabusStrategy;

import java.util.*;

// l3z5
public class FrontendStrategy implements SyllabusStrategy {
    @Override
    public List<Map<String, List<String>>> show() {
        List<Map<String, List<String>>> syllabus = new ArrayList<>();
        syllabus.add(createHtmlCssSection());
        syllabus.add(createJsBasicsSection());
        syllabus.add(createApiSection());
        syllabus.add(createFrameworksSection());
        return syllabus;
    }

    private Map<String, List<String>> createHtmlCssSection() {
        Map<String, List<String>> htmlCssSection = new LinkedHashMap<>();
        htmlCssSection.put("HTML Basics", Arrays.asList(
                "HTML Syntax", "Headings, Paragraphs, and Text", "Links and Images", "Lists and Tables", "Forms and Input Elements"));
        htmlCssSection.put("CSS Basics", Arrays.asList(
                "CSS Syntax", "Selectors and Combinators", "Colors, Fonts, and Text Styles", "Box Model", "Positioning and Layout", "Flexbox", "Grid"));
        htmlCssSection.put("Responsive Design", Arrays.asList(
                "Media Queries", "Mobile-first Design", "Viewport Meta Tag", "CSS Units (rem, em, vh, etc.)", "Responsive Images"));
        return htmlCssSection;
    }

    private Map<String, List<String>> createJsBasicsSection() {
        Map<String, List<String>> jsBasicsSection = new LinkedHashMap<>();
        jsBasicsSection.put("JavaScript Syntax & Basics", Arrays.asList(
                "Variables and Data Types", "Operators and Expressions", "Conditional Statements", "Loops (for, while, do-while)", "Functions", "Scope and Hoisting", "Arrays and Objects"));
        jsBasicsSection.put("DOM Manipulation", Arrays.asList(
                "Selecting Elements", "Creating and Modifying Elements", "Event Handling", "Forms and Input Validation"));
        jsBasicsSection.put("ES6+ Features", Arrays.asList(
                "Let and Const", "Arrow Functions", "Template Literals", "Destructuring", "Modules (import/export)", "Promises and Async/Await"));
        return jsBasicsSection;
    }

    private Map<String, List<String>> createApiSection() {
        Map<String, List<String>> apiSection = new LinkedHashMap<>();
        apiSection.put("REST APIs", Arrays.asList(
                "What is an API?", "Making API Calls with Fetch", "Understanding JSON", "Error Handling", "Working with Async/Await"));
        apiSection.put("Working with External APIs", Arrays.asList(
                "Using Public APIs", "Authentication (API Keys, OAuth)", "Handling Responses and Data"));
        return apiSection;
    }

    private Map<String, List<String>> createFrameworksSection() {
        Map<String, List<String>> frameworksSection = new LinkedHashMap<>();
        frameworksSection.put("React Basics", Arrays.asList(
                "What is React?", "Components and JSX", "State and Props", "Event Handling", "Lifecycle Methods", "Hooks (useState, useEffect)"));
        frameworksSection.put("Vue.js Basics", Arrays.asList(
                "Components", "Vue Directives", "Reactivity", "Vue Router", "Vuex for State Management"));
        return frameworksSection;
    }
}
