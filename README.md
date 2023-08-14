# CodeRun
GitHub License ![License](https://img.shields.io/github/license/gitninja02/coderun)

GitHub Stars ![GitHub stars](https://img.shields.io/github/license/gitninja02/coderun)


CodeRun is a versatile tool that allows users to submit code snippets in various programming languages and receive real-time execution results. It's designed for scenarios where quick code validation, testing, or learning is needed without setting up a local development environment. This project leverages the power of concurrent execution to ensure fast and efficient code processing.

## Features
- <strong><span style="color:pink;">Multi-Language Support : </span></strong>Execute code in a wide range of programming languages.
- <strong><span style="color:maroon;">Concurrent Execution:</strong> Process multiple code submissions simultaneously for optimal performance.
- <strong><span style="color:yellow;">Real-Time Results:</strong>  Get instant feedback on code execution, including output and errors.
- <strong><span style="color:green;">Caching:</strong>  Improved performance and speed through caching using <strong><em>Redis</em></strong>.
- <strong><span style="color:violet;">Easy Integration :</strong>  Simple API endpoints make integration into applications and services seamless.

## Usage
### Submit Code
- Make a POST request to the <strong>'/execute'</strong> endpoint with the following JSON payload:


```json 
{
    "language": "python",
    "code": "print('Hello, world!')"
}
```

- Receive Response: The API will respond with the execution results, including output and errors:

``` json
{
    "stdout": "Hello, world!\n",
    "stderr": ""
}
```

## Supported Languages
- Python
- JavaScript
- Java
- C++

## Installation and Setup
1. Clone this repository:

``` bash
git clone https://github.com/gitninja02/coderun.git
cd coderun
```

2. Install dependencies:

``` bash
mvn install
```

3. Start the server:
``` bash
mvn spring-boot:run
```

### Contributing
Contributions are welcome! If you'd like to contribute to this project, please follow these steps:

1. Fork the repository.

Create a new branch for your feature/fix:

``` bash
git checkout -b feature-new-feature
```
2. Commit your changes and push to your forked repository.

3. Submit a pull request detailing your changes.

### License
This project is licensed under the MIT License.

By Hari Vishwanath

Project Demo(Link to be added)