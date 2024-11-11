## Assignment to be Implemented:

1. Search for the user with username “Delphine”.
2. Use the details fetched to make a search for the posts written by the user.
3. For each post, fetch the comments and validate if the emails in the comment section are in the proper format.
4. Think of various scenarios for the test workflow and all the things that can go wrong. Add them to test coverage.

---

## High-Level Framework Implemented:

For implementing the above task, below is the technical stack used:
1. **Programming Language**: Java
2. **IDE Used**: Eclipse
3. **Libraries**: 
   - **Rest Assured**
   - **TestNG**

---

## Framework Overview at a High Level:

### 1. Setup Maven Project
Created a Maven Project in Eclipse with a `POM.xml`. Necessary dependencies like Rest Assured, TestNG, Extent Reports, Surefire plugin, etc., were added.

### 2. Base Test Class Setup
Created an `InvokeAPIHelper.java` class with common methods to call the GET, POST APIs, which are used in the tests. This base class has common configuration settings like authentication headers.

### 3. Setup the Test Class
Created the test class `APITests.java` for different API tests using TestNG annotations.

### 4. Utility Classes
Created utility classes like:
- **JsonReader.java** to read the input JSON for environment and endpoint details.
- **ReusableFunctions.java** for reusable methods used for response validation.
- **ExtentReportManager.java** to integrate with Extent Reports for enhanced reporting.

---

## High-Level Functional Flow of the Framework:

### 1. Execution with TestNG's `Testing.xml`:
The framework uses a `testing.xml` configuration file to trigger test execution, allowing the creation of different test suites for flexible and organized test runs.

### 2. Test Execution with `APITests.java`:
Core tests are defined within `APITests.java` using TestNG annotations like `@Test` for structuring tests. This class serves as the entry point for executing scenarios defined in `testing.xml`.

### 3. Priority-Driven Test Execution:
Tests run in a specific order using TestNG's `@Priority` annotation, ensuring a controlled sequence, especially for scenarios dependent on prior tests' outcomes.

### 4. Base URL and Endpoint Configuration:
The framework reads base URLs and resource paths from `BlogPostEndpoints.json`, centralizing configuration for reusability and simplified updates.

### 5. User API Retrieval and Variable Storage:
The first test retrieves user data from the users API and searches for the username "Delphine". Upon finding the user, their `userId` is stored for use in later tests.

### 6. Dependency Handling with TestNG's `@DependsOnMethod`:
`userId` retrieved in Test 1 is passed to Test 2 using the `@DependsOnMethod` annotation, ensuring logical flow by executing the second test only after the first's success.

### 7. Capturing Post IDs in Test 2:
The posts API is called using `userId` from Test 1. The response captures all associated `postIds`, storing them in a list object for use in the next test.

### 8. Retrieving Comments and Validating Emails in Test 3:
Test 3 iterates through each `postId` captured in Test 2, calling the API to retrieve associated comments. A reusable method validates the format of each commenter's email.

### 9. Logging Results in Extent Reports:
Results and relevant information are logged using Extent Reports, generating an HTML report with a date-time stamp in the `Reports` folder, providing detailed insights into test execution.

### 10. Assertions and Fine-Tuning:
Basic assertions like response code checks are implemented, with options for additional assertions on response headers, response times, and specific JSON fields.

---

## Version Control with Git:

The framework is version-controlled using Git, ensuring consistent tracking and management of all development changes. Updates and modifications are systematically committed and pushed to the repository with descriptive comments. This approach maintains a detailed version history and facilitates collaboration, code reviews, and seamless rollbacks or enhancements when needed.

### GitHub Repository:

[BlogPostValidation GitHub Repository](https://github.com/shivakumardabbiru/BlogPostValidation.git)

---

## Continuous Integration with CircleCI:

The Git repository is integrated with CircleCI for continuous integration. Any changes made and pushed to the Git repository automatically trigger a CircleCI pipeline job that validates tests. This integration is achieved using a `.circleci` directory with a `config.yml` file containing all necessary pipeline configuration details. Upon execution, the job generates an Extent Report HTML file, stored under the designated artifacts section, providing a summary of test results and execution status. This setup ensures an efficient feedback loop, enhancing code quality and enabling rapid iterations.
